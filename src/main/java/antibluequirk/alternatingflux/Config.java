package antibluequirk.alternatingflux;

import antibluequirk.alternatingflux.wire.AFWireType;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	@net.minecraftforge.common.config.Config(modid = AlternatingFlux.MODID)
	public static class AFConfig {
		@Comment({ "The transfer rates for the wires.", "Order: AF" })
		public static int[] wireTransferRate = new int[] { 131072 };

		@Comment({ "The percentage of power lost every 16 blocks of distance for a wire network.", "Order: AF" })
		public static double[] wireLossRatio = new double[] { 0.00125 }; // .00125 is %8 over 1024 blocks

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