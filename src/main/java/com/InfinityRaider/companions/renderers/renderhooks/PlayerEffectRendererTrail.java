package com.InfinityRaider.companions.renderers.renderhooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public abstract class PlayerEffectRendererTrail extends PlayerEffectRenderer {
    private ArrayList<int[]> trail = new ArrayList<int[]>();
    private int tickCounter = 0;
    private int idleTimer = 0;

    private final int updateInterval;
    private final int maxSize;

    protected PlayerEffectRendererTrail(int updateInterval, int maxSize) {
        super();
        this.updateInterval = updateInterval;
        this.maxSize = maxSize;
    }

    @Override
    void renderEffects(EntityPlayer player, RenderPlayer renderer, float partialTick) {
        GL11.glPushMatrix();

        updateTrail(player);
        rotateToGeneralCoordinates(player, partialTick);
        translateToGeneralCoordinates(player, partialTick);

        for (int i = 0; i < trail.size(); i++) {
            int[] coords = trail.get(i);
            float dy = getVerticalOffset(player);
            GL11.glTranslatef(coords[0] - 0.5F, coords[1] + dy, coords[2] - 0.5F);
            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1, 1, 1, ((float) i) / ((float) trail.size()));

            draw(Tessellator.instance);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
            GL11.glTranslatef(-coords[0]+0.5F, -coords[1] - dy, -coords[2]+0.5F);
        }

        GL11.glPopMatrix();
    }

    protected abstract void draw(Tessellator tessellator);

    private void updateTrail(EntityPlayer player) {
        tickCounter = (tickCounter + 1) % updateInterval;
        int maxIdleTime = maxSize;
        if (tickCounter != 0) {
            return;
        }
        int[] lastCoords = trail.size()==0?null:trail.get(trail.size() - 1);
        int[] current = new int[] {(int) player.posX, (int) player.posY-1, (int) player.posZ};
        if (isOnGround(player) && (lastCoords==null || current[0]!=lastCoords[0] || current[1]!=lastCoords[1] || current[2]!=lastCoords[2])) {
            trail.add(current);
            idleTimer = 0;
        } else {
            if(trail.size()==0) {
                idleTimer = 0;
            } else {
                idleTimer = idleTimer + (idleTimer >= maxIdleTime ? 0 : 1);
            }
        }
        if (idleTimer>=maxIdleTime || trail.size()>maxSize) {
            //trail.remove(0);
        }
    }
    private boolean isOnGround(EntityPlayer player) {
        if(player == Minecraft.getMinecraft().thePlayer) {
            return player.onGround;
        } else {
            return !player.worldObj.isAirBlock((int) player.posX, (int) player.posY-1, (int) player.posZ);
        }
    }
}
