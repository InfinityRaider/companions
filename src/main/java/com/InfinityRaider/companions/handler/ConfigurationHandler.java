package com.InfinityRaider.companions.handler;

import com.InfinityRaider.companions.reference.Reference;
import com.InfinityRaider.companions.utility.LogHelper;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
    public static final String CATEGORY_DEBUG = "Debug";
    public static final String CATEGORY_DUNGEON_LOOT = "Dungeon Loot";

    public static Configuration config;

    //debug
    public static boolean debug;

    public static void init(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
            loadConfiguration();
        }

        LogHelper.debug("Configuration Loaded");
    }

    @SideOnly(Side.CLIENT)
    public static void initClientConfigs(FMLPreInitializationEvent event) {

    }

    @SideOnly(Side.CLIENT)
    public static boolean disableRenderEffect(String effect) {
        boolean flag = config.getBoolean(effect, "Client config options", false, "Set this to true to disable the rendering effects for "+effect+" on this client");
        if(config.hasChanged()) {
            config.save();
        }
        return flag;
    }

    //read values from the config
    private static void loadConfiguration() {
        //debug mode
        debug = config.getBoolean("debug",CATEGORY_DEBUG,false,"Set to true if you wish to enable debug mode");

        if(config.hasChanged()) {config.save();}
    }

    public static int getDungeonLootWeight(String lootType, String titleId) {
        int weight = config.getInt(titleId, CATEGORY_DUNGEON_LOOT + ": " + lootType, 5, 0, 100, "Dungeon loot weight for title scroll: " + titleId + " (0 disables dungeon loot for this title");
        if(config.hasChanged()) {
            config.save();
        }
        return weight;
    }


    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(Reference.MOD_ID)) {
            loadConfiguration();
            LogHelper.debug("Configuration reloaded.");
        }
    }
}
