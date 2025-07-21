package com.nikitadan4pi.BThack.impl.Modules.PLAYER;

import com.nikitadan4pi.BThack.Core.Render.BThackRender;
import com.nikitadan4pi.BThack.api.Events.Block.AttackBlockEvent;
import com.nikitadan4pi.BThack.api.Events.Block.UseBlockEvent;
import com.nikitadan4pi.BThack.api.Events.Entity.AttackEntityEvent;
import com.nikitadan4pi.BThack.api.Events.Render.RenderHudPostEvent;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.nikitadan4pi.BThack.api.Module.Module;
import com.nikitadan4pi.BThack.api.Utils.InventoryUtils;
import com.nikitadan4pi.BThack.api.Utils.ItemUtils;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.Event;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ItemSaver extends Module {

    public final NumberSetting minStrength = new NumberSetting("Min Strength(%)", this, 5, 1, 90, false);
    public final BooleanSetting attackSaver = new BooleanSetting("Attack Saver", this, true);

    public ItemSaver() {
        super("ItemSaver",
                "lang.module.ItemSaver",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );

        initSettings(
                minStrength,
                attackSaver
        );
    }

    private int alpha = 0;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        arrayListInfo = minStrength.getValue() + "%";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        arrayListInfo = minStrength.getValue() + "%";
    }

    @EventSubscriber
    public void onRenderOverlay(RenderHudPostEvent e) {
        if (alpha > 0) {
            BThackRender.drawString(LanguageSystem.translate("lang.module.ItemSaver.saveMessage"), (mc.getWindow().getScaledWidth() / 2f) - (mc.textRenderer.getWidth(LanguageSystem.translate("lang.module.ItemSaver.saveMessage")) / 2f), (mc.getWindow().getScaledHeight() / 2f) + 40, new Color(255, 98, 0, Math.min(Math.max(alpha, 1), 255)).hashCode());
        }
        if (alpha > 0) alpha--;
    }

    @EventSubscriber
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck()) return;

        check(e);
    }

    @EventSubscriber
    public void onUseBlock(UseBlockEvent e) {
        if (nullCheck()) return;

        check(e);
    }

    @EventSubscriber
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (attackSaver.getValue()) check(e);
    }


    private void check(Event e) {
        ItemStack item = InventoryUtils.getItem(mc.player.getInventory().selectedSlot);
        if (item.getItem() instanceof BlockItem) return;
        float currentDamage = ItemUtils.getItemDurabilityInPercentages(item);
        if (currentDamage < minStrength.getValue()) {
            if (alpha != 380) alpha = 380;
            e.setCancelled(true);
        }
    }
}
