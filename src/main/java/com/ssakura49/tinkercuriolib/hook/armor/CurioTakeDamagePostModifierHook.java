package com.ssakura49.tinkercuriolib.hook.armor;

import java.util.Collection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioTakeDamagePostModifierHook {
    default void onCurioTakeDamagePost(IToolStackView curio, ModifierEntry entry, LivingDamageEvent event, LivingEntity entity, DamageSource source) {
    }

    public static record AllMerger(Collection<CurioTakeDamagePostModifierHook> modules) implements CurioTakeDamagePostModifierHook {
        public void onCurioTakeDamagePost(IToolStackView curio, ModifierEntry entry, LivingDamageEvent event, LivingEntity entity, DamageSource source) {
            for(CurioTakeDamagePostModifierHook module : this.modules) {
                module.onCurioTakeDamagePost(curio, entry, event, entity, source);
            }

        }
    }
}
