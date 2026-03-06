package com.ssakura49.tinkercuriolib.hook.combat;

import java.util.Collection;

import com.ssakura49.tinkercuriolib.hook.TCLibHooks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

public interface CurioLootingModifierHook {
    int onCurioUpdateLooting(IToolStackView tool, ModifierEntry modifier,SlotContext slotContext, DamageSource source, LivingEntity target,ItemStack stack, int looting);

    static int getCurioLooting(IToolStackView tool,SlotContext slotContext, DamageSource source, LivingEntity target,ItemStack stack, int looting) {
        if (!tool.isBroken()) {
            for (ModifierEntry entry : tool.getModifierList()) {
                looting = entry.getHook(TCLibHooks.CURIO_LOOTING).onCurioUpdateLooting(tool, entry,slotContext,source,target,stack, looting);
            }
        }
        return looting;
    }


    /** Constructor for a merger that sums all children */
    record ComposeMerger(Collection<CurioLootingModifierHook> modules) implements CurioLootingModifierHook {
        @Override
        public int onCurioUpdateLooting(IToolStackView tool, ModifierEntry modifier,SlotContext slotContext, DamageSource source, LivingEntity target,ItemStack stack,int looting) {
            for (CurioLootingModifierHook module : modules) {
                looting = module.onCurioUpdateLooting(tool,modifier,slotContext,source,target,stack, looting);
            }
            return looting;
        }
    }
}
