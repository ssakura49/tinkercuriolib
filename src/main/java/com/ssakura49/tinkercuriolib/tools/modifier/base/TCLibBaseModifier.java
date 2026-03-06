package com.ssakura49.tinkercuriolib.tools.modifier.base;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;

import java.awt.*;

public class TCLibBaseModifier extends Modifier

{
    protected void registerHooks(ModuleHookMap.@NotNull Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this
        );
    }

    public boolean isNoLevels() {
        return false;
    }
    @Override
    public @NotNull Component getDisplayName(int level) {
        return this.isNoLevels() ? super.getDisplayName() : super.getDisplayName(level);
    }
}
