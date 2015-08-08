package com.InfinityRaider.companions.item;

import com.InfinityRaider.companions.reference.Constants;
import com.InfinityRaider.companions.reference.Data;
import com.InfinityRaider.companions.reference.Reference;
import com.InfinityRaider.companions.utility.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import travellersgear.TravellersGear;
import travellersgear.api.ITravellersGear;

import java.util.ArrayList;
import java.util.List;

public class ItemCompanionTitle extends Item implements ITravellersGear {
    public ItemCompanionTitle() {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(TravellersGear.creativeTab);
        this.setMaxStackSize(1);
    }

    @Override
    public int getSlot(ItemStack itemStack) {
        return Constants.SLOT_TITLE_ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.addAll(getSubItems());
    }

    public ArrayList<ItemStack> getSubItems() {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        for (String title : Data.titles) {
            ItemStack stack = new ItemStack(this);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("title", Reference.MOD_ID + ".title_" + title);
            list.add(stack);
        }
        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        list.add(StatCollector.translateToLocalFormatted("TG.desc.gearSlot.tg." + Constants.SLOT_TITLE_ID));
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("title"))
            list.add(StatCollector.translateToLocal(stack.getTagCompound().getString("title")));
            list.add(StatCollector.translateToLocal(Reference.MOD_ID+".tooltip_titleItem"));
    }

    @Override
    public void onTravelGearTick(EntityPlayer entityPlayer, ItemStack itemStack) {

    }

    @Override
    public void onTravelGearEquip(EntityPlayer entityPlayer, ItemStack itemStack) {

    }

    @Override
    public void onTravelGearUnequip(EntityPlayer entityPlayer, ItemStack itemStack) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        LogHelper.debug("registering icon for: " + this.getUnlocalizedName());
        itemIcon = reg.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf('.')+1));
    }
}
