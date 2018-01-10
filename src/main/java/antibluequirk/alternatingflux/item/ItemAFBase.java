package antibluequirk.alternatingflux.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nonnull;
import antibluequirk.alternatingflux.AlternatingFlux;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.items.ItemIEBase;

public class ItemAFBase extends ItemIEBase {
	public ItemAFBase(String name, int stackSize, String... subNames) {
		super(name, stackSize, subNames);
		fixupItem(this);
	}

	//This function allows us to use IEBase classes, by fixing things up so they come from our mod.
    //It should be called right after the super call in any constructor of a class that derives from BlockIEBase
    //This is kind of hacky, but allows us to avoid copying a lot of code.
    @SuppressWarnings("rawtypes")
    public static void fixupItem(ItemIEBase item) {
    	//First, get the item out of IE's registries.
    	Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
    	if (rItem != item) throw new IllegalStateException("fixupItem was not called at the appropriate time");
    	
    	//Now, reconfigure the block to match our mod.
    	item.setUnlocalizedName(AlternatingFlux.MODID+"."+item.itemName);
    	item.setCreativeTab(AlternatingFlux.creativeTab);
    	
    	//And add it to our registries.
    	AlternatingFlux.items.add(item);
    }
}