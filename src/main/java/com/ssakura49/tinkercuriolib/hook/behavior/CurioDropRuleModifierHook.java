package com.ssakura49.tinkercuriolib.hook.behavior;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;

public interface CurioDropRuleModifierHook {
    default ICurio.DropRule getCurioDropRule(IToolStackView curio, ModifierEntry entry, DamageSource source, int looting, boolean recentlyHit, ItemStack stack) {
        return DropRule.DEFAULT;
    }

    public static record AllMerger(Collection<CurioDropRuleModifierHook> modules) implements CurioDropRuleModifierHook {
        public ICurio.DropRule getCurioDropRule(IToolStackView curio, ModifierEntry entry, DamageSource source, int looting, boolean recent, ItemStack stack) {
            Iterator<CurioDropRuleModifierHook> var7 = this.modules.iterator();
            if (var7.hasNext()) {
                CurioDropRuleModifierHook module = var7.next();
                return module.getCurioDropRule(curio, entry, source, looting, recent, stack);
            } else {
                return DropRule.DEFAULT;
            }
        }
    }
}