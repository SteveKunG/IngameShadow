package com.stevekung.ingameshadow.mixin;

import java.util.OptionalLong;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.worldselection.WorldGenSettingsComponent;

@Mixin(WorldGenSettingsComponent.class)
public interface WorldGenSettingsComponentAccessor
{
    @Accessor("seedEdit")
    EditBox getSeedEdit();

    @Invoker
    OptionalLong invokeParseSeed();
}