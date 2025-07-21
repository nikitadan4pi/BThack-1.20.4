package com.nikitadan4pi.BThack.api.Events;


import com.ferra13671.MegaEvents.Base.Event;

public class LightmapGammaColorEvent extends Event {

    public int gammaColor;

    public LightmapGammaColorEvent(int gammaColor) {
        this.gammaColor = gammaColor;
    }
}
