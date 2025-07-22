package com.nikitadan4pi.BThack.api.Events;

import com.ferra13671.MegaEvents.Base.Event;

public class SendMessageEvent extends Event {
    public final String message;

    public SendMessageEvent(String message) {
        this.message = message;
    }
}
