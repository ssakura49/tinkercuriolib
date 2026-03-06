package com.ssakura49.tinkercuriolib.hook.ranged;

import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public interface CurioProjectileDamageModifierHook {
    float getProjectileDamage(ModDataNBT var1, ModifierEntry var2, ModifierNBT var3, @NotNull Projectile var4, @Nullable AbstractArrow var5, @Nullable LivingEntity var6, @NotNull Entity var7, float var8, float var9);

    public static record AllMerger(Collection<CurioProjectileDamageModifierHook> modules) implements CurioProjectileDamageModifierHook {
        public float getProjectileDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, @NotNull Projectile projectile, @Nullable AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage) {
            for(CurioProjectileDamageModifierHook module : this.modules) {
                damage = module.getProjectileDamage(persistentData, entry, modifiers, projectile, arrow, attacker, target, baseDamage, damage);
            }

            return damage;
        }
    }
}
