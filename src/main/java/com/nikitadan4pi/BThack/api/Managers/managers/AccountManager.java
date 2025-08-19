package com.nikitadan4pi.BThack.api.Managers.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nikitadan4pi.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.nikitadan4pi.BThack.Core.FileSystem.JsonUtils;
import com.nikitadan4pi.BThack.api.Utils.Account.Account;
import com.nikitadan4pi.BThack.api.Utils.Account.types.CrackedAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        if(accounts.contains(account)) return;
        accounts.add(account);
    }

    public void removeAccount(String name) {
        for (Account account : accounts){
            if (account.getUsername() == name) {
                accounts.remove(account);
                return;
            }
        }
    }

    public void replaceAccount(Account oldAccount, Account newAccount) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).equals(oldAccount)) {
                accounts.set(i, newAccount);
                return;
            }
        }
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public void save() throws IOException {
        ConfigUtils.saveInJson("Accounts", "", jsonObject -> {
            JsonArray accountList = new JsonArray();
            accounts.forEach(account -> {
                JsonObject accountObject = new JsonObject();
                JsonUtils.add(accountObject, "Name", account.getUsername());
                accountList.add(accountObject);
            });
            jsonObject.add("Accounts", accountList);
        });
    }

    public void load() throws IOException {
        ConfigUtils.loadFromJson("Accounts", "", jsonObject -> {
            if (!JsonUtils._null(jsonObject, "Accounts")) {
                JsonArray accountList = jsonObject.get("Accounts").getAsJsonArray();
                accountList.forEach(jsonElement -> {
                    if (jsonElement.isJsonObject()) {
                        JsonObject accountObject = jsonElement.getAsJsonObject();
                        String data = new String();

                        if (!JsonUtils._null(accountObject, "Name")) {
                            try {
                                data = accountObject.get("Name").getAsString();
                            } catch (Exception ignored) {
                                return;
                            }
                        }
                        addAccount(new CrackedAccount(data));
                    }
                });
            }
        }, () -> {});
    }
}
