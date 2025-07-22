package com.nikitadan4pi.BThack.api.Shader;

import net.minecraft.client.render.VertexFormats;

public class Shaders {
    public static Shaders INSTANCE;

    public final ShaderProgram POSITION = ShaderProgram.of("render/position", VertexFormats.POSITION);
}
