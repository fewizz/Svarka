package org.bukkit.craftbukkit;

import org.bukkit.Achievement;
import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.item.Item;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;

import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftStatistic {
    private static final BiMap<String, org.bukkit.Statistic> statistics;
    private static final BiMap<String, org.bukkit.Achievement> achievements;

    static {
        ImmutableMap<String, org.bukkit.Achievement> specialCases = ImmutableMap.<String, org.bukkit.Achievement> builder()
                .put("achievement.buildWorkBench", Achievement.BUILD_WORKBENCH)
                .put("achievement.diamonds", Achievement.GET_DIAMONDS)
                .put("achievement.portal", Achievement.NETHER_PORTAL)
                .put("achievement.ghast", Achievement.GHAST_RETURN)
                .put("achievement.theEnd", Achievement.END_PORTAL)
                .put("achievement.theEnd2", Achievement.THE_END)
                .put("achievement.blazeRod", Achievement.GET_BLAZE_ROD)
                .put("achievement.potion", Achievement.BREW_POTION)
                .build();
        ImmutableBiMap.Builder<String, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.<String, org.bukkit.Statistic>builder();
        ImmutableBiMap.Builder<String, org.bukkit.Achievement> achievementBuilder = ImmutableBiMap.<String, org.bukkit.Achievement>builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name()), statistic);
            }
        }
        for (Achievement achievement : Achievement.values()) {
            if (specialCases.values().contains(achievement)) {
                continue;
            }
            achievementBuilder.put("achievement." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, achievement.name()), achievement);
        }

        achievementBuilder.putAll(specialCases);

        statistics = statisticBuilder.build();
        achievements = achievementBuilder.build();
    }

    private CraftStatistic() {}

    public static org.bukkit.Achievement getBukkitAchievement(net.minecraft.stats.Achievement achievement) {
        return getBukkitAchievementByName(achievement.name);
    }

    public static org.bukkit.Achievement getBukkitAchievementByName(String name) {
        return achievements.get(name);
    }

    public static org.bukkit.Statistic getBukkitStatistic(StatBase statistic) {
        return getBukkitStatisticByName(statistic.name);
    }

    public static org.bukkit.Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity.")) {
            name = "stat.killEntity";
        }
        if (name.startsWith("stat.entityKilledBy.")) {
            name = "stat.entityKilledBy";
        }
        if (name.startsWith("stat.breakItem.")) {
            name = "stat.breakItem";
        }
        if (name.startsWith("stat.useItem.")) {
            name = "stat.useItem";
        }
        if (name.startsWith("stat.mineBlock.")) {
            name = "stat.mineBlock";
        }
        if (name.startsWith("stat.craftItem.")) {
            name = "stat.craftItem";
        }
        if (name.startsWith("stat.drop.")) {
            name = "stat.drop";
        }
        if (name.startsWith("stat.pickup.")) {
            name = "stat.pickup";
        }
        return statistics.get(name);
    }

    public static StatBase getNMSStatistic(org.bukkit.Statistic statistic) {
        return StatList.getOneShotStat(statistics.inverse().get(statistic));
    }

    public static net.minecraft.stats.Achievement getNMSAchievement(org.bukkit.Achievement achievement) {
        return (net.minecraft.stats.Achievement) StatList.getOneShotStat(achievements.inverse().get(achievement));
    }

    public static StatBase getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatList.a(CraftMagicNumbers.getBlock(material)); // PAIL: getMineBlockStatistic
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatList.a(CraftMagicNumbers.getItem(material)); // PAIL: getCraftItemStatistic
            }
            if (stat == Statistic.USE_ITEM) {
                return StatList.b(CraftMagicNumbers.getItem(material)); // PAIL: getUseItemStatistic
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatList.c(CraftMagicNumbers.getItem(material)); // PAIL: getBreakItemStatistic
            }
            if (stat == Statistic.DROP) {
                return StatList.e(CraftMagicNumbers.getItem(material)); // PAIL: getDropItemStatistic
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static StatBase getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
    	EntityEggInfo monsteregginfo = (EntityEggInfo) EntityList.ENTITY_EGGS.get(entity.getName());

        if (monsteregginfo != null) {
            if (stat == org.bukkit.Statistic.KILL_ENTITY) {
                return monsteregginfo.killEntityStat;
            }
            if (stat == org.bukkit.Statistic.ENTITY_KILLED_BY) {
                return monsteregginfo.entityKilledByStat;
            }
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(StatBase statistic) {
        String statisticString = statistic.name;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(StatBase statistic) {
        String statisticString = statistic.name;
        String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        Item item = (Item) Item.REGISTRY.getObject(new ResourceLocation(val));
        if (item != null) {
            return Material.getMaterial(Item.getIdFromItem(item));
        }
        Block block = (Block) Block.REGISTRY.get(new ResourceLocation(val));
        if (block != null) {
            return Material.getMaterial(Block.getIdFromBlock(block));
        }
        try {
            return Material.getMaterial(Integer.parseInt(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
