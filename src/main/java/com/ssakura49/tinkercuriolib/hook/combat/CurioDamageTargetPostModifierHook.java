package com.ssakura49.tinkercuriolib.hook.combat;

import java.util.Collection;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioDamageTargetPostModifierHook {
    default void onCurioDamageTargetPost(IToolStackView curio, ModifierEntry entry, LivingDamageEvent event, LivingEntity attacker, LivingEntity target) {
    }

    public static record AllMerger(Collection<CurioDamageTargetPostModifierHook> modules) implements CurioDamageTargetPostModifierHook {
        public void onCurioDamageTargetPost(IToolStackView curio, ModifierEntry entry, LivingDamageEvent event, LivingEntity attacker, LivingEntity target) {
            for(CurioDamageTargetPostModifierHook module : this.modules) {
                module.onCurioDamageTargetPost(curio, entry, event, attacker, target);
            }

        }
    }
}