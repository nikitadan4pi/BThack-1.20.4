package com.nikitadan4pi.BThack.api.Managers;

import com.nikitadan4pi.BThack.api.Managers.managers.*;
import com.nikitadan4pi.BThack.api.Managers.managers.Destroy.DestroyManager;
import com.nikitadan4pi.BThack.api.Managers.managers.Memory.MemoryManager;
import com.nikitadan4pi.BThack.api.Managers.managers.Setting.SettingsManager;
import com.nikitadan4pi.BThack.api.Managers.managers.TravelChange.TravelChangeManager;

public class Managers {
    public static final TPSManager TPS_MANAGER = new TPSManager();
    public static final BlockSearchManager BLOCK_SEARCH_MANAGER = new BlockSearchManager();
    public static final FireWorkManager FIREWORK_MANAGER = new FireWorkManager();
    public static final DestroyManager DESTROY_MANAGER = new DestroyManager();
    public static final NetworkManager NETWORK_MANAGER = new NetworkManager();
    public static final TickManager TICK_MANAGER = new TickManager();
    public static final MainMenuShaderManager MAIN_MENU_SHADER_MANAGER = new MainMenuShaderManager();
    public static final TotemPopManager TOTEM_POP_MANAGER = new TotemPopManager();
    public static final SettingsManager SETTINGS_MANAGER = new SettingsManager();
    public static final MemoryManager MEMORY_MANAGER = new MemoryManager();
    public static final TravelChangeManager TRAVEL_CHANGE_MANAGER = new TravelChangeManager();
}
