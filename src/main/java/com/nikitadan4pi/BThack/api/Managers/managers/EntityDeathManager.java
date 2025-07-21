package com.nikitadan4pi.BThack.api.Managers.managers;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.api.Events.ClientTickEvent;
import com.nikitadan4pi.BThack.api.Events.Entity.EntityDeathEvent;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.Ticker;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.LivingEntity;

public class EntityDeathManager implements Mc {

    public EntityDeathManager() {
        delayTicker.reset();
    }


    Ticker delayTicker = new Ticker();
    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (Module.nullCheck()) return;

        if (delayTicker.passed(500)) {
            mc.world.getEntities().forEach(entity -> {
                if (entity instanceof LivingEntity entity2 && entity2.isDead()) {
                    EntityDeathEvent event = new EntityDeathEvent(entity2);
                    BThack.EVENT_BUS.activate(event);
                }
            });
            delayTicker.reset();
        }
    }
}
