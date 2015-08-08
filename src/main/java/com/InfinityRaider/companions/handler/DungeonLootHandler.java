package com.InfinityRaider.companions.handler;

import com.InfinityRaider.companions.Companions;
import com.InfinityRaider.companions.reference.Data;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DungeonLootHandler {
    public static void registerDungeonLoot() {
        ArrayList<ItemStack> list = Companions.itemCompanionTitle.getSubItems();
        Field[] fields = ChestGenHooks.class.getFields();
        for(Field field:fields) {
            if(field.getType()!=String.class) {
                continue;
            }
            String type;
            ChestGenHooks chestGen;
            try {
                type = (String) field.get(null);
                chestGen = ChestGenHooks.getInfo(type);
            } catch(Exception e) {
                continue;
            }
            if(chestGen == null) {
                continue;
            }
            for (int i = 0; i < Data.titles.length; i++) {
                ItemStack stack = list.get(i);
                int weight = ConfigurationHandler.getDungeonLootWeight(type, Data.titles[i]);
                if (weight > 0) {
                    chestGen.addItem(new WeightedRandomChestContent(stack, weight, 1, 1));
                }
            }
        }
    }
}
