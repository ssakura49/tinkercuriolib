package com.ssakura49.tinkercuriolib.hook.ranged;

import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public interface CurioProjectileLaunchModifierHook {
    default void onCurioProjectileLaunch(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData) {
    }

    public static record AllMerger(Collection<CurioProjectileLaunchModifierHook> modules) implements CurioProjectileLaunchModifierHook {
        public void onCurioProjectileShoot(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData) {
            for(CurioProjectileLaunchModifierHook module : this.modules) {
                module.onCurioProjectileLaunch(curio, entry, shooter, projectile, arrow, persistentData);
            }

        }
    }
}