package com.InfinityRaider.companions.proxy;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
    void registerEventHandlers();

    void initConfiguration(FMLPreInitializationEvent event);
}
