package com.ssakura49.tinkercuriolib.tools.module;

import com.ssakura49.tinkercuriolib.hook.TCLibHooks;
import com.ssakura49.tinkercuriolib.hook.armor.CurioEquipmentChangeModifierHook;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.capability.TinkerDataKeys;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;

public record CurioLevelModule(TinkerDataCapability.TinkerDataKey<Integer> key, boolean allowBroken, @Nullable TagKey<Item> curioTag) implements HookProvider, CurioEquipmentChangeModifierHook, ModifierModule {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.defaultHooks(TCLibHooks.CURIO_EQUIPMENT_CHANGE);;
    public static final RecordLoadable<CurioLevelModule> LOADER = RecordLoadable.create(
            TinkerDataKeys.INTEGER_REGISTRY.requiredField("key", CurioLevelModule::key),
            BooleanLoadable.INSTANCE.defaultField("allow_broken", false, CurioLevelModule::allowBroken),
            Loadables.ITEM_TAG.nullableField("curio_tag", CurioLevelModule::curioTag),
            CurioLevelModule::new
    );;

    @Override
    public @NotNull RecordLoadable<CurioLevelModule> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public void onCurioEquip(IToolStackView curio, ModifierEntry entry,
                             SlotContext context, LivingEntity entity,
                             ItemStack prevStack, ItemStack stack) {

        addLevelsIfCurio(curio, entity, this.key, entry.intEffectiveLevel(), this.allowBroken, this.curioTag);
    }
    @Override
    public void onCurioUnequip(IToolStackView curio, ModifierEntry entry,
                               SlotContext context, LivingEntity entity,
                               ItemStack newStack, ItemStack stack) {
        addLevelsIfCurio(curio, entity, this.key, -entry.intEffectiveLevel(), this.allowBroken, this.curioTag);
    }

    public static void addLevels(LivingEntity entity,
                                 TinkerDataCapability.TinkerDataKey<Integer> key,
                                 int amount) {
        entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(data -> {
            int total = data.get(key, 0) + amount;

            if (total <= 0) {
                data.remove(key);
            } else {
                data.put(key, total);
            }
        });
    }

    public static boolean validCurio(IToolStackView curio, @Nullable TagKey<Item> tag) {
        return tag == null || curio.hasTag(tag);
    }

    public static void addLevelsIfCurio(IToolStackView curio, LivingEntity entity, TinkerDataCapability.TinkerDataKey<Integer> key, int amount, boolean allowBroken, @Nullable TagKey<Item> tag) {
        if (validCurio(curio, tag) && (allowBroken || !curio.isBroken())) {
            addLevels(entity, key, amount);
        }
    }

    public static int getLevel(LivingEntity living, TinkerDataCapability.TinkerDataKey<Integer> key) {
        return living.getCapability(TinkerDataCapability.CAPABILITY)
                .map(data -> data.get(key, 0))
                .orElse(0);
    }

    public TinkerDataCapability.TinkerDataKey<Integer> key() {
        return key;
    }

    public boolean allowBroken() {
        return allowBroken;
    }

    @Nullable
    public TagKey<Item> curioTag() {
        return curioTag;
    }
}