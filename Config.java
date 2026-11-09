package com.spawnintensity;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Settings, saved in config/spawnintensity-common.toml. */
public final class Config {
    public enum SpawnFilter { ALL, HOSTILE, ANIMALS }

    private static final ForgeConfigSpec.Builder B = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue SPAWN_MULT =
            B.comment("Natural mob spawn multiplier (1 = normal)").defineInRange("spawnMultiplier", 1, 1, 100);
    public static final ForgeConfigSpec.EnumValue<SpawnFilter> FILTER =
            B.comment("Which mobs the spawn multiplier affects").defineEnum("spawnFilter", SpawnFilter.ALL);
    public static final ForgeConfigSpec.IntValue MOB_CAP =
            B.comment("Safety cap: no extra spawns once this many mobs exist").defineInRange("mobCap", 400, 100, 1000);
    public static final ForgeConfigSpec.IntValue GROWTH =
            B.comment("Growth speed (sets randomTickSpeed). 1 = vanilla").defineInRange("growthSpeed", 1, 1, 100);
    public static final ForgeConfigSpec.IntValue LOOT_MULT =
            B.comment("Mob loot drop multiplier").defineInRange("lootMultiplier", 1, 1, 100);
    public static final ForgeConfigSpec.IntValue BLOCK_MULT =
            B.comment("Block drop multiplier").defineInRange("blockMultiplier", 1, 1, 100);
    public static final ForgeConfigSpec.IntValue CHEST_MULT =
            B.comment("Chest loot multiplier (new loot chests)").defineInRange("chestMultiplier", 1, 1, 100);
    public static final ForgeConfigSpec.IntValue DESPAWN_SECONDS =
            B.comment("Dropped items despawn after this many seconds (300 = vanilla)").defineInRange("itemDespawnSeconds", 300, 5, 300);
    public static final ForgeConfigSpec.IntValue BREED_COOLDOWN =
            B.comment("Seconds before animals can breed again after breeding (300 = vanilla, 0 = no cooldown)")
                    .defineInRange("breedingCooldownSeconds", 300, 0, 300);
    public static final ForgeConfigSpec.IntValue BABY_GROWTH =
            B.comment("Seconds for a baby animal to grow up (1200 = vanilla, 10 = very fast)")
                    .defineInRange("babyGrowthSeconds", 1200, 10, 1200);
    public static final ForgeConfigSpec.BooleanValue APOCALYPSE =
            B.comment("Apocalypse mode: only zombies, they spawn by day, new zombie loot").define("apocalypse", false);

    public static final ForgeConfigSpec.ConfigValue<String> SPAWN_TYPES =
            B.comment("Spawn types that get multiplied (comma separated): NATURAL, CHUNK_GENERATION, STRUCTURE, SPAWNER, EVENT, PATROL, ...")
                    .define("spawnTypes", "NATURAL,CHUNK_GENERATION,STRUCTURE");
    public static final ForgeConfigSpec.BooleanValue MULTIPLY_UNTRACKED =
            B.comment("Some modded mobs are put into the world WITHOUT the normal spawn event.",
                            "true = multiply those too (this makes modded enemies work). Babies and riders are skipped.")
                    .define("multiplyUntracked", true);
    public static final ForgeConfigSpec.ConfigValue<String> EXTRA_HOSTILE =
            B.comment("Extra mobs to treat as HOSTILE (comma separated), e.g. \"somemod:thing\" or \"somemod:*\"")
                    .define("extraHostile", "");
    public static final ForgeConfigSpec.ConfigValue<String> SPAWN_BLACKLIST =
            B.comment("Mobs that are never duplicated or removed (comma separated), e.g. \"somemod:big_boss\" or \"somemod:*\"")
                    .define("spawnBlacklist", "");

    public static final ForgeConfigSpec SPEC = B.build();

    private Config() {}

    private static int get(ForgeConfigSpec.IntValue v, int def) {
        try {
            return v.get();
        } catch (IllegalStateException e) {
            return def;
        }
    }

    public static int spawnMult() { return get(SPAWN_MULT, 1); }
    public static int mobCap() { return get(MOB_CAP, 400); }
    public static int growth() { return get(GROWTH, 1); }
    public static int lootMult() { return get(LOOT_MULT, 1); }
    public static int blockMult() { return get(BLOCK_MULT, 1); }
    public static int chestMult() { return get(CHEST_MULT, 1); }
    public static int breedCooldown() { return get(BREED_COOLDOWN, 300); }
    public static int babyGrowth() { return get(BABY_GROWTH, 1200); }
    public static int despawnSeconds() { return get(DESPAWN_SECONDS, 300); }

    private static String str(ForgeConfigSpec.ConfigValue<String> v, String def) {
        try {
            return v.get();
        } catch (IllegalStateException e) {
            return def;
        }
    }

    public static List<String> split(String s) {
        List<String> out = new ArrayList<>();
        for (String p : s.split(",")) {
            String t = p.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    public static List<String> blacklist() { return split(str(SPAWN_BLACKLIST, "")); }
    public static List<String> extraHostile() { return split(str(EXTRA_HOSTILE, "")); }

    public static List<String> spawnTypes() {
        List<String> l = new ArrayList<>();
        for (String s : split(str(SPAWN_TYPES, "NATURAL,CHUNK_GENERATION,STRUCTURE"))) l.add(s.toUpperCase(Locale.ROOT));
        return l;
    }

    public static boolean multiplyUntracked() {
        try {
            return MULTIPLY_UNTRACKED.get();
        } catch (IllegalStateException e) {
            return true;
        }
    }

    public static SpawnFilter filter() {
        try {
            return FILTER.get();
        } catch (IllegalStateException e) {
            return SpawnFilter.ALL;
        }
    }

    public static boolean apocalypse() {
        try {
            return APOCALYPSE.get();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static void set(ForgeConfigSpec.IntValue v, int value) {
        v.set(value);
        v.save();
    }

    public static void setFilter(SpawnFilter f) {
        FILTER.set(f);
        FILTER.save();
    }

    public static void setApocalypse(boolean on) {
        APOCALYPSE.set(on);
        APOCALYPSE.save();
    }
}
