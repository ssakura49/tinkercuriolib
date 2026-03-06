package com.ssakura49.tinkercuriolib.hook.ranged;

import com.ssakura49.tinkercuriolib.content.ProjectileImpactContent;
import java.util.Collection;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CurioProjectileHitModifierHook {
    default void onCurioProjectileHit(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, ProjectileImpactContent content, HitResult hit) {
    }

    public static record AllMerger(Collection<CurioProjectileHitModifierHook> modules) implements CurioProjectileHitModifierHook {
        public void onCurioProjectileHit(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, ProjectileImpactContent content, HitResult hit) {
            for(CurioProjectileHitModifierHook module : this.modules) {
                module.onCurioProjectileHit(curio, entry, shooter, content, hit);
            }

        }
    }
}
