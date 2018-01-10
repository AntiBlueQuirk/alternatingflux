package antibluequirk.alternatingflux.block;

import java.util.Locale;

import net.minecraft.util.IStringSerializable;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;

public enum BlockTypes_Connector implements IStringSerializable, BlockIEBase.IBlockEnum
{
	RELAY_AF,
	TRANSFORMER_AF;

	@Override
	public String getName() { return this.toString().toLowerCase(Locale.ENGLISH); }
	
	@Override
	public int getMeta() { return ordinal(); }
	
	@Override
	public boolean listForCreative() { return true; }
}
