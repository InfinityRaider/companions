package com.InfinityRaider.companions.renderers.renderhooks;

import com.InfinityRaider.companions.reference.Data;
import com.InfinityRaider.companions.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
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

    public float getVerticalOffset(EntityPlayer player) {
        if(player == Minecraft.getMinecraft().thePlayer) {
            return  0.15F;
        } else {
            return player.isSneaking()?-0.35F:-0.5F;
        }
    }

    abstract void renderEffects(EntityPlayer player, RenderPlayer renderer, float partialTick);

    protected void translateToGeneralCoordinates(EntityPlayer player, float partialTick) {
        double x = player.prevPosX + (player.posX - player.prevPosX)*partialTick;
        double y = player.prevPosY + (player.posY - player.prevPosY)*partialTick;
        double z = player.prevPosZ + (player.posZ - player.prevPosZ)*partialTick;
        GL11.glTranslated(-x, -y, -z);
    }

    protected void rotateToGeneralCoordinates(EntityPlayer player, float partialTick) {
        float yaw = player.prevRenderYawOffset + (player.renderYawOffset-player.prevRenderYawOffset)*partialTick;
        GL11.glRotatef(-yaw, 0, 1, 0);
        GL11.glRotatef(180, 1, 0, 0);
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
