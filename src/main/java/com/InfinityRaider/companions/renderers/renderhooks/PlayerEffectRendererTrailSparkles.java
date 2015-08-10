package com.InfinityRaider.companions.renderers.renderhooks;

import com.InfinityRaider.companions.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class PlayerEffectRendererTrailSparkles extends PlayerEffectRendererTrail {
    private static int trailSize = 20;
    private int counter = 0;
    private int subCounter = 0;
    private final ResourceLocation texture;

    PlayerEffectRendererTrailSparkles() {
        super(5, trailSize);
        this.texture = new ResourceLocation(Reference.MOD_ID, "textures/entities/player/trail/sparkles.png");
    }

    @Override
    protected void draw(Tessellator tessellator) {
        int discr = 20;
        int period = 5;
        subCounter = (subCounter+1)%trailSize;
        if(subCounter==0) {
            counter = (counter + 1) % (period * discr);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        tessellator.startDrawingQuads();

        float vMin = (1.00F/((float) period))*(counter%discr);
        float vMax = vMin + 0.20F;

        tessellator.addVertexWithUV(0, 0, 0, 0, vMax);
        tessellator.addVertexWithUV(1, 0, 0, 1, vMax);
        tessellator.addVertexWithUV(1, 0, 1, 1, vMin);
        tessellator.addVertexWithUV(0, 0, 1, 0, vMin);

        tessellator.draw();
    }
}
