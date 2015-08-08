package com.InfinityRaider.companions;

import com.InfinityRaider.companions.handler.ConfigurationHandler;
import com.InfinityRaider.companions.handler.DungeonLootHandler;
import com.InfinityRaider.companions.item.ItemCompanionTitle;
import com.InfinityRaider.companions.proxy.IProxy;
import com.InfinityRaider.companions.reference.Names;
import com.InfinityRaider.companions.reference.Reference;
import com.InfinityRaider.companions.utility.LogHelper;
import com.InfinityRaider.companions.utility.RegisterHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION,
        dependencies = "required-after:"+ Names.Mods.baubles+";after:"+Names.Mods.travellersGear
)
public class Companions {
    @Mod.Instance(Reference.MOD_ID)
    public static Companions instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS,serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static ItemCompanionTitle itemCompanionTitle;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        LogHelper.debug("Starting Pre-Initialization");
        proxy.initConfiguration(event);
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
        itemCompanionTitle = new ItemCompanionTitle();
        RegisterHelper.registerItem(itemCompanionTitle, "titleItem");
        LogHelper.debug("Pre-Initialization Complete");
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        LogHelper.debug("Starting Initialization");
        proxy.registerEventHandlers();
        DungeonLootHandler.registerDungeonLoot();
        LogHelper.debug("Initialization Complete");
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        LogHelper.debug("Starting Post-Initialization");
        LogHelper.debug("Post-Initialization Complete");
    }

    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {

    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
    }
}
