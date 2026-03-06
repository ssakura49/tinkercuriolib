package com.ssakura49.tinkercuriolib.handler;

import com.ssakura49.tinkercuriolib.content.ProjectileImpactContent;
import com.ssakura49.tinkercuriolib.event.ItemStackDamageEvent;
import com.ssakura49.tinkercuriolib.event.LivingDamageCalculationEvent;
import com.ssakura49.tinkercuriolib.hook.TCLibHooks;
import com.ssakura49.tinkercuriolib.hook.armor.CurioTakeDamagePostModifierHook;
import com.ssakura49.tinkercuriolib.hook.armor.CurioTakeDamagePreModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioGetToolDamageModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioTakeHealModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioDamageCalculateModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioDamageTargetPostModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioDamageTargetPreModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioKillTargetModifierHook;
import com.ssakura49.tinkercuriolib.hook.mining.CurioBreakSpeedModifierHook;
import com.ssakura49.tinkercuriolib.hook.ranged.CurioProjectileDamageModifierHook;
import com.ssakura49.tinkercuriolib.hook.ranged.CurioProjectileHitModifierHook;
import com.ssakura49.tinkercuriolib.hook.ranged.CurioProjectileLaunchModifierHook;
import com.ssakura49.tinkercuriolib.utils.TCToolUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@EventBusSubscriber(
        modid = "tinkercuriolib",
        bus = Bus.FORGE
)
public class ForgeEvent {
    @SubscribeEvent
    public static void onDamageStack(ItemStackDamageEvent event) {
        LivingEntity entity = event.getEntity();
        TCToolUtil.getStacks(entity).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((entry) -> {
                CurioGetToolDamageModifierHook hook = (CurioGetToolDamageModifierHook)entry.getHook(TCLibHooks.CURIO_TOOL_DAMAGE);
                hook.onCurioGetToolDamage(curio, entry, entity, event);
            });
        });
    }

    private static void onHurtEntity(LivingHurtEvent event) {
        Entity var2 = event.getSource().getEntity();
        if (var2 instanceof LivingEntity attacker) {
            TCToolUtil.getStacks(attacker).forEach((stack) -> {
                ToolStack curio = ToolStack.from(stack);
                curio.getModifierList().forEach((e) -> {
                    CurioDamageTargetPreModifierHook hook = (CurioDamageTargetPreModifierHook)e.getHook(TCLibHooks.CURIO_DAMAGE_TARGET_PRE);
                    hook.onDamageTargetPre(curio, e, event, attacker, event.getEntity());
                });
            });
        }

    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        onHurtEntity(event);
        TCToolUtil.getStacks(entity).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioTakeDamagePreModifierHook hook = (CurioTakeDamagePreModifierHook)e.getHook(TCLibHooks.CURIO_TAKE_DAMAGE_PRE);
                hook.onCurioTakeDamagePre(curio, e, event, entity, event.getSource());
            });
        });
    }

    private static void onDamageEntity(LivingDamageEvent event) {
        Entity var2 = event.getSource().getEntity();
        if (var2 instanceof LivingEntity attacker) {
            TCToolUtil.getStacks(attacker).forEach((stack) -> {
                ToolStack curio = ToolStack.from(stack);
                curio.getModifierList().forEach((e) -> {
                    CurioDamageTargetPostModifierHook hook = (CurioDamageTargetPostModifierHook)e.getHook(TCLibHooks.CURIO_DAMAGE_TARGET_POST);
                    hook.onCurioDamageTargetPost(curio, e, event, attacker, event.getEntity());
                });
            });
        }

    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        onDamageEntity(event);
        TCToolUtil.getStacks(entity).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioTakeDamagePostModifierHook hook = (CurioTakeDamagePostModifierHook)e.getHook(TCLibHooks.CURIO_TAKE_DAMAGE_POST);
                hook.onCurioTakeDamagePost(curio, e, event, entity, event.getSource());
            });
        });
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity var2 = event.getSource().getEntity();
        if (var2 instanceof LivingEntity attacker) {
            TCToolUtil.getStacks(attacker).forEach((stack) -> {
                ToolStack curio = ToolStack.from(stack);
                curio.getModifierList().forEach((e) -> {
                    CurioKillTargetModifierHook hook = (CurioKillTargetModifierHook)e.getHook(TCLibHooks.CURIO_KILL_TARGET);
                    hook.onCurioToKillTarget(curio, e, event, attacker, event.getEntity());
                });
            });
        }

    }

    @SubscribeEvent
    public static void onLivingCalculate(LivingDamageCalculationEvent event) {
        LivingEntity attacker = event.getAttacker();
        if (attacker != null) {
            TCToolUtil.getStacks(attacker).forEach((stack) -> {
                ToolStack curio = ToolStack.from(stack);
                curio.getModifierList().forEach((e) -> {
                    CurioDamageCalculateModifierHook hook = (CurioDamageCalculateModifierHook)e.getHook(TCLibHooks.CURIO_CALCULATE_DAMAGE);
                    hook.onCurioCalculateDamage(curio, e, event, attacker, event.getTarget());
                });
            });
        }

    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        TCToolUtil.getStacks(entity).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioTakeHealModifierHook hook = (CurioTakeHealModifierHook)e.getHook(TCLibHooks.CURIO_TAKE_HEAL);
                hook.onCurioTakeHeal(curio, e, event, entity);
            });
        });
    }

    @SubscribeEvent
    public static void onProjectileLaunch(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Projectile projectile) {
            Entity shooter = projectile.getOwner();
            if (shooter instanceof LivingEntity livingEntity) {
                TCToolUtil.getStacks(livingEntity).forEach((stack) -> {
                    ToolStack curio = ToolStack.from(stack);
                    curio.getModifierList().forEach((e) -> {
                        CurioProjectileLaunchModifierHook hook = (CurioProjectileLaunchModifierHook)e.getHook(TCLibHooks.CURIO_PROJECTILE_LAUNCH);
                        AbstractArrow var10005;
                        if (projectile instanceof AbstractArrow arrow) {
                            var10005 = arrow;
                        } else {
                            var10005 = null;
                        }

                        hook.onCurioProjectileLaunch(curio, e, livingEntity, projectile, var10005, PersistentDataCapability.getOrWarn(projectile));
                    });
                });
            }
        }

    }

    @SubscribeEvent
    public static void onProjectileHit(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile != null) {
            Entity shooter = projectile.getOwner();
            if (shooter instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity)shooter;
                TCToolUtil.getStacks(entity).forEach((stack) -> {
                    ToolStack curio = ToolStack.from(stack);
                    curio.getModifierList().forEach((entry) -> {
                        CurioProjectileHitModifierHook hook = (CurioProjectileHitModifierHook)entry.getHook(TCLibHooks.CURIO_PROJECTILE_HIT);
                        hook.onCurioProjectileHit(curio, entry, entity, new ProjectileImpactContent(event, projectile), event.getRayTraceResult());
                    });
                });
            }
        }

    }

    @SubscribeEvent(
            priority = EventPriority.HIGH
    )
    public static void onProjectileDamage(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity entity = source.getDirectEntity();
        if (!event.isCanceled() && entity instanceof Projectile projectile) {
            Entity shooter = projectile.getOwner();
            if (shooter instanceof LivingEntity livingEntity) {
                entity.getCapability(EntityModifierCapability.CAPABILITY).ifPresent((cap) -> {
                    ModDataNBT nbt = PersistentDataCapability.getOrWarn(entity);
                    ModifierNBT modifiers = cap.getModifiers();
                    float baseDamage = event.getAmount();
                    float damage = baseDamage;

                    for(ItemStack stack : TCToolUtil.getStacks(livingEntity)) {
                        ToolStack curio = ToolStack.from(stack);

                        for(ModifierEntry entry : curio.getModifierList()) {
                            CurioProjectileDamageModifierHook hook = (CurioProjectileDamageModifierHook)entry.getHook(TCLibHooks.CURIO_PROJECTILE_DAMAGE);
                            AbstractArrow var10005;
                            if (projectile instanceof AbstractArrow arrow) {
                                var10005 = arrow;
                            } else {
                                var10005 = null;
                            }

                            damage = hook.getProjectileDamage(nbt, entry, modifiers, projectile, var10005, livingEntity, event.getEntity(), baseDamage, damage);
                        }
                    }

                    event.setAmount(damage);
                });
            }
        }

    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        TCToolUtil.getStacks(event.getEntity()).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioBreakSpeedModifierHook hook = (CurioBreakSpeedModifierHook)e.getHook(TCLibHooks.CURIO_BREAK_SPEED);
                hook.onCurioBreakSpeed(curio, e, event, event.getEntity());
            });
        });
    }
}
