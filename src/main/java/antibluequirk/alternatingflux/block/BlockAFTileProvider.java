package antibluequirk.alternatingflux.block;

import antibluequirk.alternatingflux.AlternatingFlux;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.BlockIETileProvider;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

abstract class BlockAFTileProvider<E extends Enum<E> & BlockIEBase.IBlockEnum> extends BlockIETileProvider<E>
{
	public BlockAFTileProvider(String name, Material material, PropertyEnum<E> mainProperty, Class<? extends ItemBlockIEBase> itemBlock, Object... additionalProperties) {
		super(name, material, mainProperty, itemBlock, additionalProperties);
		BlockAFBase.fixupBlock(this, itemBlock);
	}

	@Override
	public String createRegistryName()
	{
		return AlternatingFlux.MODID+":"+name;
	}
}
