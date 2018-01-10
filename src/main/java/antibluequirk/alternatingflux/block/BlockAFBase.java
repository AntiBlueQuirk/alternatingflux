package antibluequirk.alternatingflux.block;

import antibluequirk.alternatingflux.AlternatingFlux;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.Item;

public class BlockAFBase <E extends Enum<E> & BlockIEBase.IBlockEnum> extends BlockIEBase<E> {
	public BlockAFBase(String name, Material material, PropertyEnum<E> mainProperty, Class<? extends ItemBlockIEBase> itemBlock, Object... additionalProperties)
	{
		super(name, material, mainProperty, itemBlock, additionalProperties);
		fixupBlock(this, itemBlock);
	}

	@Override
	public String createRegistryName()
	{
		return AlternatingFlux.MODID+":"+name;
	}
	
	//This function allows us to use BlockIEBase class, by fixing things up so they come from our mod.
    //It should be called right after the super call in any constructor of a class that derives from BlockIEBase
    //This is kind of hacky, but allows us to avoid copying a lot of code.
    @SuppressWarnings("rawtypes")
    public static void fixupBlock(BlockIEBase block, Class<? extends ItemBlockIEBase> itemBlock) {
    	//First, get the block out of IE's registries.
    	Block rBlock = IEContent.registeredIEBlocks.remove(IEContent.registeredIEBlocks.size()-1);
    	if (rBlock != block) throw new IllegalStateException("fixupBlock was not called at the appropriate time, removed block did not match");
    	
    	Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
    	if (rItem.getClass() != itemBlock) throw new IllegalStateException("fixupBlock was not called at the appropriate time");
    	
    	//Now, reconfigure the block to match our mod.
    	block.setCreativeTab(AlternatingFlux.creativeTab);
    	
    	//And add it to our registries.
    	AlternatingFlux.blocks.add(block);
    	try{
    		AlternatingFlux.items.add(itemBlock.getConstructor(Block.class).newInstance(block));
    	}catch(Exception e){e.printStackTrace();}
    }
}
