package com.InfinityRaider.companions.proxy;

import com.InfinityRaider.companions.handler.ConfigurationHandler;
import com.InfinityRaider.companions.renderers.renderhooks.RenderPlayerHooks;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        RenderPlayerHooks renderPlayerHooks = new RenderPlayerHooks();
        MinecraftForge.EVENT_BUS.register(renderPlayerHooks);
    }

    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        super.initConfiguration(event);
        ConfigurationHandler.initClientConfigs(event);
    }
}
