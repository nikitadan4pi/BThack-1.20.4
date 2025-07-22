package com.nikitadan4pi.BThack.mixins.gui_and_hud;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class MixinScreen {
    //Yeah, it looks really dumb, but who cares?

    @Shadow @Nullable protected MinecraftClient client;
    @Unique
    private float _mouseX;
    @Unique
    private float _mouseY;

    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    public void modifyRenderBackgroundTexture(DrawContext context, CallbackInfo ci) {
        if (ModuleList.clientSetting.bthackMainMenu.getValue()) {
            ci.cancel();
            BThackMainMenuScreen.drawWallpaper(_mouseX, _mouseY);
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (client.world == null) {
            _mouseX = mouseX;
            _mouseY = mouseY;
        }
    }
}
