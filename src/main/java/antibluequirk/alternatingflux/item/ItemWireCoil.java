package antibluequirk.alternatingflux.item;

import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityEnergyMeter;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFeedthrough;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityRedstoneBreaker;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import antibluequirk.alternatingflux.block.TileEntityRelayAF;
import antibluequirk.alternatingflux.block.TileEntityTransformerAF;
import antibluequirk.alternatingflux.wire.AFWireType;

public class ItemWireCoil extends ItemAFBase implements IWireCoil {
	public ItemWireCoil() {
		super("wirecoil", 64, "af");
	}

	@Override
	public WireType getWireType(ItemStack stack) {
		switch (stack.getItemDamage()) {
		default:
		case 0:
			return (WireType) AFWireType.AF;
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("linkingPos")) {
			int[] link = stack.getTagCompound().getIntArray("linkingPos");
			if (link != null && link.length > 3) {
				tooltip.add(I18n.format(Lib.DESC_INFO + "attachedToDim", link[1], link[2], link[3], link[0]));
			}
		}
	}


    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        return ApiUtils.doCoilUse(this, player, world, pos, hand, side, hitX, hitY, hitZ);
    }


	public boolean canConnectCable(WireType wire, TileEntity targetEntity) {
		//We specifically only support whitelisted TEs here.
		//Without this, you can connect the AF wire to any connectable block that doesn't specifically deny it.
		if (!(targetEntity instanceof TileEntityRelayAF) &&
			!(targetEntity instanceof TileEntityTransformerAF) &&
			!(targetEntity instanceof TileEntityRedstoneBreaker) &&
			!(targetEntity instanceof TileEntityEnergyMeter) &&
			!(targetEntity instanceof TileEntityFeedthrough))
			return false;
		return true;
	}
}