package antibluequirk.alternatingflux;

import antibluequirk.alternatingflux.wire.AFWireType;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	@net.minecraftforge.common.config.Config(modid = AlternatingFlux.MODID)
	public static class AFConfig {
		@Comment({ "The transfer rates for the wires.", "Order: AF" })
		public static int[] wireTransferRate = new int[] { 131072 };

		@Comment({ "The percentage of power lost every 16 blocks of distance for a wire network.",
    "The default value of 0.00125 for AF is approximately equivalent to 8% over 1024 blocks, though in practice this will be closer to 11.2% due to the capacity penalty.",
    "If you'd like to halve that penalty to 4% (5.6%), set this value to 0.000625.",
    "Order: AF" })
		public static double[] wireLossRatio = new double[] { 0.00125 };

		@Comment({ "The RGB color of the wires.", "Order: AF" })
		public static int[] wireColouration = new int[] { 0xf6866c };

		@Comment({ "The maximum length of a single wire.", "Order: AF" })
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
	}
}