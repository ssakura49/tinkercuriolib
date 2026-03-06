package com.ssakura49.tinkercuriolib.hook.armor;

import java.util.Collection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioTakeDamagePreModifierHook {
    default void onCurioTakeDamagePre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity entity, DamageSource source) {
    }

    public static record AllMerger(Collection<CurioTakeDamagePreModifierHook> modules) implements CurioTakeDamagePreModifierHook {
        public void onCurioTakeDamagePre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity entity, DamageSource source) {
            for(CurioTakeDamagePreModifierHook module : this.modules) {
                module.onCurioTakeDamagePre(curio, entry, event, entity, source);
            }

        }
    }
}
