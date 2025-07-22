package com.nikitadan4pi.BThack.api.Utils.Account;

import com.mojang.util.UndashedUuid;
import net.minecraft.nbt.NbtCompound;

public class AccountCache {
    public String username = "";
    public String uuid = "";

   // @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.putString("username", username);
        tag.putString("uuid", uuid);

        return tag;
    }

  //  @Override
    public AccountCache fromTag(NbtCompound tag) {
        if (!tag.contains("username") || !tag.contains("uuid")) throw new RuntimeException();

        username = tag.getString("username");
        uuid = tag.getString("uuid");
        return this;
    }
}