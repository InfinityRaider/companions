package com.InfinityRaider.companions.proxy;

import com.InfinityRaider.companions.handler.ConfigurationHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public abstract class CommonProxy implements IProxy {
    @Override
    public void registerEventHandlers() {

    }

    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event);
    }
}
