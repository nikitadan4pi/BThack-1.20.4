package com.nikitadan4pi.BThack.impl.Commands;

import com.nikitadan4pi.BThack.api.CommandSystem.command.AbstractCommand;
import com.nikitadan4pi.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.nikitadan4pi.BThack.api.Managers.managers.Destroy.DestroyThread3D;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.api.Utils.MathUtils;
import com.nikitadan4pi.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;

public class BreakCommand extends AbstractCommand {

    public BreakCommand() {
        super("lang.command.BreakCommand.description", "break [x] [y] [z]", "break"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            invalidArgumentError();
            return;
        }
        if (!MathUtils.isDouble(args[0]) || !MathUtils.isDouble(args[1]) || !MathUtils.isDouble(args[2])) {
            invalidArgumentError();
            return;
        }
        if (DestroyManager.isDestroying) {
            ChatUtils.sendMessage(Formatting.RED + LanguageSystem.translate("lang.command.BreakCommand.alreadyDestroying"));
            return;
        }

        DestroyThread3D thread3D = new DestroyThread3D();
        thread3D.set3DSchematic(new ArrayList<>(Arrays.asList(new Vec3d(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])))), new ModifyBlockPos(mc.player.getX(), mc.player.getY(), mc.player.getZ()));
        thread3D.start();
    }
}
