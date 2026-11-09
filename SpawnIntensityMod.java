package com.spawnintensity;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(SpawnIntensityMod.MODID)
public class SpawnIntensityMod {
    public static final String MODID = "spawnintensity";

    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

    @SuppressWarnings("unused")
    private static final RegistryObject<Codec<? extends IGlobalLootModifier>> MULTIPLIER =
            LOOT_MODIFIERS.register("multiplier", MultiplierModifier.CODEC);

    public SpawnIntensityMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        LOOT_MODIFIERS.register(modBus);
        // Re-apply settings whenever config/spawnintensity-common.toml is edited while the game runs
        modBus.addListener((ModConfigEvent.Reloading ev) -> {
            if (ev.getConfig().getSpec() == Config.SPEC) Handlers.onConfigChanged();
        });
        MinecraftForge.EVENT_BUS.register(new Handlers());
    }
}
