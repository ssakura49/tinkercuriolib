package com.ssakura49.tinkercuriolib.hook.behavior;

import java.util.Collection;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioTakeHealModifierHook {
    default void onCurioTakeHeal(IToolStackView curio, ModifierEntry entry, LivingHealEvent event, LivingEntity entity) {
    }

    public static record AllMerger(Collection<CurioTakeHealModifierHook> modules) implements CurioTakeHealModifierHook {
        public void onCurioTakeHeal(IToolStackView curio, ModifierEntry entry, LivingHealEvent event, LivingEntity entity) {
            for(CurioTakeHealModifierHook module : this.modules) {
                module.onCurioTakeHeal(curio, entry, event, entity);
            }

        }
    }
}
