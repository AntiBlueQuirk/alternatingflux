package antibluequirk.alternatingflux.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import antibluequirk.alternatingflux.wire.AFWireType;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.AbstractConnection;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IEForgeEnergyWrapper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEInternalFluxHandler;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityRelayAF extends TileEntityImmersiveConnectable
		implements ITickable, IDirectionalTile, IBlockBounds, IOBJModelCallback<IBlockState> {
	public EnumFacing	facing					= EnumFacing.DOWN;

	boolean firstTick = true;

	@Override
	public void update() {
		if (!world.isRemote) {
			
		} else if (firstTick) {
			Set<Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(world, pos);
			if (conns != null)
				for (Connection conn : conns)
					if (pos.compareTo(conn.end) < 0 && world.isBlockLoaded(conn.end))
						this.markContainingBlockForUpdate(null);
			firstTick = false;
		}
	}

	@Override
	public EnumFacing getFacing() {
		return this.facing;
	}

	@Override
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}

	@Override
	public int getFacingLimitation() {
		return 0;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer) {
		return true;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity) {
		return false;
	}
	
	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target)
	{
		if(cableType!=AFWireType.AF)
			return false;
		return limitType==null || limitType==cableType;
	}
	
	@Override
	public boolean canRotate(EnumFacing axis) {
		return false;
	}

	@Override
	public boolean isEnergyOutput() {
		return false;
	}

	@Override
	public int outputEnergy(int amount, boolean simulate, int energyType) {
		return 0;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
		super.writeCustomNBT(nbt, descPacket);
		nbt.setInteger("facing", facing.ordinal());
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
		super.readCustomNBT(nbt, descPacket);
		facing = EnumFacing.getFront(nbt.getInteger("facing"));
	}

	@Override
	public Vec3d getRaytraceOffset(IImmersiveConnectable link)
	{
		EnumFacing side = facing.getOpposite();
		return new Vec3d(.5+side.getFrontOffsetX()*.4375, .5+side.getFrontOffsetY()*.4375, .5+side.getFrontOffsetZ()*.4375);
	}
	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		EnumFacing side = facing.getOpposite();
		double conRadius = con.cableType.getRenderDiameter()/2;
		return new Vec3d(.5+side.getFrontOffsetX()*(.375-conRadius), .5+side.getFrontOffsetY()*(.375-conRadius), .5+side.getFrontOffsetZ()*(.375-conRadius));
	}

	@SideOnly(Side.CLIENT)
	private AxisAlignedBB renderAABB;

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		int inc = getRenderRadiusIncrease();
		return new AxisAlignedBB(this.pos.getX() - inc, this.pos.getY() - inc, this.pos.getZ() - inc,
				this.pos.getX() + inc + 1, this.pos.getY() + inc + 1, this.pos.getZ() + inc + 1);
	}

	int getRenderRadiusIncrease() {
		return AFWireType.AF.getMaxLength();
	}

	@Override
	public float[] getBlockBounds() {
		float length = .875f;
		float wMin = .3125f;
		float wMax = .6875f;
		switch (facing.getOpposite()) {
		case UP:
			return new float[] { wMin, 0, wMin, wMax, length, wMax };
		case DOWN:
			return new float[] { wMin, 1 - length, wMin, wMax, 1, wMax };
		case SOUTH:
			return new float[] { wMin, wMin, 0, wMax, wMax, length };
		case NORTH:
			return new float[] { wMin, wMin, 1 - length, wMax, wMax, 1 };
		case EAST:
			return new float[] { 0, wMin, wMin, length, wMax, wMax };
		case WEST:
			return new float[] { 1 - length, wMin, wMin, 1, wMax, wMax };
		}
		return new float[] { 0, 0, 0, 1, 1, 1 };
	}
	
	@Override
	public boolean shouldRenderGroup(IBlockState object, String group)
	{
		return MinecraftForgeClient.getRenderLayer()== BlockRenderLayer.TRANSLUCENT;
	}
}