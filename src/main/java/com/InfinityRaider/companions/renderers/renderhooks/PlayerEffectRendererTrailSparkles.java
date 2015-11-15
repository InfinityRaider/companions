package com.InfinityRaider.companions.renderers.renderhooks;

import com.InfinityRaider.companions.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class PlayerEffectRendererTrailSparkles extends PlayerEffectRendererTrail {
    private final int frames = 5;

    private int counter = 0;
    private int offset = 0;

    private final ResourceLocation texture;

    PlayerEffectRendererTrailSparkles() {
        super(5, 20);
        this.texture = new ResourceLocation(Reference.MOD_ID, "textures/entities/player/trail/sparkles.png");
    }

    @Override
    protected void draw(Tessellator tessellator, EntityPlayer player, int index) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glDisable(GL11.GL_LIGHTING);
        tessellator.startDrawingQuads();

        float vMin = (1.00F/((float) frames))*((offset+index)%frames);
        float vMax = vMin + 0.20F;

        tessellator.addVertexWithUV(0, 0, 0, 0, vMax);
        tessellator.addVertexWithUV(0, 0, 1, 0, vMin);
        tessellator.addVertexWithUV(1, 0, 1, 1, vMin);
        tessellator.addVertexWithUV(1, 0, 0, 1, vMax);

        tessellator.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void onTick(EntityPlayer player) {
        int period = 4;
        counter=(counter+1)%period;
        if(counter == 0) {
            offset = (offset+1)%frames;
        }
    }

    @Override
    protected Object getObjectForPlayer(EntityPlayer player) {
        return null;
    }
}
