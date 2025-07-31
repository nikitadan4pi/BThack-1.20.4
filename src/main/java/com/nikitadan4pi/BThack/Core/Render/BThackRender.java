package com.nikitadan4pi.BThack.Core.Render;

import com.nikitadan4pi.BThack.Core.Client.ModuleList;
import com.nikitadan4pi.BThack.Core.Render.Box.BThackBoxRender;
import com.nikitadan4pi.BThack.Core.Render.Line.BThackLineRender;
import com.nikitadan4pi.BThack.Core.Render.Utils.*;
import com.nikitadan4pi.BThack.api.Shader.ShaderProgram;
import com.nikitadan4pi.BThack.api.Shader.Shaders;
import com.nikitadan4pi.BThack.api.Utils.RegionPos;
import com.ferra13671.TextureUtils.GLTexture;
import com.ferra13671.TextureUtils.GlTex;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import org.joml.Matrix4f;
import com.nikitadan4pi.BThack.api.Interfaces.Mc;
import org.joml.Vector3f;

import static com.nikitadan4pi.BThack.Core.Render.Utils.BThackRenderUtils.*;

public final class BThackRender implements Mc {

    public static final VertexConsumerProvider.Immediate bufferSource = mc.getBufferBuilders().getEntityVertexConsumers();
    public static final DrawContext guiGraphics = new DrawContext(mc, bufferSource);
    public static final BThackWorldRenderContext worldRenderContext = new BThackWorldRenderContext();
    public static final BThackBoxRender boxRender = new BThackBoxRender();
    public static final BThackLineRender lineRender = new BThackLineRender();
    //public static Font defaultFont;
    //public static FontRenderManager fontRenderManager = new FontRenderManager(defaultFont);

    private static final ScissorStack scissorStack = new ScissorStack();

    private static boolean inited = false;

    public static void init()  {
        if (!inited) {
            boxRender.init();
            //now font system is crashing client. I can't fix it now XD
            /*if (DeviceSystem.getLaunchDevice() == DeviceSystem.LaunchDevice.PC && false) {
                try {
                    defaultFont = FontUtils.createFontNoThrow(ConfigUtils.newInputStream("assets/bthack/fonts/defaultFont.ttf", PathMode.INSIDEJAR), 17);
                    reloadFontRenderManager();
                } catch (Exception e) {
                    BThack.error(e.getMessage());
                }
            }*/
        }
        RenderSystem.recordRenderCall(() -> Shaders.INSTANCE = new Shaders());
        inited = true;
    }

    public static void trace(Vec3d vec3d, Matrix4f matrix, Vec3d start, float red, float green, float blue, float alpha, BufferBuilder bufferBuilder, Vec3d regionVec) {
        Shaders.INSTANCE.POSITION.use();
        Shaders.INSTANCE.POSITION.setUniformValue("color", red, green, blue, alpha);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

        Vec3d end = vec3d.subtract(new Vec3d(regionVec.x, regionVec.y, regionVec.z));
        bufferBuilder.vertex(matrix, (float)start.x, (float)start.y, (float)start.z).next();
        bufferBuilder.vertex(matrix, (float)end.x, (float)end.y, (float)end.z).next();
        draw();
    }

    public static void drawRect(float x1, float y1, float x2, float y2, int color) {
        drawRect(x1, y1, x2, y2, color, guiGraphics.getMatrices().peek().getPositionMatrix());
    }

    public static void drawRect(float x1, float y1, float x2, float y2, int color, Matrix4f matrix4f) {
        if (x1 < x2) {
            float tempX = x1;
            x1 = x2;
            x2 = tempX;
        }

        if (y1 < y2) {
            float tempY = y1;
            y1 = y2;
            y2 = tempY;
        }

        float[] c = hashCodeToRGBA(color);

        Shaders.INSTANCE.POSITION.use();
        Shaders.INSTANCE.POSITION.setUniformValue("color", c[0], c[1], c[2], c[3]);

        BufferBuilder buffer = prepareToDraw();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        buffer.vertex(matrix4f, x1, y1, 0).next();
        buffer.vertex(matrix4f, x1, y2, 0).next();
        buffer.vertex(matrix4f, x2, y2, 0).next();
        buffer.vertex(matrix4f, x2, y1, 0).next();

        draw();
        Shaders.INSTANCE.POSITION.release();
    }

