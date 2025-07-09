package com.ferra13671.BThack.api.Shader;

import net.minecraft.client.render.VertexFormats;

public class Shaders {
    public static Shaders INSTANCE;

    public final ShaderProgram POSITION = ShaderProgram.of("render/position", VertexFormats.POSITION);
    public final ShaderProgram ROUNDED_RECT_WITH_OUTLINE = ShaderProgram.of("render/rounded_rect_with_outline", VertexFormats.POSITION);

}
