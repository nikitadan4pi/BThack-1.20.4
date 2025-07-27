package com.nikitadan4pi.BThack.api.Shader;

import baritone.api.event.events.RenderEvent;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.nikitadan4pi.BThack.api.Events.DisconnectEvent;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import static com.nikitadan4pi.BThack.api.Interfaces.Mc.mc;

public class Shaders {
    public static Shaders INSTANCE;
    public static ShaderTicker shaderTicker = new ShaderTicker();

    public final ShaderProgram POSITION = ShaderProgram.of("render/position", VertexFormats.POSITION);
    public final ShaderProgram X_RAINBOW = new ShaderProgram(Identifier.of("bthack", "render/x_rainbow"), VertexFormats.POSITION) {
        @Override
        public void use() {
            this.setUniformValue("time", shaderTicker.getPassedTime() / 1000f);
            this.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
            super.use();
        }

        @Override
        public void release() {
            this.setUniformValue("alpha", 1f);
            this.setUniformValue("brightness", 1f);
            this.setUniformValue("scale", 1f);
            this.setUniformValue("speed", 1f);
            super.release();
        }
    };
    public final ShaderProgram XY_GRADIENT = new ShaderProgram(Identifier.of("bthack", "render/xy_gradient"), VertexFormats.POSITION) {
        @Override
        public void use() {
            this.setUniformValue("time", shaderTicker.getPassedTime() / 1000f);
            this.setUniformValue("resolution", (float) mc.getWindow().getWidth(), mc.getWindow().getHeight());
            super.use();
        }

        @Override
        public void release() {
            this.setUniformValue("brightness", 1f);
            this.setUniformValue("scale", 1f);
            this.setUniformValue("speed", 1f);
            super.release();
        }

        @EventSubscriber
        public void onDisconnect(DisconnectEvent e) {
            shaderTicker.reset();
        }

        @EventSubscriber
        public void onRender(RenderEvent e) {
            this.updateTime();
        }

        public static void updateTime() {
            shaderTicker.update(1);
        }
    };
    public static void updateTime() {
        shaderTicker.update(1);
    }
}
