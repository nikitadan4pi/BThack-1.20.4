package com.nikitadan4pi.BThack.impl.Modules.COMBAT;

import com.nikitadan4pi.BThack.api.Events.Entity.AttackEntityEvent;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.InventoryUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.nikitadan4pi.BThack.api.Utils.Modules.AutoSwordUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.decoration.EndCrystalEntity;

public class AutoSword extends Module {
    public AutoSword() {
        super("AutoSword",
                "lang.module.AutoSword",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );
    }

    @EventSubscriber
    public void onPacket(AttackEntityEvent e) {
        if (e.getEntity() instanceof EndCrystalEntity || e.getPlayer() != mc.player) return;

        int inventorySlot = AutoSwordUtils.getSword();
        if (inventorySlot != -1) {
            InventoryUtils.swapItem(inventorySlot);
        }
    }
}
