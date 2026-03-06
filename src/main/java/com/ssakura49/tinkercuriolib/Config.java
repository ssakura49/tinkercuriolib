package com.ssakura49.tinkercuriolib;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber(
        modid = "tinkercuriolib",
        bus = Bus.MOD
)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static final ForgeConfigSpec SPEC;

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
    }

    static {
        SPEC = BUILDER.build();
    }
}
