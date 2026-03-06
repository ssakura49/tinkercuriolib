package com.ssakura49.tinkercuriolib.hook.mining;

import java.util.Collection;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioBreakSpeedModifierHook {
    default void onCurioBreakSpeed(IToolStackView curio, ModifierEntry entry, PlayerEvent.BreakSpeed event, Player player) {
    }

    public static record AllMerger(Collection<CurioBreakSpeedModifierHook> modules) implements CurioBreakSpeedModifierHook {
        public void onCurioBreakSpeed(IToolStackView curio, ModifierEntry entry, PlayerEvent.BreakSpeed event, Player player) {
            for(CurioBreakSpeedModifierHook module : this.modules) {
                module.onCurioBreakSpeed(curio, entry, event, player);
            }

        }
    }
}
