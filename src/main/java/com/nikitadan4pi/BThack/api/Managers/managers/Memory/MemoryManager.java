package com.nikitadan4pi.BThack.api.Managers.managers.Memory;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class MemoryManager implements Mc {

   public void cleanMemory() {
      if (ModuleList.memoryCleaner.isEnabled()) {
         Runnable runnable = new CleanerThread();
         Thread gcThread = new Thread(runnable, "MemoryCleaner GC Thread");
         gcThread.setDaemon(true);
         gcThread.start();
      } else {
         if (mc.player != null && mc.world != null) {
            ChatUtils.sendMessage(Formatting.YELLOW + "Memory Cleaner module is disabled, please enable it before next use.");
            if (ModuleList.cleanMemory.isEnabled()) {
               ModuleList.cleanMemory.setToggled(false);
            }
         }
      }
   }
}
