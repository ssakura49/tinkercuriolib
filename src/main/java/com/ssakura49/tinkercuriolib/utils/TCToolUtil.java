package com.ssakura49.tinkercuriolib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import slimeknights.tconstruct.common.TinkerTags.Items;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class TCToolUtil {
    public static boolean isTool(ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            Item item = stack.getItem();
            if (item instanceof IModifiable modifiable) {
                return modifiable.getToolDefinition() != ToolDefinition.EMPTY;
            }
        }

        return false;
    }

    public static List<SlotResult> findAllSlot(LivingEntity entity) {
        Optional<ICuriosItemHandler> optional = CuriosApi.getCuriosInventory(entity).resolve();
        return optional.isPresent() ? ((ICuriosItemHandler)optional.get()).findCurios(TCToolUtil::isTool) : List.of();
    }

//    public static void projectileDamageBonus(LivingEntity shooter, Projectile projectile) {
//        if (!findAllSlot(shooter).isEmpty()) {
//            for(SlotResult result : findAllSlot(shooter)) {
//                float bonus = (Float)ToolStack.from(result.stack()).getStats().get(TCToolStats.PROJECTILE_BONUS_CURIO);
//                if (projectile instanceof AbstractArrow arrow) {
//                    arrow.setBaseDamage(arrow.getBaseDamage() * (double)(1.0F + bonus));
//                }
//            }
//        }
//
//    }

    public static List<ItemStack> getStacks(LivingEntity entity) {
        List<ItemStack> list = new ArrayList<>();
        CuriosApi.getCuriosInventory(entity).ifPresent((handler) -> handler.getCurios().forEach((id, curios) -> {
            for(int i = 0; i < curios.getSlots(); ++i) {
                ItemStack stack = curios.getStacks().getStackInSlot(i);
                if (!stack.isEmpty() && stack.is(Items.MODIFIABLE)) {
                    list.add(stack);
                }
            }

        }));
        return list;
    }

    public static List<ItemStack> getAllToolStackInCurios(LivingEntity entity, Predicate<ItemStack> predicate) {
        List<ItemStack> result = new ArrayList();
        LazyOptional<ICuriosItemHandler> handlerOptional = CuriosApi.getCuriosInventory(entity);
        if (handlerOptional.isPresent()) {
            ICuriosItemHandler handler = (ICuriosItemHandler)handlerOptional.orElseThrow(IllegalStateException::new);
            IItemHandlerModifiable curiosInv = handler.getEquippedCurios();

            for(int i = 0; i < handler.getSlots(); ++i) {
                ItemStack curio = curiosInv.getStackInSlot(i);
                if (predicate.test(curio)) {
                    result.add(curio);
                }
            }
        }

        return result;
    }

    public static ItemStack getToolStackInCurios(LivingEntity entity, Predicate<ItemStack> predicate) {
        LazyOptional<ICuriosItemHandler> handlerOptional = CuriosApi.getCuriosInventory(entity);
        if (handlerOptional.isPresent()) {
            ICuriosItemHandler handler = (ICuriosItemHandler)handlerOptional.orElseThrow(IllegalStateException::new);
            IItemHandlerModifiable curiosInv = handler.getEquippedCurios();

            for(int i = 0; i < handler.getSlots(); ++i) {
                ItemStack curio = curiosInv.getStackInSlot(i);
                if (predicate.test(curio)) {
                    return curio;
                }
            }
        }

        return null;
    }

    public static String getEquipmentSlotNameInCurios(LivingEntity entity, ItemStack stack) {
        LazyOptional<ICuriosItemHandler> handlerOptional = CuriosApi.getCuriosInventory(entity);
        if (handlerOptional.isPresent()) {
            ICuriosItemHandler handler = (ICuriosItemHandler)handlerOptional.orElseThrow(IllegalStateException::new);
            Optional<SlotResult> slotResultOptional = handler.findFirstCurio((curio) -> ItemStack.isSameItem(stack, curio));
            if (slotResultOptional.isPresent()) {
                SlotResult slotResult = (SlotResult)slotResultOptional.orElseThrow(IllegalStateException::new);
                return slotResult.slotContext().identifier();
            }
        }

        return null;
    }
}
