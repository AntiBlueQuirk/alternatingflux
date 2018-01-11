package antibluequirk.alternatingflux;

import antibluequirk.alternatingflux.wire.AFWireType;
import blusunrize.immersiveengineering.common.Config.Mapped;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	
	@Mapped(mapClass = blusunrize.immersiveengineering.common.Config.class, mapName = "manual_int")
	public static int alternatingflux_afTransferRate;
	
	@Mapped(mapClass = blusunrize.immersiveengineering.common.Config.class, mapName = "manual_double")
	public static double alternatingflux_afkmLossMin;
	@Mapped(mapClass = blusunrize.immersiveengineering.common.Config.class, mapName = "manual_double")
	public static double alternatingflux_afkmLossMax;
	
	@Mapped(mapClass = blusunrize.immersiveengineering.common.Config.class, mapName = "manual_int")
	public static int alternatingflux_afMaxLength;
	
	
	@net.minecraftforge.common.config.Config(modid = AlternatingFlux.MODID)
	public static class AFConfig {
		@Comment({ "The transfer rates for the wires.", "Order: AF" })
		public static int[] wireTransferRate = new int[] { 131072 };

		@Comment({ "The percentage of power lost every X blocks of distance for a wire network. (X is equal to the maximum wire length.)",
		"The default value of 0.005 for AF is approximately equivalent to 10.67-14.93% over 1024 blocks, depending on the capacity penalty.",
		"If you'd like to halve that penalty to 5.33-7.47%, set this value to 0.0025.",
		"Order: AF" })
		public static double[] wireLossRatio = new double[] { 0.005 };

		@Comment({ "The RGB color of the wires.", "Order: AF" })
		public static int[] wireColouration = new int[] { 0xf6866c };

		@Comment({ "The maximum length of a single wire. The loss penalty is applied for each of these lengths, so increasing this value increases the efficiency of the wire.", "Order: AF" })
		public static int[] wireLength = { 48 };

	}

	public static void preInit(FMLPreInitializationEvent event) {
		refresh();
	}
	
	public static void refresh() {
		AFWireType.wire_rates = AFConfig.wireTransferRate;
		AFWireType.wire_lossratios = AFConfig.wireLossRatio;
		AFWireType.wire_colors = AFConfig.wireColouration;
		AFWireType.wire_length = AFConfig.wireLength;
    
		alternatingflux_afTransferRate = AFConfig.wireTransferRate[0];
		alternatingflux_afkmLossMin = ((1024/AFConfig.wireLength[0])*AFConfig.wireLossRatio[0]*100);
		alternatingflux_afkmLossMax = alternatingflux_afkmLossMin*1.4;
		//Crude rounding, because we can't control the formatting.
		alternatingflux_afkmLossMin = Math.round(alternatingflux_afkmLossMin * 10.0) / 10.0;
		alternatingflux_afkmLossMax = Math.round(alternatingflux_afkmLossMax * 10.0) / 10.0;
		alternatingflux_afMaxLength = AFConfig.wireLength[0];
		blusunrize.immersiveengineering.common.Config.checkMappedValues(Config.class);
	}
}