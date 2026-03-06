package com.ssakura49.tinkercuriolib.hook;

import com.ssakura49.tinkercuriolib.TinkerCurioLib;
import com.ssakura49.tinkercuriolib.hook.armor.CurioEquipmentChangeModifierHook;
import com.ssakura49.tinkercuriolib.hook.armor.CurioTakeDamagePostModifierHook;
import com.ssakura49.tinkercuriolib.hook.armor.CurioTakeDamagePreModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioAttributeModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioDropRuleModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioFortuneModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioGetToolDamageModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioTakeHealModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioDamageCalculateModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioDamageTargetPostModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioDamageTargetPreModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioKillTargetModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioLootingModifierHook;
import com.ssakura49.tinkercuriolib.hook.interation.CurioInventoryTickModifierHook;
import com.ssakura49.tinkercuriolib.hook.mining.CurioBreakSpeedModifierHook;
import com.ssakura49.tinkercuriolib.hook.ranged.CurioProjectileDamageModifierHook;
import com.ssakura49.tinkercuriolib.hook.ranged.CurioProjectileHitModifierHook;
import com.ssakura49.tinkercuriolib.hook.ranged.CurioProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

public class TCLibHooks {
    public static final ModuleHook<CurioFortuneModifierHook> CURIO_FORTUNE = ModifierHooks.register(TinkerCurioLib.location("curio_fortune"), CurioFortuneModifierHook.class, CurioFortuneModifierHook.ComposeMerger::new, (tool, modifier, slotContext,lootContext,stack, fortune) -> fortune);
    public static final ModuleHook<CurioEquipmentChangeModifierHook> CURIO_EQUIPMENT_CHANGE = ModifierHooks.register(TinkerCurioLib.location("curio_equipment_change"), CurioEquipmentChangeModifierHook.class, CurioEquipmentChangeModifierHook.AllMerger::new, new CurioEquipmentChangeModifierHook() {
    });
    public static final ModuleHook<CurioInventoryTickModifierHook> CURIO_TICK = ModifierHooks.register(TinkerCurioLib.location("curio_tick"), CurioInventoryTickModifierHook.class, CurioInventoryTickModifierHook.AllMerger::new, new CurioInventoryTickModifierHook() {
    });
    public static final ModuleHook<CurioLootingModifierHook> CURIO_LOOTING = ModifierHooks.register(TinkerCurioLib.location("curio_looting"), CurioLootingModifierHook.class, CurioLootingModifierHook.ComposeMerger::new, (tool, modifier, slotContext,source,livingEntity,stack, looting) -> looting);
    public static final ModuleHook<CurioAttributeModifierHook> CURIO_ATTRIBUTE = ModifierHooks.register(TinkerCurioLib.location("curio_attribute"), CurioAttributeModifierHook.class, CurioAttributeModifierHook.AllMerger::new, new CurioAttributeModifierHook() {
    });
    public static final ModuleHook<CurioGetToolDamageModifierHook> CURIO_TOOL_DAMAGE = ModifierHooks.register(TinkerCurioLib.location("curio_tool_damage"), CurioGetToolDamageModifierHook.class, CurioGetToolDamageModifierHook.AllMerger::new, new CurioGetToolDamageModifierHook() {
    });
    public static final ModuleHook<CurioTakeHealModifierHook> CURIO_TAKE_HEAL = ModifierHooks.register(TinkerCurioLib.location("curio_take_heal"), CurioTakeHealModifierHook.class, CurioTakeHealModifierHook.AllMerger::new, new CurioTakeHealModifierHook() {
    });
    public static final ModuleHook<CurioBreakSpeedModifierHook> CURIO_BREAK_SPEED = ModifierHooks.register(TinkerCurioLib.location("curio_break_speed"), CurioBreakSpeedModifierHook.class, CurioBreakSpeedModifierHook.AllMerger::new, new CurioBreakSpeedModifierHook() {
    });
    public static final ModuleHook<CurioTakeDamagePreModifierHook> CURIO_TAKE_DAMAGE_PRE = ModifierHooks.register(TinkerCurioLib.location("curio_take_damage_pre"), CurioTakeDamagePreModifierHook.class, CurioTakeDamagePreModifierHook.AllMerger::new, new CurioTakeDamagePreModifierHook() {
    });
    public static final ModuleHook<CurioTakeDamagePostModifierHook> CURIO_TAKE_DAMAGE_POST = ModifierHooks.register(TinkerCurioLib.location("curio_take_damage_post"), CurioTakeDamagePostModifierHook.class, CurioTakeDamagePostModifierHook.AllMerger::new, new CurioTakeDamagePostModifierHook() {
    });
    public static final ModuleHook<CurioProjectileLaunchModifierHook> CURIO_PROJECTILE_LAUNCH = ModifierHooks.register(TinkerCurioLib.location("curio_projectile_launch"), CurioProjectileLaunchModifierHook.class, CurioProjectileLaunchModifierHook.AllMerger::new, new CurioProjectileLaunchModifierHook() {
    });
    public static final ModuleHook<CurioProjectileHitModifierHook> CURIO_PROJECTILE_HIT = ModifierHooks.register(TinkerCurioLib.location("curio_projectile_hit"), CurioProjectileHitModifierHook.class, CurioProjectileHitModifierHook.AllMerger::new, new CurioProjectileHitModifierHook() {
    });
    public static final ModuleHook<CurioDamageTargetPreModifierHook> CURIO_DAMAGE_TARGET_PRE = ModifierHooks.register(TinkerCurioLib.location("curio_damage_target_pre"), CurioDamageTargetPreModifierHook.class, CurioDamageTargetPreModifierHook.AllMerger::new, new CurioDamageTargetPreModifierHook() {
    });
    public static final ModuleHook<CurioDamageTargetPostModifierHook> CURIO_DAMAGE_TARGET_POST = ModifierHooks.register(TinkerCurioLib.location("curio_damage_target_post"), CurioDamageTargetPostModifierHook.class, CurioDamageTargetPostModifierHook.AllMerger::new, new CurioDamageTargetPostModifierHook() {
    });
    public static final ModuleHook<CurioDamageCalculateModifierHook> CURIO_CALCULATE_DAMAGE = ModifierHooks.register(TinkerCurioLib.location("curio_calculate_damage"), CurioDamageCalculateModifierHook.class, CurioDamageCalculateModifierHook.AllMerger::new, new CurioDamageCalculateModifierHook() {
    });
    public static final ModuleHook<CurioKillTargetModifierHook> CURIO_KILL_TARGET = ModifierHooks.register(TinkerCurioLib.location("curio_kill_target"), CurioKillTargetModifierHook.class, CurioKillTargetModifierHook.AllMerger::new, new CurioKillTargetModifierHook() {
    });
    public static final ModuleHook<CurioDropRuleModifierHook> CURIO_DROP_RULE = ModifierHooks.register(TinkerCurioLib.location("curio_drop_rule"), CurioDropRuleModifierHook.class, CurioDropRuleModifierHook.AllMerger::new, new CurioDropRuleModifierHook() {
    });
    public static final ModuleHook<CurioProjectileDamageModifierHook> CURIO_PROJECTILE_DAMAGE = ModifierHooks.register(TinkerCurioLib.location("curio_projectile_damage"), CurioProjectileDamageModifierHook.class, CurioProjectileDamageModifierHook.AllMerger::new, (CurioProjectileDamageModifierHook)(modDataNBT, modifierEntry, modifierEntries, projectile, arrow, living, entity, baseDamage, damage) -> damage);
}
