package com.ssakura49.tinkercuriolib.content;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class ProjectileImpactContent {
    private final ProjectileImpactEvent event;
    private final Projectile projectile;

    public ProjectileImpactContent(ProjectileImpactEvent event, Projectile projectile) {
        this.event = event;
        this.projectile = projectile;
    }

    public Entity getOwner() {
        return this.projectile.getOwner();
    }

    public Projectile getProjectile() {
        return this.event.getProjectile();
    }

    public Entity getTarget() {
        return this.event.getEntity();
    }

    public HitResult getResult() {
        return this.event.getRayTraceResult();
    }

    public ModDataNBT getPersistent() {
        return PersistentDataCapability.getOrWarn(this.projectile);
    }

    @Nullable
    public ModifierNBT getModifiers() {
        return EntityModifierCapability.getOrEmpty(this.projectile);
    }

    @Nullable
    public EntityHitResult getEntityResult() {
        return this.getResult().getType().equals(Type.ENTITY) ? (EntityHitResult)this.getResult() : null;
    }

    @Nullable
    public BlockHitResult getBlockResult() {
        return this.getResult().getType().equals(Type.BLOCK) ? (BlockHitResult)this.getResult() : null;
    }
}
