package com.ssakura49.tinkercuriolib.hook.combat;

import java.util.Collection;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioKillTargetModifierHook {
    default void onCurioToKillTarget(IToolStackView curio, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
    }

    public static record AllMerger(Collection<CurioKillTargetModifierHook> modules) implements CurioKillTargetModifierHook {
        public void onCurioToKillTarget(IToolStackView curio, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
            for(CurioKillTargetModifierHook module : this.modules) {
                module.onCurioToKillTarget(curio, entry, event, attacker, target);
            }

        }
    }
}
