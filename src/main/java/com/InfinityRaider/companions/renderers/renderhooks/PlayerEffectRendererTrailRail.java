package com.InfinityRaider.companions.renderers.renderhooks;

import com.InfinityRaider.companions.utility.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class PlayerEffectRendererTrailRail extends PlayerEffectRendererTrail<ForgeDirection> {
    protected PlayerEffectRendererTrailRail() {
        super(2, 20);
    }

    @Override
    protected void draw(Tessellator tessellator, EntityPlayer player, int index) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        Block rail = Blocks.rail;

        GL11.glDisable(GL11.GL_LIGHTING);

        ForgeDirection prev = getObjectForIndex(index-1);
        ForgeDirection current = getObjectForIndex(index);
        ForgeDirection next = getObjectForIndex(index+1);
        next = next==null?getObjectForPlayer(player):next;
        prev = prev==null?current:prev;

        if(isStraight(prev, next)) {
            renderStraight(tessellator, rail.getIcon(0,1), prev);
        } else {
            renderBend(tessellator, rail.getIcon(0, 6), prev, next);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void onTick(EntityPlayer player) {

    }

    private void renderStraight(Tessellator tessellator, IIcon icon, ForgeDirection dir) {
        if(dir==ForgeDirection.EAST || dir==ForgeDirection.WEST) {
            rotateMatrix(90, false);
        }

        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(0, 0, 1, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(1, 0, 1, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(1, 0, 0, icon.getMaxU(), icon.getMaxV());

        tessellator.draw();

        if(dir==ForgeDirection.EAST || dir==ForgeDirection.WEST) {
            rotateMatrix(90, true);

        }
    }

    private void renderBend(Tessellator tessellator, IIcon icon, ForgeDirection from, ForgeDirection to) {
        int angle = getRotation(from, to);

        rotateMatrix(angle, false);
        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(0, 0, 1, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(1, 0, 1, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(1, 0, 0, icon.getMaxU(), icon.getMaxV());

        tessellator.draw();
        rotateMatrix(angle, true);
    }

    private boolean isStraight(ForgeDirection prev, ForgeDirection next) {
        if(prev==ForgeDirection.NORTH || prev==ForgeDirection.SOUTH) {
            return next==ForgeDirection.NORTH || next==ForgeDirection.SOUTH;
        }
        else if (prev==ForgeDirection.EAST || prev==ForgeDirection.WEST) {
            return next==ForgeDirection.EAST || next==ForgeDirection.WEST;
        }
        return false;
    }

    private int getRotation(ForgeDirection from, ForgeDirection to) {
        int angle;
        if(from==ForgeDirection.NORTH) {
            if(to==ForgeDirection.WEST) {
                angle = 0;
            }
            else {
                angle = 90;
            }
        }
        else if(from==ForgeDirection.EAST) {
            if(to==ForgeDirection.SOUTH) {
                angle = 0;
            }
            else {
                angle = 270;
            }
        }
        else if(from==ForgeDirection.SOUTH) {
            if(to==ForgeDirection.WEST) {
                angle = 270;
            }
            else {
                angle = 180;
            }
        }
        else {
            if(to==ForgeDirection.NORTH) {
                angle = 180;
            }
            else {
                angle = 90;
            }
        }
        return angle;
    }

    private void rotateMatrix(int angle, boolean inverse) {
        float dx = angle % 270 == 0 ? 0 : -1;
        float dz = angle > 90 ? -1 : 0;
        if (inverse) {
            GL11.glTranslatef(-dx, 0, -dz);
            GL11.glRotatef(-angle, 0, 1, 0);
        } else {
            GL11.glRotatef(angle, 0, 1, 0);
            GL11.glTranslatef(dx, 0, dz);
        }

    }

    @Override
    protected ForgeDirection getObjectForPlayer(EntityPlayer player) {
        double x = player.motionX;
        double z = player.motionZ;
        if(Math.abs(x)>=Math.abs(z)) {
            return x>0?ForgeDirection.WEST:ForgeDirection.EAST;
        } else {
            return z>0?ForgeDirection.NORTH:ForgeDirection.SOUTH;
        }
    }
}
