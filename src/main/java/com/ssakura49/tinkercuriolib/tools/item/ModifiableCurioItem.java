package com.ssakura49.tinkercuriolib.tools.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.ssakura49.tinkercuriolib.hook.TCLibHooks;
import com.ssakura49.tinkercuriolib.hook.armor.CurioEquipmentChangeModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioAttributeModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioDropRuleModifierHook;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioFortuneModifierHook;
import com.ssakura49.tinkercuriolib.hook.combat.CurioLootingModifierHook;
import com.ssakura49.tinkercuriolib.hook.interation.CurioInventoryTickModifierHook;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.EnchantmentModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.DurabilityDisplayModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.BlockInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.EntityInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.SlotStackModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.UsingToolModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.tools.IndestructibleItemEntity;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.display.ToolNameHook;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.definition.module.mining.MiningSpeedToolHook;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.ToolHarvestLogic;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerToolActions;
import slimeknights.tconstruct.tools.data.ModifierIds;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;

public class ModifiableCurioItem extends Item implements IModifiableDisplay, ICurioItem {
    private final ToolDefinition toolDefinition;
    private final int maxStackSize;
    private ItemStack toolForRendering;

    public ModifiableCurioItem(Item.Properties properties, ToolDefinition toolDefinition, int maxStackSize) {
        super(properties);
        this.toolDefinition = toolDefinition;
        this.maxStackSize = maxStackSize;
    }

    public ModifiableCurioItem(Item.Properties properties, ToolDefinition toolDefinition) {
        this(properties, toolDefinition, 1);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);

