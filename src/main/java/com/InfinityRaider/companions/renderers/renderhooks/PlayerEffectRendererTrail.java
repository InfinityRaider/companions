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
public abstract class PlayerEffectRendererTrail<T> extends PlayerEffectRenderer {
    private ArrayList<TrailComponent<T>> trail = new ArrayList<TrailComponent<T>>();
    private int tickCounter = 0;
    private int idleTimer = 0;

    private final int updateInterval;
    private final int maxSize;

    protected PlayerEffectRendererTrail(int updateInterval, int maxSize) {
        super();
        this.updateInterval = updateInterval;
        this.maxSize = maxSize;
    }

    protected T getObjectForIndex(int i) {
        return isWithinBounds(i)?trail.get(i).getComponent():null;
    }

    protected int[] getCoordinatesForIndex(int i) {
        return isWithinBounds(i)?trail.get(i).getCoords():null;
    }

    protected TrailComponent<T> getTrailComponentForIndex(int i) {
        return isWithinBounds(i)?trail.get(i):null;
    }

    private boolean isWithinBounds(int i) {
        return i>=0 && i<trail.size();
    }

    @Override
    void renderEffects(EntityPlayer player, RenderPlayer renderer, float partialTick) {
        GL11.glPushMatrix();

        updateTrail(player, partialTick);
        onTick(player);
        rotateToGeneralCoordinates(player, partialTick);
        translateToGeneralCoordinates(player, partialTick);

        for (int i = 0; i < trail.size(); i++) {
            int[] coords = trail.get(i).coords;
            float dy = getVerticalOffset(player);
            GL11.glTranslatef(coords[0], coords[1] + dy, coords[2]);
            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor4f(1, 1, 1, ((float) i) / ((float) trail.size()));

            draw(Tessellator.instance, player, i);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
            GL11.glTranslatef(-coords[0], -coords[1] - dy, -coords[2]);
        }

        GL11.glPopMatrix();
    }

    protected abstract void draw(Tessellator tessellator, EntityPlayer player, int index);

    private void updateTrail(EntityPlayer player, float partialTick) {
        tickCounter = (tickCounter + 1) % updateInterval;
        int maxIdleTime = maxSize;
        if (tickCounter != 0) {
            return;
        }
        int[] lastCoords = trail.size()==0?null:trail.get(trail.size() - 1).getCoords();
        int[] current = getCurrentCoordinates(player, partialTick);
        if (isOnGround(player) && (lastCoords==null || current[0]!=lastCoords[0] || current[1]!=lastCoords[1] || current[2]!=lastCoords[2])) {
            trail.add(new TrailComponent<T>(current, getObjectForPlayer(player)));
            idleTimer = 0;
        } else {
            if(trail.size()==0) {
                idleTimer = 0;
            } else {
                idleTimer = idleTimer + (idleTimer >= maxIdleTime ? 0 : 1);
            }
        }
        if (idleTimer>=maxIdleTime || trail.size()>maxSize) {
            trail.remove(0);
        }
    }

    private int[] getCurrentCoordinates(EntityPlayer player, float partialTick) {
        return new int[] {
                (int) (player.prevPosX + (player.posX - player.prevPosX)*partialTick - 1),
                (int) (player.prevPosY + (player.posY - player.prevPosY)*partialTick - 1),
                (int) (player.prevPosZ + (player.posZ - player.prevPosZ)*partialTick)
        };
    }

    private boolean isOnGround(EntityPlayer player) {
        if(player == Minecraft.getMinecraft().thePlayer) {
            return player.onGround;
        } else {
            return !player.worldObj.isAirBlock((int) player.posX, (int) player.posY-1, (int) player.posZ);
        }
    }

    protected abstract void onTick(EntityPlayer player);

    protected abstract T getObjectForPlayer(EntityPlayer player);

    protected static class TrailComponent<T> {
        private int[] coords;
        private T obj;

        public TrailComponent(int[] coords, T obj) {
            this(coords[0], coords[1], coords[2], obj);
        }

        public TrailComponent(int x, int y, int z, T obj) {
            this.coords = new int[] {x, y, z};
            this.obj = obj;
        }

        public int[] getCoords() {
            return coords;
        }

        public int x() {
            return coords[0];
        }

        public int y() {
            return coords[1];
        }

        public int z() {
            return coords[2];
        }

        public T getComponent() {
            return obj;
        }
    }
}
