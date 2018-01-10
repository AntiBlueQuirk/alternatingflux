package antibluequirk.alternatingflux.wire;

import antibluequirk.alternatingflux.AlternatingFlux;
import antibluequirk.alternatingflux.Config.AFConfig;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AFWireType extends WireType {
	public static AFWireType AF = new AFWireType(0);

	final int ordinal;

	public static String[]	wire_names	= { "AF" };
	public static int[]		wire_rates;
	public static double[]	wire_lossratios;
	public static int[]		wire_length;

	public static int[]		wire_colors;
	public static double[]	wire_renderdias	= { 0.078125 };

	public AFWireType(int ordinal) {
		super();
		this.ordinal = ordinal;
	}

	/**
	 * In this case, this does not return the loss RATIO but the loss PER BLOCK
	 */
	@Override
	public double getLossRatio() {
		return wire_lossratios[ordinal];
	}

	@Override
	public int getTransferRate() {
		return wire_rates[ordinal];
	}

	@Override
	public int getColour(Connection connection) {
		return wire_colors[ordinal];
	}

	@Override
	public double getSlack() {
		return 1.002;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon(Connection connection) {
		return iconDefaultWire;
	}

	@Override
	public int getMaxLength() {
		return AFConfig.wireLength[ordinal];
	}

	@Override
	public ItemStack getWireCoil() {
		return new ItemStack(AlternatingFlux.item_coil, 1, ordinal);
	}

	@Override
	public String getUniqueName() {
		return wire_names[ordinal];
	}

	@Override
	public double getRenderDiameter() {
		return wire_renderdias[ordinal];
	}

	@Override
	public boolean isEnergyWire() {
		return true;
	}
}