    public static void drawLine(float x1, float y1, float x2, float y2, float width, int color) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        if (x1 > x2) {
            float tempX = x1;
            x1 = x2;
            x2 = tempX;
        }

        if (y1 > y2) {
            float tempY = y1;
            y1 = y2;
            y2 = tempY;
        }

        width = width / 2;

        float[] c = hashCodeToRGBA(color);

        BufferBuilder buffer = prepareToDraw();
        Shaders.INSTANCE.POSITION.use();
        Shaders.INSTANCE.POSITION.setUniformValue("color", c[0], c[1], c[2], c[3]);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        buffer.vertex(matrix4f, x2 + width, y2 + width, 0).next();
        buffer.vertex(matrix4f, x2 + width, y2 - width, 0).next();
        buffer.vertex(matrix4f, x1 - width, y1 - width, 0).next();
        buffer.vertex(matrix4f, x1 - width, y1 + width, 0).next();

        draw();
    }

    public static void drawRoundedRectOld(float x1, float y1, float x2, float y2, float radius, int color) {
        float[] rgba = ColorUtils.hashCodeToRGBA(color);
        Matrix4f matrix = BThackMatrix.peek().getPositionMatrix();
        BThackRenderUtils.resetShader();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        float[][] map = new float[][]{new float[]{x2 - radius, y2 - radius, radius}, new float[]{x2 - radius, y1 + radius, radius}, new float[]{x1 + radius, y1 + radius, radius}, new float[]{x1 + radius, y2 - radius, radius}};
        for (int i = 0; i < 4; i++) {
            float[] current = map[i];
            double rad = current[2];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / 10f)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, current[0] + sin, current[1] + cos, 0.0F).color(rgba[0], rgba[1], rgba[2], rgba[3]);
            }
            float rad1 = (float) Math.toRadians((360 / 4d + i * 90d));
            float sin = (float) (Math.sin(rad1) * rad);
            float cos = (float) (Math.cos(rad1) * rad);
            bufferBuilder.vertex(matrix, current[0] + sin, current[1] + cos, 0.0F).color(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
        BThackRenderUtils.drawNoReset(bufferBuilder.end());
    }

    public static void drawRoundedRectWithOutline(float x1, float y1, float x2, float y2, float radius, int color, int outlineColor, float depth) {
        BufferBuilder buffer = BThackRenderUtils.prepareToDraw(() -> Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.shader.getProgram());
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        Matrix4f matrix4f = BThackMatrix.peek().getPositionMatrix();
        Vector3f startPos = matrix4f.transformPosition(x1, y1, 0, new Vector3f());
        Vector3f endPos = matrix4f.transformPosition(x2, y2, 0, new Vector3f());

        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("resolution", (float) mc.getWindow().getWidth(), (float) mc.getWindow().getHeight());
        float scale = mc.getWindow().getScaledHeight() / (float) mc.getWindow().getHeight();
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("position", startPos.x / scale, startPos.y / scale);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("size", (endPos.x - startPos.x) / scale, (endPos.y - startPos.y) / scale);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("radius", radius / scale);
        float[] rgba1 = ColorUtils.hashCodeToRGBA(color);
        float[] rgba2 = ColorUtils.hashCodeToRGBA(outlineColor);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("color", rgba1[0], rgba1[1], rgba1[2], rgba1[3]);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("outlineColor", rgba2[0], rgba2[1], rgba2[2], rgba2[3]);
        Shaders.INSTANCE.ROUNDED_RECT_WITH_OUTLINE.setUniformValue("depth", depth / scale);

        buffer.vertex(matrix4f, x1 - 1, y1 - 1, 0).next();
        buffer.vertex(matrix4f, x1 - 1, y2 + 1, 0).next();
        buffer.vertex(matrix4f, x2 + 1, y2 + 1, 0).next();
        buffer.vertex(matrix4f, x2 + 1, y1 - 1, 0).next();

        draw();
    }

    public static void drawGradientRoundedRectWithOutline(float x1, float y1, float x2, float y2, float radius, int color, int outlineColor1, int outlineColor2, float depth, float scale, float speed) {
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.use();
        BufferBuilder buffer = BThackRenderUtils.prepareToDraw();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        Matrix4f matrix4f = BThackMatrix.peek().getPositionMatrix();
        Vector3f startPos = matrix4f.transformPosition(x1, y1, 0, new Vector3f());
        Vector3f endPos = matrix4f.transformPosition(x2, y2, 0, new Vector3f());

        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("resolution", (float) mc.getWindow().getWidth(), (float) mc.getWindow().getHeight());
        float _scale = mc.getWindow().getScaledHeight() / (float) mc.getWindow().getHeight();
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("position", startPos.x / _scale, startPos.y / _scale);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("size", (endPos.x - startPos.x) / _scale, (endPos.y - startPos.y) / _scale);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("radius", radius / _scale);
        float[] rgba1 = ColorUtils.hashCodeToRGBA(color);
        float[] rgba2 = ColorUtils.hashCodeToRGBA(outlineColor1);
        float[] rgba3 = ColorUtils.hashCodeToRGBA(outlineColor2);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("color", rgba1[0], rgba1[1], rgba1[2], rgba1[3]);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("outlineColor1", rgba2[0], rgba2[1], rgba2[2], rgba2[3]);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("outlineColor2", rgba3[0], rgba3[1], rgba3[2], rgba3[3]);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("depth", depth / _scale);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("scale", scale);
        Shaders.INSTANCE.XY_GRADIENT_ROUNDED_RECT_WITH_OUTLINE.setUniformValue("speed", speed);

        buffer.vertex(matrix4f, x1 - 1, y1 - 1, 0).next();
        buffer.vertex(matrix4f, x1 - 1, y2 + 1, 0).next();
        buffer.vertex(matrix4f, x2 + 1, y2 + 1, 0).next();
        buffer.vertex(matrix4f, x2 + 1, y1 - 1, 0).next();

        draw();
    }

    public static void drawVerticalGradientRect(float x1, float y1, float x2, float y2, int startColor, int endColor) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        float[] startC = hashCodeToRGBA(startColor);
        float[] endC = hashCodeToRGBA(endColor);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, x1, y1, 0).color(startC[0], startC[1], startC[2], startC[3]).next();
        buffer.vertex(matrix4f, x1, y2, 0).color(endC[0], endC[1], endC[2], endC[3]).next();
        buffer.vertex(matrix4f, x2, y2, 0).color(endC[0], endC[1], endC[2], endC[3]).next();
        buffer.vertex(matrix4f, x2, y1, 0).color(startC[0], startC[1], startC[2], startC[3]).next();

        draw();
    }

    public static void drawHorizontalGradientRect(float x1, float y1, float x2, float y2, int startColor, int endColor) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        float[] startC = hashCodeToRGBA(startColor);
        float[] endC = hashCodeToRGBA(endColor);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, x1, y1, 0).color(startC[0], startC[1], startC[2], startC[3]).next();
        buffer.vertex(matrix4f, x1, y2, 0).color(startC[0], startC[1], startC[2], startC[3]).next();
        buffer.vertex(matrix4f, x2, y2, 0).color(endC[0], endC[1], endC[2], endC[3]).next();
        buffer.vertex(matrix4f,  x2, y1, 0).color(endC[0], endC[1], endC[2], endC[3]).next();

        draw();
    }

    public static void drawVerticalGradientOutlineRect(float x1, float y1, float x2, float y2, float depth, int upColor, int downColor) {
        drawRect(x1 + depth, y1, x2 - depth, y1 + depth, upColor); //up
        drawRect(x1 + depth, y2 - depth, x2, y2, downColor); //down
        drawVerticalGradientRect(x1,y1, x1 + depth, y2, upColor, downColor);
        drawVerticalGradientRect(x2 - depth, y1, x2, y2 - depth, upColor, downColor);
    }

    public static void draw4ColorRect(float x1, float y1, float x2, float y2, int x1y1Color, int x2y1Color, int x1y2Color, int x2y2Color) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        float[] x1y1C = hashCodeToRGBA(x1y1Color);
        float[] x2y1C = hashCodeToRGBA(x2y1Color);
        float[] x1y2C = hashCodeToRGBA(x1y2Color);
        float[] x2y2C = hashCodeToRGBA(x2y2Color);

        BufferBuilder buffer = prepareToDraw(GameRenderer::getPositionColorProgram);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix4f, x1, y1, 0).color(x1y1C[0], x1y1C[1], x1y1C[2], x1y1C[3]).next();
        buffer.vertex(matrix4f, x1, y2, 0).color(x1y2C[0], x1y2C[1], x1y2C[2], x1y2C[3]).next();
        buffer.vertex(matrix4f, x2, y2, 0).color(x2y2C[0], x2y2C[1], x2y2C[2], x2y2C[3]).next();
        buffer.vertex(matrix4f, x2, y1, 0).color(x2y1C[0], x2y1C[1], x2y1C[2], x2y1C[3]).next();

        draw();
    }

    public static void drawHorizontalRainbowRect(float x1, float y1, float x2, float y2, int rainbowType) {
        float counter = 1;
        float dX;
        float tX = x1;
        int delay = (int) RainbowUtils.getRainbowRectSpeed(rainbowType)[1];
        float speed = RainbowUtils.getRainbowRectSpeed(rainbowType)[0];

        float fX;

        if (x1 < x2) {
            fX = x2 - x1;
            fX /= 45;
        } else {
            fX = x1 - x2;
            fX = -(fX / 45);
        }
        dX = fX != 0 ? (int) Math.ceil(fX) : 0;

        Tessellator tessellator = Tessellator.getInstance();
        Shaders.INSTANCE.POSITION.use();
        applyBlend();
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();
        BufferBuilder buffer = tessellator.getBuffer();

        while (tX != x2) {
            if (x1 < x2) {
                if (tX + dX > x2) {
                    dX = x2 - tX;
                }
            } else {
                if (tX + dX < x2) {
                    dX = tX - x2;
                }
            }

            int color = ColorUtils.rainbow((int)(counter * delay), speed);
            float[] c = hashCodeToRGBA(color);

            Shaders.INSTANCE.POSITION.setUniformValue("color", c[0], c[1], c[2], c[3]);
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

            buffer.vertex(matrix4f, tX, y1, 0).next();
            buffer.vertex(matrix4f, tX, y2, 0).next();
            buffer.vertex(matrix4f, tX + dX, y2, 0).next();
            buffer.vertex(matrix4f, tX + dX, y1, 0).next();

            drawNoReset();

            tX += dX;
            counter++;
        }
    }

    public static void drawOutlineRect(float x1, float y1, float x2, float y2, float depth, int color) {
        float outlineX = x1 > x2 ? -depth : depth;
        float outlineY = y1 > y2 ? depth : -depth;

        drawRect(x1,y1, x1 + outlineX, y2, color);
        drawRect(x1 + outlineX, y2, x2, y2 + outlineY, color);
        drawRect(x2, y2 + outlineY, x2 - outlineX, y1, color);
        drawRect(x2 - outlineX, y1, x1 + outlineX, y1 - outlineY, color);
    }

    public static void drawShaderOutlineRect(ShaderProgram shaderProgram, float x1, float y1, float x2, float y2, float depth) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        guiGraphics.getMatrices().push();
        shaderProgram.use();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        drawShaderOutlineRect(x1,y1, x1 + depth, y2, buffer); //left
        drawShaderOutlineRect(x1 + depth, y2 - depth, x2, y2, buffer); //down
        drawShaderOutlineRect(x2, y2 - depth, x2 - depth, y1, buffer); //right
        drawShaderOutlineRect(x1 + depth, y1, x2 - depth, y1 + depth, buffer); //up

        draw();
        shaderProgram.release();
        guiGraphics.getMatrices().pop();
    }

    public static void drawShaderOutlineRect(float x1, float y1, float x2, float y2, BufferBuilder buffer) {
        Matrix4f matrix4f = BThackMatrix.peek().getPositionMatrix();
        buffer.vertex(matrix4f, x1, y1, 0).next();
        buffer.vertex(matrix4f, x1, y2, 0).next();
        buffer.vertex(matrix4f, x2, y2, 0).next();
        buffer.vertex(matrix4f, x2, y1, 0).next();
    }

    public static void drawSquare(float x1, float y1, float size, int color) {
        drawRect(x1 - size, y1 - size, x1 + size, y1 + size, color);
    }


    public static void drawTriangle(float x, float y, float size, float theta, int color) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        double radians = Math.toRadians(theta);

        float xA = -size;
        double newXA = xA * Math.cos(radians) + size * Math.sin(radians);
        double newYA = size * Math.cos(radians) - xA * Math.sin(radians);

        float xB = 0;
        float yB = -(size * 2);
        double newXB = xB * Math.cos(radians) + yB * Math.sin(radians);
        double newYB = yB * Math.cos(radians) - xB * Math.sin(radians);

        double newXC = size * Math.cos(radians) + size * Math.sin(radians);
        double newYC = size * Math.cos(radians) - size * Math.sin(radians);

        float[] c = hashCodeToRGBA(color);

        BufferBuilder buffer = prepareToDraw();
        Shaders.INSTANCE.POSITION.use();
        Shaders.INSTANCE.POSITION.setUniformValue("color", c[0], c[1], c[2], c[3]);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        buffer.vertex(matrix4f, (float)(x + newXB), (float)(y + newYB), 0).next();
        buffer.vertex(matrix4f, (float)(x + newXA), (float)(y + newYA), 0).next();
        buffer.vertex(matrix4f, (float)(x + newXC), (float)(y + newYC), 0).next();
        buffer.vertex(matrix4f, (float)(x + newXB), (float)(y + newYB), 0).next();

        draw();
    }

    public static void drawString(String text, float x1, float y1, int color, boolean shadow, float size) {

        if (text == null || text.isEmpty()) return;

        guiGraphics.getMatrices().push();
        guiGraphics.getMatrices().scale(size, size, size);
        mc.textRenderer.draw(text, x1 * (1 / size), y1 * (1 / size), color, shadow, guiGraphics.getMatrices().peek().getPositionMatrix(), guiGraphics.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880, mc.textRenderer.isRightToLeft());
        guiGraphics.tryDraw();
        guiGraphics.getMatrices().pop();
        resetShader();
        //drawString(text, x1, y1, color, shadow, FontRenderManager.DrawMode.NORMAL_BOLD);
    }

    public static void drawString(String text, float x1, float y1, int color, boolean shadow) {
        drawString(text, x1, y1, color, shadow, 1);
    }

    public static void drawString(String text, float x1, float y1, int color) {
        drawString(text, x1, y1, color, true);
    }

    public static void drawCenteredString(String text, float x1, float y1, int color) {
        drawCenteredString(text, x1, y1, color, 1);
    }

    public static void drawCenteredString(String text, float x1, float y1, int color, float size) {
        drawString(text, (x1 - (mc.textRenderer.getWidth(text) / 2f)), y1, color, true, size);
    }

    /**
     * This is shit, don't use it please, use another renderer on my texture system.
     */
    @Deprecated
    public static void drawTextureRect(Identifier texture, float x1, float y1, float x2, float y2) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y2, 0.0f).texture(0, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y2, 0.0f).texture(1, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y1, 0.0f).texture(1, 0).next();
        bufferBuilder.vertex(matrix4f, x1, y1, 0.0f).texture(0, 0).next();
        draw();
    }

    public static void drawTextureRect(GlTex texture, float x1, float y1, float x2, float y2){
        BufferBuilder buffer;
        RenderSystem.setShaderTexture(0, texture.getTexId());
        buffer = BThackRenderUtils.prepareToDraw(GameRenderer::getPositionTexProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        Matrix4f matrix4f = BThackRender.guiGraphics.getMatrices().peek().getPositionMatrix();
        buffer.vertex(matrix4f, x1, y2, 0.0f).texture(0, 1).next();
        buffer.vertex(matrix4f, x2, y2, 0.0f).texture(1, 1).next();
        buffer.vertex(matrix4f, x2, y1, 0.0f).texture(1, 0).next();
        buffer.vertex(matrix4f, x1, y1, 0.0f).texture(0, 0).next();
        //buffer.end();
        draw();
        buffer = null;
    }

    public static void drawTextureRect(GLTexture texture, float x1, float y1, float x2, float y2) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        RenderSystem.setShaderTexture(0, texture.getTexId());
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y2, 0.0f).texture(0, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y2, 0.0f).texture(1, 1).next();
        bufferBuilder.vertex(matrix4f, x2, y1, 0.0f).texture(1, 0).next();
        bufferBuilder.vertex(matrix4f, x1, y1, 0.0f).texture(0, 0).next();
        draw();
    }

    /**
     * THE SHADER MUST HAVE VERTEXFORMAT = VERTEXFORMATS.POSITION!!!!
     */
    public static void drawShader(ShaderProgram shaderProgram, float x1, float y1, float x2, float y2) {
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();

        guiGraphics.getMatrices().push();
        shaderProgram.use();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix4f, x1, y2, 0.0f).next();
        bufferBuilder.vertex(matrix4f, x2, y2, 0.0f).next();
        bufferBuilder.vertex(matrix4f, x2, y1, 0.0f).next();
        bufferBuilder.vertex(matrix4f, x1, y1, 0.0f).next();
        draw();
        shaderProgram.release();
        guiGraphics.getMatrices().pop();
        //RenderSystem.setShader(GameRenderer::getPositionProgram);
    }

    public static void drawItem(DrawContext context, ItemStack stack, int x, int y, String amountText, boolean onSlot) {
        drawItem(context, stack, x, y, amountText, onSlot, 1);
    }

    public static void drawItem(DrawContext context, ItemStack stack, int x, int y, String amountText, boolean onSlot, float size) {
        context.getMatrices().push();
        //context.getMatrices().translate(0.0f, 0.0f, 232.0f);
        context.getMatrices().scale(size, size, 1);
        context.drawItem(stack, x, y);
        if (onSlot)
            context.drawItemInSlot(mc.textRenderer, stack, x, y, amountText);
        context.getMatrices().pop();
    }

    public static void enableScissor(int x, int y, int width, int height) {
        setScissor(scissorStack.push(new ScreenRect(x, y, width, height)));
    }

    public static void disableScissor() {
        setScissor(scissorStack.pop());
    }

    private static void setScissor(ScreenRect rect) {
        if (rect != null) {
            Window window = mc.getWindow();
            int i = window.getFramebufferHeight();
            double d = window.getScaleFactor();
            double e = (double) rect.getLeft() * d;
            double f = (double) i - (double) rect.getBottom() * d;
            double g = (double) rect.width() * d;
            double h = (double) rect.height() * d;
            RenderSystem.enableScissor((int) e, (int) f, Math.max(0, (int) g), Math.max(0, (int) h));
        } else {
            RenderSystem.disableScissor();
        }
    }

    public static void applyRegionalRenderOffset(MatrixStack matrixStack) {
        applyRegionalRenderOffset(matrixStack, getCameraRegion());
    }

    public static void applyRegionalRenderOffset(MatrixStack matrixStack, Chunk chunk) {
        applyRegionalRenderOffset(matrixStack, RegionPos.of(chunk.getPos()));
    }

    public static void applyRegionalRenderOffset(MatrixStack matrixStack, RegionPos region) {
        Vec3d offset = region.toVec3d().subtract(getCameraPos());
        matrixStack.translate(offset.x, offset.y, offset.z);
    }

    public static DrawContext getGuiGraphics() {
        return guiGraphics;
    }

    public static void drawHudPlate(float x1, float y1, float x2, float y2) {
        ModuleList.HUD.hudStyle.draw(x1, y1, x2, y2);
    }
}
