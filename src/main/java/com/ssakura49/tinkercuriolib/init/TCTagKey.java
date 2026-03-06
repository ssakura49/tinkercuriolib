package com.ssakura49.tinkercuriolib.init;

import com.ssakura49.tinkercuriolib.TinkerCurioLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class TCTagKey {
    static boolean tagsLoaded = false;

    public static void init() {
        TCTagKey.Items.init();
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TagsUpdatedEvent.class, (event) -> tagsLoaded = true);
    }

    public static boolean isTagsLoaded() {
        return tagsLoaded;
    }

    public static class Items {
        public static final TagKey<Item> TINKER_CURIO = local("modifiable/curio");

        private static void init() {
        }

        private static TagKey<Item> local(String name) {
            return TagKey.create(Registries.ITEM, TinkerCurioLib.location(name));
        }
    }
}