        for(ModifierEntry entry : tool.getModifierList()) {
            CurioInventoryTickModifierHook hook = (CurioInventoryTickModifierHook)entry.getHook(TCLibHooks.CURIO_TICK);
            hook.onCurioTick(tool, entry, slotContext, slotContext.entity(), stack);
        }

    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);

        for(ModifierEntry entry : tool.getModifierList()) {
            CurioEquipmentChangeModifierHook hook = (CurioEquipmentChangeModifierHook)entry.getHook(TCLibHooks.CURIO_EQUIPMENT_CHANGE);
            hook.onCurioEquip(tool, entry, slotContext, slotContext.entity(), prevStack, stack);
        }

    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);

        for(ModifierEntry entry : tool.getModifierList()) {
            CurioEquipmentChangeModifierHook hook = (CurioEquipmentChangeModifierHook)entry.getHook(TCLibHooks.CURIO_EQUIPMENT_CHANGE);
            hook.onCurioUnequip(tool, entry, slotContext, slotContext.entity(), newStack, stack);
        }

    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        int fortune = ICurioItem.super.getFortuneLevel(slotContext, lootContext, stack);
        ToolStack tool = ToolStack.from(stack);

        for(ModifierEntry entry : tool.getModifierList()) {
            CurioFortuneModifierHook hook = (CurioFortuneModifierHook)entry.getHook(TCLibHooks.CURIO_FORTUNE);
            fortune = hook.onCurioUpdateFortune(tool, entry, slotContext, lootContext, stack, fortune);
        }

        return fortune;
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        int looting = ICurioItem.super.getLootingLevel(slotContext, source, target, baseLooting, stack);
        ToolStack tool = ToolStack.from(stack);

        for(ModifierEntry entry : tool.getModifierList()) {
            CurioLootingModifierHook hook = (CurioLootingModifierHook)entry.getHook(TCLibHooks.CURIO_LOOTING);
            looting = hook.onCurioUpdateLooting(tool, entry, slotContext, source, target, stack, looting);
        }

        return looting;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return ICurioItem.super.canEquip(slotContext, stack);
    }

    @Override
    public ICurio.@NotNull DropRule getDropRule(SlotContext context, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        Iterator<ModifierEntry> var7 = tool.getModifiers().iterator();
        if (var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            return ((CurioDropRuleModifierHook)entry.getHook(TCLibHooks.CURIO_DROP_RULE)).getCurioDropRule(tool, entry, source, lootingLevel, recentlyHit, stack);
        } else {
            return tool.getModifierLevel(ModifierIds.soulbound) > 0 ? DropRule.ALWAYS_KEEP : DropRule.DEFAULT;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(IToolStackView tool, EquipmentSlot slot) {
        return ImmutableMultimap.of();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return ImmutableMultimap.of();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext context, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        ToolStack tool = ToolStack.from(stack);
        this.getCurioStatAttributes(map, tool, uuid);

        for(ModifierEntry entry : tool.getModifierList()) {
            CurioAttributeModifierHook hook = (CurioAttributeModifierHook)entry.getHook(TCLibHooks.CURIO_ATTRIBUTE);
            Objects.requireNonNull(map);
            hook.modifyCurioAttribute(tool, entry, context, uuid, map::put);
        }

        return map;
    }

    protected void getCurioStatAttributes(Multimap<Attribute, AttributeModifier> modifiers, ToolStack tool, UUID uuid) {
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.isDamaged() ? 1 : this.maxStackSize;
    }

    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.isCurse() && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentModifierHook.getEnchantmentLevel(stack, enchantment);
    }

    @Override
    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentModifierHook.getAllEnchantments(stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ToolCapabilityProvider(stack);
    }

    @Override
    public void verifyTagAfterLoad(CompoundTag nbt) {
        ToolStack.verifyTag(this, nbt, this.getToolDefinition());
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        ToolStack.ensureInitialized(stack, this.getToolDefinition());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ModifierUtil.checkVolatileFlag(stack, SHINY);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return RarityModule.getRarity(stack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return IndestructibleItemEntity.hasCustomEntity(stack);
    }

    @Override
    @Nullable
    public Entity createEntity(Level world, Entity original, ItemStack stack) {
        return IndestructibleItemEntity.createFrom(world, original, stack);
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ToolDamageUtil.getFakeMaxDamage(stack);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return !this.canBeDepleted() ? 0 : ToolStack.from(stack).getDamage();
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (this.canBeDepleted()) {
            ToolStack.from(stack).setDamage(damage);
        }

    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T damager, Consumer<T> onBroken) {
        ToolDamageUtil.handleDamageItem(stack, amount, damager, onBroken);
        return 0;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getCount() == 1 && DurabilityDisplayModifierHook.showDurabilityBar(stack);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityRGB(pStack);
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return DurabilityDisplayModifierHook.getDurabilityWidth(pStack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        return stack.getCount() > 1 || EntityInteractionModifierHook.leftClickEntity(stack, player, target);
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return this.canPerformAction(stack, TinkerToolActions.SHIELD_DISABLE);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return IsEffectiveToolHook.isEffective(ToolStack.from(stack), state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        return ToolHarvestLogic.mineBlock(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return stack.getCount() == 1 ? MiningSpeedToolHook.getDestroySpeed(stack, state) : 0.0F;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        return stack.getCount() > 1 || ToolHarvestLogic.handleBlockBreak(stack, pos, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack held, Slot slot, ClickAction action, Player player) {
        return SlotStackModifierHook.overrideStackedOnOther(held, slot, action, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack slotStack, ItemStack held, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return SlotStackModifierHook.overrideOtherStackedOnMe(slotStack, held, slot, action, player, access);
    }

    protected static boolean shouldInteract(@Nullable LivingEntity player, ToolStack toolStack, InteractionHand hand) {
        IModDataView volatileData = toolStack.getVolatileData();
        if (volatileData.getBoolean(NO_INTERACTION)) {
            return false;
        } else if (hand == InteractionHand.OFF_HAND) {
            return true;
        } else {
            return player == null || !volatileData.getBoolean(DEFER_OFFHAND) || player.getOffhandItem().isEmpty();
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            InteractionHand hand = context.getHand();
            if (shouldInteract(context.getPlayer(), tool, hand)) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = ((BlockInteractionModifierHook)entry.getHook(ModifierHooks.BLOCK_INTERACT)).beforeBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (stack.getCount() == 1) {
            ToolStack tool = ToolStack.from(stack);
            InteractionHand hand = context.getHand();
            if (shouldInteract(context.getPlayer(), tool, hand)) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = ((BlockInteractionModifierHook)entry.getHook(ModifierHooks.BLOCK_INTERACT)).afterBlockUse(tool, entry, context, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player playerIn, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        ToolStack tool = ToolStack.from(stack);
        if (shouldInteract(playerIn, tool, hand)) {
            for(ModifierEntry entry : tool.getModifierList()) {
                InteractionResult result = ((EntityInteractionModifierHook)entry.getHook(ModifierHooks.ENTITY_INTERACT)).afterEntityUse(tool, entry, playerIn, target, hand, InteractionSource.RIGHT_CLICK);
                if (result.consumesAction()) {
                    return result;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack stack = playerIn.getItemInHand(hand);
        if (stack.getCount() > 1) {
            return InteractionResultHolder.pass(stack);
        } else {
            ToolStack tool = ToolStack.from(stack);
            if (shouldInteract(playerIn, tool, hand)) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    InteractionResult result = ((GeneralInteractionModifierHook)entry.getHook(ModifierHooks.GENERAL_INTERACT)).onToolUse(tool, entry, playerIn, hand, InteractionSource.RIGHT_CLICK);
                    if (result.consumesAction()) {
                        return new InteractionResultHolder(result, stack);
                    }
                }
            }

            return InteractionResultHolder.pass(stack);
        }
    }

    @Override
    public void onUseTick(@NotNull Level pLevel, @NotNull LivingEntity entityLiving, @NotNull ItemStack stack, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = (GeneralInteractionModifierHook)activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);

        for(ModifierEntry entry : tool.getModifiers()) {
            ((UsingToolModifierHook)entry.getHook(ModifierHooks.TOOL_USING)).onUsingTick(tool, entry, entityLiving, duration, timeLeft, activeModifier);
        }

        hook.onUsingTick(tool, activeModifier, entityLiving, timeLeft);
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        if (super.canContinueUsing(oldStack, newStack) && oldStack != newStack) {
            GeneralInteractionModifierHook.finishUsing(ToolStack.from(oldStack));
        }

        return super.canContinueUsing(oldStack, newStack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = (GeneralInteractionModifierHook)activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);

        for(ModifierEntry entry : tool.getModifiers()) {
            ((UsingToolModifierHook)entry.getHook(ModifierHooks.TOOL_USING)).beforeReleaseUsing(tool, entry, entityLiving, duration, 0, activeModifier);
        }

        hook.onFinishUsing(tool, activeModifier, entityLiving);
        return stack;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        GeneralInteractionModifierHook hook = (GeneralInteractionModifierHook)activeModifier.getHook(ModifierHooks.GENERAL_INTERACT);
        int duration = hook.getUseDuration(tool, activeModifier);

        for(ModifierEntry entry : tool.getModifiers()) {
            ((UsingToolModifierHook)entry.getHook(ModifierHooks.TOOL_USING)).beforeReleaseUsing(tool, entry, entityLiving, duration, timeLeft, activeModifier);
        }

        hook.onStoppedUsing(tool, activeModifier, entityLiving, timeLeft);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        UsingToolModifierHook.afterStopUsing(tool, entity, timeLeft);
        GeneralInteractionModifierHook.finishUsing(tool);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        return activeModifier != ModifierEntry.EMPTY ? ((GeneralInteractionModifierHook)activeModifier.getHook(ModifierHooks.GENERAL_INTERACT)).getUseDuration(tool, activeModifier) : 0;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        ToolStack tool = ToolStack.from(stack);
        ModifierEntry activeModifier = GeneralInteractionModifierHook.getActiveModifier(tool);
        return activeModifier != ModifierEntry.EMPTY ? ((GeneralInteractionModifierHook)activeModifier.getHook(ModifierHooks.GENERAL_INTERACT)).getUseAction(tool, activeModifier) : UseAnim.NONE;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return stack.getCount() == 1 && ModifierUtil.canPerformAction(ToolStack.from(stack), toolAction);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return ToolNameHook.getName(this.getToolDefinition(), stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        TooltipUtil.addInformation(this, stack, level, tooltip, SafeClientAccess.getTooltipKey(), flag);
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return TooltipUtil.getModifierHideFlags(this.getToolDefinition());
    }

    @Override
    public @NotNull ItemStack getRenderTool() {
        if (this.toolForRendering == null) {
            this.toolForRendering = ToolBuildHandler.buildToolForRendering(this, this.getToolDefinition());
        }

        return this.toolForRendering;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return this.shouldCauseReequipAnimation(oldStack, newStack, false);
    }

    @Override
    public @NotNull ToolDefinition getToolDefinition() {
        return this.toolDefinition;
    }
}
