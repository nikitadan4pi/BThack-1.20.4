package com.nikitadan4pi.BThack.impl.Modules.COMBAT;

import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.InventoryUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.item.Items;

public class AutoTotemFall extends Module {

    public static NumberSetting fallCheck;
    public static NumberSetting toGround;

    public AutoTotemFall() {
        super("AutoTotemFall",
                "lang.module.AutoTotemFall",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        fallCheck = new NumberSetting("Fall Check", this, 10, 5, 20, true);
        toGround = new NumberSetting("To Ground", this, 5, 3, 10, true);

        initSettings(
                fallCheck,
                toGround
        );
    }

    boolean isFalling = false;

    @Override
    public void onEnable() {
        super.onEnable();
        isFalling = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        isFalling = false;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        if (mc.player.fallDistance > fallCheck.getValue()) {
            isFalling = !mc.player.verticalCollision;
        } else {
            isFalling = false;
        }

        if (!isFalling) return;
        if (PlayerUtils.getGroundPos(mc.world, mc.player).y + toGround.getValue() > mc.player.getY()) {
            if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {

                int slot = InventoryUtils.findItem(Items.TOTEM_OF_UNDYING);
                if (slot == -1) return;

                if (slot < 9) slot += 36;

                InventoryUtils.replaceItems(slot, InventoryUtils.OFFHAND_SLOT, 5);
            }
        }
    }
}
