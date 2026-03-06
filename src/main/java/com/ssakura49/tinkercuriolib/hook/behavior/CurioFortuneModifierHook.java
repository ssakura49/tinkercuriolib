package com.ssakura49.tinkercuriolib.hook.behavior;

import java.util.Collection;

import com.ssakura49.tinkercuriolib.hook.TCLibHooks;
import com.ssakura49.tinkercuriolib.hook.combat.CurioLootingModifierHook;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

public interface CurioFortuneModifierHook {
    int onCurioUpdateFortune(IToolStackView tool, ModifierEntry modifier, SlotContext slotContext, LootContext lootContext, ItemStack stack, int fortune);

    static int getCurioFortune(IToolStackView tool,SlotContext slotContext, LootContext lootContext,ItemStack stack, int fortune) {
        if (!tool.isBroken()) {
            for (ModifierEntry entry : tool.getModifierList()) {
                fortune = entry.getHook(TCLibHooks.CURIO_FORTUNE).onCurioUpdateFortune(tool, entry,slotContext,lootContext,stack, fortune);
            }
        }
        return fortune;
    }


    /** Constructor for a merger that sums all children */
    record ComposeMerger(Collection<CurioFortuneModifierHook> modules) implements CurioFortuneModifierHook {
        @Override
        public int onCurioUpdateFortune(IToolStackView tool, ModifierEntry modifier,SlotContext slotContext, LootContext lootContext,ItemStack stack,int fortune) {
            for (CurioFortuneModifierHook module : modules) {
                fortune = module.onCurioUpdateFortune(tool,modifier,slotContext,lootContext,stack, fortune);
            }
            return fortune;
        }
    }
}