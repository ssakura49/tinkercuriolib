package com.ssakura49.tinkercuriolib.hook.behavior;

import com.ssakura49.tinkercuriolib.event.ItemStackDamageEvent;
import java.util.Collection;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioGetToolDamageModifierHook {
    default void onCurioGetToolDamage(IToolStackView curio, ModifierEntry entry, LivingEntity entity, ItemStackDamageEvent event) {
    }

    public static record AllMerger(Collection<CurioGetToolDamageModifierHook> modules) implements CurioGetToolDamageModifierHook {
        public void onCurioGetToolDamage(IToolStackView curio, ModifierEntry entry, LivingEntity entity, ItemStackDamageEvent event) {
            for(CurioGetToolDamageModifierHook module : this.modules) {
                module.onCurioGetToolDamage(curio, entry, entity, event);
            }

        }
    }
}
