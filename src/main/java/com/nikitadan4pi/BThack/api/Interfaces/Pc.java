package com.nikitadan4pi.BThack.api.Interfaces;

import com.nikitadan4pi.BTbot.api.Utils.Controller.ClientPlayerController;
import com.nikitadan4pi.BThack.BThack;

public interface Pc {
    ClientPlayerController pc = BThack.instance.playerController;
}
