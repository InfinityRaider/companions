package com.InfinityRaider.companions.renderers.renderhooks;

import com.InfinityRaider.companions.handler.ConfigurationHandler;
import com.InfinityRaider.companions.reference.Constants;
import com.InfinityRaider.companions.reference.Names;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import travellersgear.api.TravellersGearAPI;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public final class RenderPlayerHooks {
    private HashMap<Integer, PlayerEffectRenderer> renderers;
    private HashMap<String, Integer> ids;
    private static boolean hasInit = false;

    public RenderPlayerHooks() {
        if(!hasInit) {
            hasInit = true;
            renderers = new HashMap<Integer, PlayerEffectRenderer>();
            ids = new HashMap<String, Integer>();
            this.init();
        }
    }

    private void init() {
        this.registerPlayerEffectRenderer(new PlayerEffectRendererNavi());
        this.registerPlayerEffectRenderer(new PlayerEffectRendererParticlesEnchanted());
        this.registerPlayerEffectRenderer(new PlayerEffectRendererEntityDragon());
        this.registerPlayerEffectRenderer(new PlayerEffectRendererEntityBat());
        this.registerPlayerEffectRenderer(new PlayerEffectRendererButterfly());
        this.registerPlayerEffectRenderer(new PlayerEffectRendererTrailSparkles());
        this.registerPlayerEffectRenderer(new PlayerEffectRendererTrailRail());
    }

    private void registerPlayerEffectRenderer(PlayerEffectRenderer renderer) {
        if(ConfigurationHandler.disableRenderEffect(renderer.getSimpleName())) {
            return;
        }
        renderers.put(renderer.getId(), renderer);
        ids.put(renderer.getName(), renderer.getId());
    }

    private PlayerEffectRenderer getRendererFromId(int id) {
        return renderers.get(id);
    }

    private PlayerEffectRenderer getRendererFromTitle(String title) {
        try {
            return getRendererFromId(ids.get(title));
        } catch(Exception e) {
            return null;
        }
    }

    @SubscribeEvent
    public void RenderPlayerEffects(RenderPlayerEvent.Specials.Post event) {
        ItemStack titleStack = TravellersGearAPI.getExtendedInventory(event.entityPlayer)[Constants.SLOT_TITLE_ID];
        if(titleStack==null || titleStack.getItem()==null) {
            return;
        }
        String title = (titleStack.hasTagCompound() && titleStack.stackTagCompound.hasKey(Names.NBT.title))?titleStack.stackTagCompound.getString(Names.NBT.title):null;
        if(title == null) {
            return;
        }
        PlayerEffectRenderer renderer = getRendererFromTitle(title);
        if(renderer == null) {
            return;
        }
        renderer.renderEffects(event.entityPlayer, event.renderer, event.partialRenderTick);
    }
}
