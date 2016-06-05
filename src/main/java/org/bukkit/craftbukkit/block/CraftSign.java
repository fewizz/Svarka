package org.bukkit.craftbukkit.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftChatMessage;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class CraftSign extends CraftBlockState implements Sign {
    private final TileEntitySign sign;
    private final String[] lines;

    public CraftSign(final Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        sign = (TileEntitySign) world.getTileEntityAt(getX(), getY(), getZ());
        lines = new String[sign.signText.length];
        System.arraycopy(revertComponents(sign.signText), 0, lines, 0, lines.length);
    }

    public CraftSign(final Material material, final TileEntitySign te) {
        super(material);
        sign = te;
        lines = new String[sign.signText.length];
        System.arraycopy(revertComponents(sign.signText), 0, lines, 0, lines.length);
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int index) throws IndexOutOfBoundsException {
        return lines[index];
    }

    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        lines[index] = line;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
        	ITextComponent[] newLines = sanitizeLines(lines);
            System.arraycopy(newLines, 0, sign.signText, 0, 4);
            sign.markDirty();
        }

        return result;
    }

    public static ITextComponent[] sanitizeLines(String[] lines) {
    	ITextComponent[] components = new ITextComponent[4];

        for (int i = 0; i < 4; i++) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = new TextComponentString("");
            }
        }

        return components;
    }

    public static String[] revertComponents(ITextComponent[] components) {
        String[] lines = new String[components.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = revertComponent(components[i]);
        }
        return lines;
    }

    private static String revertComponent(ITextComponent component) {
        return CraftChatMessage.fromComponent(component);
    }

    @Override
    public TileEntitySign getTileEntity() {
        return sign;
    }
}
