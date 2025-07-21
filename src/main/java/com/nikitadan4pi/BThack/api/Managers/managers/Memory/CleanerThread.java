package com.nikitadan4pi.BThack.api.Managers.managers.Memory;

import com.nikitadan4pi.BThack.BThack;
import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import com.nikitadan4pi.BThack.api.Utils.ChatUtils;
import com.nikitadan4pi.BThack.impl.Modules.CLIENT.MemoryCleaner;
import net.minecraft.util.Formatting;

public class CleanerThread implements Runnable, Mc {

   protected CleanerThread() {
   }

   public void run() {
      BThack.log("Memory cleaner thread started!");
      if (MemoryCleaner.showMessages.getValue() && mc.player != null && mc.world != null) {
         ChatUtils.sendMessage(Formatting.LIGHT_PURPLE + "Starting memory cleaning, please wait...");
      }

      System.gc();

      try {
         Thread.sleep(1000L);
      } catch (InterruptedException ignored) {}

      System.gc();
      if (MemoryCleaner.showMessages.getValue()) {
         ChatUtils.sendMessage(Formatting.LIGHT_PURPLE + "Memory clearing completed successfully!");
      }

      BThack.log("Memory cleaner thread finished!");

      if (ModuleList.cleanMemory.isEnabled()) {
         ModuleList.cleanMemory.setToggled(false);
      }
   }
}
