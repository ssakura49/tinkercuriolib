package com.ssakura49.tinkercuriolib.hook.combat;

import java.util.Collection;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioDamageTargetPreModifierHook {
    default void onDamageTargetPre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity attacker, LivingEntity target) {
    }

    public static record AllMerger(Collection<CurioDamageTargetPreModifierHook> modules) implements CurioDamageTargetPreModifierHook {
        public void onDamageTargetPre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity attacker, LivingEntity target) {
            for(CurioDamageTargetPreModifierHook module : this.modules) {
                module.onDamageTargetPre(curio, entry, event, attacker, target);
            }

        }
    }
}
