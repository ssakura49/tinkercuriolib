package com.ssakura49.tinkercuriolib.tools.module;

import com.ssakura49.tinkercuriolib.hook.TCLibHooks;
import com.ssakura49.tinkercuriolib.hook.behavior.CurioAttributeModifierHook;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.primitive.DoubleLoadable;
import slimeknights.mantle.data.loadable.primitive.ResourceLocationLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.TinkerLoadables;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

public record MultiCurioAttributeModule(ResourceLocation name, List<AttributeEntry> attributes) implements ModifierModule, CurioAttributeModifierHook {
    public static final RecordLoadable<MultiCurioAttributeModule> LOADER;

    public @NotNull RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TCLibHooks.CURIO_ATTRIBUTE);
    }

    public static UUID getUUIDFromString(String str) {
        int hash = str.hashCode();
        Random r = new Random((long)hash);
        long l0 = r.nextLong();
        long l1 = r.nextLong();
        return new UUID(l0, l1);
    }

    public void modifyCurioAttribute(IToolStackView curio, ModifierEntry entry, SlotContext context, UUID uuid, BiConsumer<Attribute, AttributeModifier> consumer) {
        String var10000 = String.valueOf(entry.getId());
        UUID baseUuid = UUID.nameUUIDFromBytes((var10000 + "." + context.identifier() + "." + context.index()).getBytes());

        for(AttributeEntry attributeEntry : this.attributes) {
            UUID attributeUuid = new UUID(baseUuid.getMostSignificantBits() ^ (long)attributeEntry.attribute().getDescriptionId().hashCode(), baseUuid.getLeastSignificantBits());
            consumer.accept(attributeEntry.attribute(), new AttributeModifier(attributeUuid, this.name.toString(), attributeEntry.value() * (double)entry.getLevel(), attributeEntry.operation()));
        }

    }

    static {
        LOADER = RecordLoadable.create(ResourceLocationLoadable.DEFAULT.requiredField("name", MultiCurioAttributeModule::name), MultiCurioAttributeModule.AttributeEntry.LOADER.list(0).requiredField("attributes", MultiCurioAttributeModule::attributes), MultiCurioAttributeModule::new);
    }

    public static record AttributeEntry(Attribute attribute, AttributeModifier.Operation operation, double value) {
        public static final RecordLoadable<AttributeEntry> LOADER;

        static {
            LOADER = RecordLoadable.create(Loadables.ATTRIBUTE.requiredField("attribute", AttributeEntry::attribute), TinkerLoadables.OPERATION.requiredField("operation", AttributeEntry::operation), DoubleLoadable.ANY.requiredField("value", AttributeEntry::value), AttributeEntry::new);
        }
    }
}
