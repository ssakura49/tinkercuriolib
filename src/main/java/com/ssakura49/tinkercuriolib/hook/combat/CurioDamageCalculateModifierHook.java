package com.ssakura49.tinkercuriolib.hook.combat;

import com.ssakura49.tinkercuriolib.event.LivingDamageCalculationEvent;
import java.util.Collection;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioDamageCalculateModifierHook {
    default void onCurioCalculateDamage(IToolStackView curio, ModifierEntry entry, LivingDamageCalculationEvent event, LivingEntity attacker, LivingEntity target) {
    }

    public static record AllMerger(Collection<CurioDamageCalculateModifierHook> modules) implements CurioDamageCalculateModifierHook {
        public void onCurioCalculateDamage(IToolStackView curio, ModifierEntry entry, LivingDamageCalculationEvent event, LivingEntity attacker, LivingEntity target) {
            for(CurioDamageCalculateModifierHook module : this.modules) {
                module.onCurioCalculateDamage(curio, entry, event, attacker, target);
            }

        }
    }
}
