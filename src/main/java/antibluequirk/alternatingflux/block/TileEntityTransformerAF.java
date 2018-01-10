package antibluequirk.alternatingflux.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import antibluequirk.alternatingflux.wire.AFWireType;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.IEProperties.PropertyBoolInverted;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDualState;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasDummyBlocks;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IMirrorAble;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IUsesBooleanProperty;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTransformer;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTransformerHV;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TileEntityTransformerAF extends TileEntityTransformerHV
{
	public WireType getSecondCable() { return super.getLimiter(1); }
	
	@Override
	protected boolean canTakeLV()
	{
		return false;
	}
	@Override
	protected boolean canTakeMV()
	{
		return false;
	}
	@Override
	protected boolean canTakeHV()
	{
		return true;
	}
	
	@Override
	protected float getLowerOffset() {
		return super.getHigherOffset();
	}

	@Override
	protected float getHigherOffset() {
		return .75F;
	}
	
	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target)
	{
		//if(cableType != WireType.STEEL && cableType != AFWireType.AF)
		//	return false;
		if(dummy!=0) {
			TileEntity master = world.getTileEntity(getPos().add(0, -dummy, 0));
			return master instanceof TileEntityTransformer && ((TileEntityTransformer) master).canConnectCable(cableType, target);
		}
		int tc = getTargetedConnector(target);
		switch(tc)
		{
		case 0:
			return canAttach(cableType, limitType, getSecondCable());
		case 1:
			return canAttach(cableType, getSecondCable(), limitType);
		}
		return false;
	}
	private boolean canAttach(WireType toAttach, WireType atConn, WireType other) {
		if (atConn!=null)
			return false;
		if (other==null)
			return true;
		if (other==toAttach)
			return false;
		WireType higher = AFWireType.AF;
		return toAttach==higher||other==higher;
	}
	
	@Override
	public boolean getIsMirrored()
	{
		if (onPost)
			return false;
		if (dummy!=0) {
			TileEntity master = world.getTileEntity(pos.down(dummy));
			return master instanceof TileEntityTransformer && ((TileEntityTransformer) master).getIsMirrored();
		}
		else
		{
			if (limitType==null&&getSecondCable()==null)
				return true;
			WireType higher = AFWireType.AF;
			boolean b = (limitType != null && higher.equals(limitType)) || (getSecondCable() != null && !higher.equals(getSecondCable()));
			return b;
		}
	}

}