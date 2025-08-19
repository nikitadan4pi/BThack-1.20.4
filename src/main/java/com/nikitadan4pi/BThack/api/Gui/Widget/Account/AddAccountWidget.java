package com.nikitadan4pi.BThack.api.Gui.Widget.Account;

import com.nikitadan4pi.BThack.api.GuiSystem.ScreenWidget;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.Button;
import com.nikitadan4pi.BThack.api.GuiSystem.buttons.TextFrameButton;
import com.nikitadan4pi.BThack.api.Managers.Managers;
import com.nikitadan4pi.BThack.api.SoundSystem.Sounds;
import com.nikitadan4pi.BThack.api.Utils.Account.Account;
import com.nikitadan4pi.BThack.api.Utils.Account.types.CrackedAccount;
import com.nikitadan4pi.BThack.api.Utils.KeyboardUtils;
import net.minecraft.client.gui.DrawContext;

public class AddAccountWidget extends ScreenWidget {
    private Account editableAccount = null;

    public AddAccountWidget() {
        super(140, 105, 1);
    }

    public AddAccountWidget(Account editableAccount) {
        this();
        this.editableAccount = editableAccount;
    }

    @Override
    public void init() {
        super.init();

        buttons.clear();
        buttons.add(new TextFrameButton(1, (int) xLeft + 70, (int) yUp + 15, 65, 10));
        Button button = Button.of(2, (int) xLeft + 70, (int) yUp + 90, 65, 10, "Confirm");
        button.setClickSound(Sounds.CONFIG_SAVED_OR_LOADED);
        buttons.add(button);

        if (editableAccount != null) {
            getButtonFromId(1).setText(editableAccount.getUsername());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawPlate();
        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        switch(activeButton.getId()){
            case 2 -> {
                Account account = new CrackedAccount(getButtonFromId(1).getText());
                if (editableAccount != null) Managers.ACCOUNT_MANAGER.replaceAccount(editableAccount, account);
                else Managers.ACCOUNT_MANAGER.addAccount(account);
                close();
            }
            case 1 -> buttons.get(0).selected = true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int shift) {
        if (keyCode == KeyboardUtils.KEY_ESCAPE) close();
        return super.keyPressed(keyCode, scanCode, shift);
    }

    @Override
    public void close() {
        super.close();
        parent.widgetManage.addWidget(new AccountsWidget());
    }
}
