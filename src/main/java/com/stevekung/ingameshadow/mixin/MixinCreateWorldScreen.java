package com.stevekung.ingameshadow.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stevekung.ingameshadow.SeedUtils;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldGenSettingsComponent;
import net.minecraft.network.chat.TextComponent;

@Mixin(CreateWorldScreen.class)
public abstract class MixinCreateWorldScreen extends Screen
{
    @Shadow
    @Final
    WorldGenSettingsComponent worldGenSettingsComponent;

    @Override
    @Shadow
    protected abstract <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T guiEventListener);

    @Unique
    private Button shadowSeedButton;

    MixinCreateWorldScreen()
    {
        super(null);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo info)
    {
        this.shadowSeedButton = this.addRenderableWidget(new Button(this.width / 2 + 104, 60, 20, 20, new TextComponent("S"), button ->
        {
            var accessor = (WorldGenSettingsComponentAccessor) this.worldGenSettingsComponent;
            var optionalLong = accessor.invokeParseSeed();

            if (optionalLong.isPresent())
            {
                accessor.getSeedEdit().setValue(String.valueOf(SeedUtils.getShadowSeed(optionalLong.getAsLong())));
            }
        }, (button, poseStack, x, y) -> this.renderTooltip(poseStack, new TextComponent("Shadow Seed"), x, y)));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo info)
    {
        var accessor = (WorldGenSettingsComponentAccessor) this.worldGenSettingsComponent;
        this.shadowSeedButton.active = !accessor.getSeedEdit().getValue().isEmpty();
    }

    @Inject(method = "setWorldGenSettingsVisible", at = @At("TAIL"))
    private void setWorldGenSettingsVisible(boolean enable, CallbackInfo info)
    {
        this.shadowSeedButton.visible = enable;
    }
}