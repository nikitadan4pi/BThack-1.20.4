package com.nikitadan4pi.BThack.api.Category;

public record Category(String name) {

    public static Category of(String name) {
        return new Category(name);
    }
}
