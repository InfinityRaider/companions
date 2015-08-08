package com.InfinityRaider.companions.renderers.renderhooks;

import com.InfinityRaider.companions.reference.Data;
import com.InfinityRaider.companions.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class PlayerEffectRenderer {
    private static int instanceCounter = 0;

    private final String name;
    private final int id;

    PlayerEffectRenderer() {
        id = instanceCounter;
        this.name = Reference.MOD_ID + ".title_" + Data.titles[getId()];
        instanceCounter++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return getName().substring(getName().indexOf('_')+1);
    }

    abstract void renderEffects(EntityPlayer player, RenderPlayer renderer, float tick);

    protected void rotateToGeneralCoordinates(EntityPlayer player, float partialTick) {
        float yaw = player.prevRenderYawOffset + (player.renderYawOffset-player.prevRenderYawOffset)*partialTick;
        GL11.glRotatef(-yaw, 0, 1, 0);
    }

    protected void drawAxisSystem() {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV(-0.005F, 2, 0, 1, 0);
        tessellator.addVertexWithUV(0.005F, 2, 0, 0, 0);
        tessellator.addVertexWithUV(0.005F, -1, 0, 0, 1);
        tessellator.addVertexWithUV(-0.005F, -1, 0, 1, 1);

        tessellator.addVertexWithUV(2, -0.005F, 0, 1, 0);
        tessellator.addVertexWithUV(2, 0.005F, 0, 0, 0);
        tessellator.addVertexWithUV(-1, 0.005F, 0, 0, 1);
        tessellator.addVertexWithUV(-1, -0.005F, 0, 1, 1);

        tessellator.addVertexWithUV(0, -0.005F, 2, 1, 0);
        tessellator.addVertexWithUV(0, 0.005F, 2, 0, 0);
        tessellator.addVertexWithUV(0, 0.005F, -1, 0, 1);
        tessellator.addVertexWithUV(0, -0.005F, -1, 1, 1);

        tessellator.draw();
    }
}
