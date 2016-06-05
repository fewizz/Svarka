package org.bukkit.craftbukkit.command;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.server.*;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.*;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper extends VanillaCommand {
    protected final CommandBase vanillaCommand;

    public VanillaCommandWrapper(CommandBase vanillaCommand) {
        super(vanillaCommand.getCommand());
        this.vanillaCommand = vanillaCommand;
    }

    public VanillaCommandWrapper(CommandBase vanillaCommand, String usage) {
        super(vanillaCommand.getCommand());
        this.vanillaCommand = vanillaCommand;
        this.description = "A Mojang provided command.";
        this.usageMessage = usage;
        this.setPermission("minecraft.command." + vanillaCommand.getCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        ICommandSender icommandlistener = getListener(sender);
        dispatchVanillaCommand(sender, icommandlistener, args);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List<String>) vanillaCommand.tabComplete(MinecraftServer.getServer(), getListener(sender), args, new BlockPos(0, 0, 0));
    }

    public static CommandSender lastSender = null; // Nasty :(

    public final int dispatchVanillaCommand(CommandSender bSender, ICommandSender icommandlistener, String[] as) {
        // Copied from net.minecraft.server.CommandHandler
        int i = getPlayerListSize(as);
        int j = 0;
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        WorldServer[] prev = MinecraftServer.getServer().worldServer;
        MinecraftServer server = MinecraftServer.getServer();
        server.worldServer = new WorldServer[server.worlds.size()];
        server.worldServer[0] = (WorldServer) icommandlistener.getWorld();
        int bpos = 0;
        for (int pos = 1; pos < server.worldServer.length; pos++) {
            WorldServer world = server.worlds.get(bpos++);
            if (server.worldServer[0] == world) {
                pos--;
                continue;
            }
            server.worldServer[pos] = world;
        }

        try {
            if (vanillaCommand.canUse(server, icommandlistener)) {
                if (i > -1) {
                    List<Entity> list = ((List<Entity>)EntitySelector.matchEntities(icommandlistener, as[i], Entity.class));
                    String s2 = as[i];
                    
                    icommandlistener.setCommandStat(Type.AFFECTED_ENTITIES, list.size());
                    Iterator<Entity> iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = iterator.next();

                        CommandSender oldSender = lastSender;
                        lastSender = bSender;
                        try {
                            as[i] = entity.getUniqueID().toString();
                            vanillaCommand.execute(server, icommandlistener, as);
                            j++;
                        } catch (WrongUsageException exceptionusage) {
                        	TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.getArgs())});
                            chatmessage.getChatModifier().setColor(TextFormatting.RED);
                            icommandlistener.addChatMessage(chatmessage);
                        } catch (CommandException commandexception) {
                        	CommandBase.notifyCommandListener(icommandlistener, vanillaCommand, 0, commandexception.getMessage(), commandexception.getErrorObjects());
                        } finally {
                            lastSender = oldSender;
                        }
                    }
                    as[i] = s2;
                } else {
                    icommandlistener.setCommandStat(Type.AFFECTED_ENTITIES, 1);
                    vanillaCommand.execute(server, icommandlistener, as);
                    j++;
                }
            } else {
            	TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                chatmessage.getChatModifier().setColor(TextFormatting.RED);
                icommandlistener.addChatMessage(chatmessage);
            }
        } catch (WrongUsageException exceptionusage) {
        	TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.getArgs()) });
            chatmessage1.getChatModifier().setColor(TextFormatting.RED);
            icommandlistener.addChatMessage(chatmessage1);
        } catch (CommandException commandexception) {
        	CommandBase.notifyCommandListener(icommandlistener, vanillaCommand, 0, commandexception.getMessage(), commandexception.getArgs());
        } catch (Throwable throwable) {
        	TextComponentTranslation chatmessage3 = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            chatmessage3.getChatModifier().setColor(TextFormatting.RED);
            icommandlistener.addChatMessage(chatmessage3);
            if (icommandlistener.getCommandSenderEntity() instanceof EntityMinecartCommandBlock) {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", icommandlistener.getPosition().getX(), icommandlistener.getPosition().getY(), icommandlistener.getPosition().getZ()), throwable);
            } else if(icommandlistener instanceof CommandBlockBaseLogic) {
            	CommandBlockBaseLogic listener = (CommandBlockBaseLogic) icommandlistener;
                MinecraftServer.LOGGER.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getPosition().getX(), listener.getPosition().getY(), listener.getPosition().getZ()), throwable);
            } else {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), throwable);
            }
        } finally {
            MinecraftServer.getServer().worldServer = prev;
        }
        icommandlistener.setCommandStat(Type.SUCCESS_COUNT, j);
        return j;
    }

    private ICommandSender getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlockLogic();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer)MinecraftServer.getServer()).rconConsoleSource;
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }
        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    private int getPlayerListSize(String as[]) {
        for (int i = 0; i < as.length; i++) {
            if (vanillaCommand.isUsernameIndex(as, i) && EntitySelector.matchesMultiplePlayers(as[i])) {
                return i;
            }
        }
        return -1;
    }

    public static String[] dropFirstArgument(String as[]) {
        String as1[] = new String[as.length - 1];
        for (int i = 1; i < as.length; i++) {
            as1[i - 1] = as[i];
        }

        return as1;
    }
}
