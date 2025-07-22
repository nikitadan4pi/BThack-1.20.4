package com.nikitadan4pi.BThack.impl.Modules.MISC;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.Events.Camera.PositionCameraEvent;
import com.nikitadan4pi.BThack.api.Events.Camera.RotateCameraEvent;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Events.GuiOpenEvent;
import com.nikitadan4pi.BThack.api.Events.InputEvent;
import com.nikitadan4pi.BThack.api.Events.Player.ChangePlayerLookEvent;
import com.nikitadan4pi.BThack.api.Events.Render.RenderHudPostEvent;
import com.nikitadan4pi.BThack.api.Events.SetOpaqueCubeEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.KeyCodeSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.impl.Modules.PLAYER.FreeCam;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.math.Vec2f;

import java.awt.*;

public class DeathCamera extends Module {

    public static KeyCodeSetting respawnKey;

    public DeathCamera() {
        super("DeathCamera",
                "lang.module.DeathCamera",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        respawnKey = new KeyCodeSetting("Respawn Key", this);

        initSettings(
                respawnKey
        );
    }

    private boolean death = false;
    private final FreeCam.FreeCamData freeCamData = new FreeCam.FreeCamData();

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        ModuleList.freeCam.setToggled(false);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        death = false;
        if (nullCheck()) return;
        mc.player.input = new KeyboardInput(mc.options);
        mc.player.requestRespawn();
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) {
            death = false;
            return;
        }

        if (mc.player.isDead()) {
            if (!death) {
                freeCamData.reset();
                death = true;
                mc.player.input = new FreeCam.FreecamKeyboardInput(mc.options, freeCamData);
                mc.player.setHealth(20);
            }
        }
    }

    @EventSubscriber
    public void onSetScreen(GuiOpenEvent e) {
        if (e.getScreen() instanceof DeathScreen) e.setCancelled(true);
    }

    @EventSubscriber
    public void onInput(InputEvent.KeyInputEvent e) {
        if (nullCheck()) return;

        if (respawnKey.isPressed() && death) {
            mc.player.requestRespawn();
            death = false;
            mc.player.input = new KeyboardInput(mc.options);
        }
    }

    @EventSubscriber
    public void onCameraPosition(PositionCameraEvent e) {
        if (death)
            e.setPosition(freeCamData.lastPosition.lerp(freeCamData.position, e.getTickDelta()));
    }

    @EventSubscriber
    public void onCameraRotate(RotateCameraEvent e) {
        if (death)
            e.setRotation(new Vec2f(freeCamData.yaw, freeCamData.pitch));
    }

    @EventSubscriber
    public void onMouseUpdate(ChangePlayerLookEvent e) {
        if (death) {
            e.cancel();
            freeCamData.changeLookDirection(e.cursorDeltaX, e.cursorDeltaY);
        }
    }

    @EventSubscriber
    public void onSetOpaqueCube(SetOpaqueCubeEvent e) {
        if (death)
            e.setCancelled(true);
    }

    @EventSubscriber
    public void onHudRender(RenderHudPostEvent e) {
        if (death) {
            BThackRender.drawCenteredString(String.format(LanguageSystem.translate("lang.module.DeathCamera.message"), KeyboardUtils.getKeyName(respawnKey.getValue())), mc.getWindow().getScaledWidth() / 2f, mc.getWindow().getScaledHeight() / 4f, new Color(255, 100, 100).hashCode());
        }
    }
}
