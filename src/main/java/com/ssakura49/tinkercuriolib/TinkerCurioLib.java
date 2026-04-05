package com.ssakura49.tinkercuriolib;

import com.mojang.logging.LogUtils;
import com.ssakura49.tinkercuriolib.init.TCTagKey;
import com.ssakura49.tinkercuriolib.tools.module.CurioLevelModule;
import com.ssakura49.tinkercuriolib.tools.module.MultiCurioAttributeModule;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;

@Mod("tinkercuriolib")
public class TinkerCurioLib {
    public static final String MODID = "tinkercuriolib";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation location(String string) {
        return ResourceLocation.fromNamespaceAndPath("tinkercuriolib", string);
    }

    public TinkerCurioLib(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        TCTagKey.init();
    }

    @SubscribeEvent
    public void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            ModifierModule.LOADER.register(location("multi_curio_attribute"), MultiCurioAttributeModule.LOADER);
            ModifierModule.LOADER.register(location("curio_level"), CurioLevelModule.LOADER);
        }

    }
}
