package antibluequirk.alternatingflux.wire;

import antibluequirk.alternatingflux.AlternatingFlux;
import antibluequirk.alternatingflux.Config.AFConfig;
import antibluequirk.alternatingflux.block.BlockTypes_Connector;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AFWireType extends WireType {
	public static AFWireType AF;

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
	
	public static void init()
	{
		AF = new AFWireType(0);
		WireApi.registerFeedthroughForWiretype(AF, new ResourceLocation(AlternatingFlux.MODID, "block/connector/relay_af.obj"),
				AlternatingFlux.TEX_PASSTHROUGH_AF, new float[]{0, 0, 16, 16},
				.75, (s)->s.getBlock()== AlternatingFlux.block_conn && s.getValue(AlternatingFlux.block_conn.property) == BlockTypes_Connector.RELAY_AF,
				8*30F/AF.getTransferRate(), 15, (f)->f);
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
	
	@Override
	public String getCategory()
	{
		return "AF";
	}

	@Override
	public double getDamageRadius()
	{
		switch (ordinal)
		{
			case 0://AF
				return .5;
		}
		return 0;
	}
		
	@Override
	public boolean canCauseDamage() {
		return true;
	}
}