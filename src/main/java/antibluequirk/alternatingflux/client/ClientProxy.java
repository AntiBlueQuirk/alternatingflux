package antibluequirk.alternatingflux.client;

import java.util.Locale;

import antibluequirk.alternatingflux.AlternatingFlux;
import antibluequirk.alternatingflux.CommonProxy;
import antibluequirk.alternatingflux.Config;
import antibluequirk.alternatingflux.item.ItemAFBase;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.client.models.smart.ConnLoader;
import blusunrize.immersiveengineering.common.blocks.BlockIEFluid;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IIEMetaBlock;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(this);
		OBJLoader.INSTANCE.addDomain(AlternatingFlux.MODID);
		IEOBJLoader.instance.addDomain(AlternatingFlux.MODID);
	}
	@Override
	public void postInit() {
		super.postInit();
		ManualInstance m = ManualHelper.getManual();
		
		m.addEntry("alternatingflux", ManualHelper.CAT_ENERGY,
				new ManualPages.Text(m, "alternatingflux0"),
				new ManualPages.Crafting(m, "alternatingfluxWire", new ItemStack(AlternatingFlux.item_material, 1, 0),
				                                                      new ItemStack(AlternatingFlux.item_coil, 1, 0)),
				new ManualPages.Text(m, "alternatingflux1"),
				new ManualPages.Crafting(m, "alternatingfluxRelay", new ItemStack(AlternatingFlux.block_conn, 1, 0)),
				new ManualPages.Crafting(m, "alternatingfluxTransformer", new ItemStack(AlternatingFlux.block_conn, 1, 1))
		);
	}
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent evt)
	{
		ConnLoader.baseModels.put("conn_af", new ResourceLocation("alternatingflux:block/connector/connector_af.obj"));
		ConnLoader.baseModels.put("rel_af", new ResourceLocation("alternatingflux:block/connector/relay_af.obj"));
		ConnLoader.baseModels.put("transformer_af_left",
				new ResourceLocation("alternatingflux:block/connector/transformer_af_left.obj"));
		ConnLoader.baseModels.put("transformer_af_right",
				new ResourceLocation("alternatingflux:block/connector/transformer_af_right.obj"));
		
    //This is mostly copied from IE's registerModels function in it's ClientProxy, so we can get everything integrated properly.
		for(Block block : AlternatingFlux.blocks)
		{
			final ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
			Item blockItem = Item.getItemFromBlock(block);
			if(blockItem==null)
				throw new RuntimeException("ITEMBLOCK FOR "+loc+" : "+block+" IS NULL");
			if(block instanceof IIEMetaBlock)
			{
				IIEMetaBlock ieMetaBlock = (IIEMetaBlock)block;
				if(ieMetaBlock.useCustomStateMapper())
					ModelLoader.setCustomStateMapper(block, IECustomStateMapper.getStateMapper(ieMetaBlock));
				ModelLoader.setCustomMeshDefinition(blockItem, new ItemMeshDefinition()
				{
					@Override
					public ModelResourceLocation getModelLocation(ItemStack stack)
					{
						return new ModelResourceLocation(loc, "inventory");
					}
				});
				for(int meta = 0; meta < ieMetaBlock.getMetaEnums().length; meta++)
				{
					String location = loc.toString();
					String prop = ieMetaBlock.appendPropertiesToState()?("inventory,"+ieMetaBlock.getMetaProperty().getName()+"="+ieMetaBlock.getMetaEnums()[meta].toString().toLowerCase(Locale.US)): null;
					if(ieMetaBlock.useCustomStateMapper())
					{
						String custom = ieMetaBlock.getCustomStateMapping(meta, true);
						if(custom!=null)
							location += "_"+custom;
					}
					try
					{
						ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(location, prop));
					} catch(NullPointerException npe)
					{
						throw new RuntimeException("WELP! apparently "+ieMetaBlock+" lacks an item!", npe);
					}
				}
			}
			//Not needed now
			/* else if(block instanceof BlockIEFluid)
				mapFluidState(block, ((BlockIEFluid)block).getFluid()); */
			else
				ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(loc, "inventory"));
		}
		
		for(Item item : AlternatingFlux.items)
		{
			if(item instanceof ItemBlock)
				continue;
			if(item instanceof ItemAFBase)
			{
				ItemAFBase ieMetaItem = (ItemAFBase)item;
				if(ieMetaItem.registerSubModels&&ieMetaItem.getSubNames()!=null&&ieMetaItem.getSubNames().length > 0)
				{
					for(int meta = 0; meta < ieMetaItem.getSubNames().length; meta++)
					{
						ResourceLocation loc = new ResourceLocation(AlternatingFlux.MODID, ieMetaItem.itemName+"/"+ieMetaItem.getSubNames()[meta]);
						ModelBakery.registerItemVariants(ieMetaItem, loc);
						ModelLoader.setCustomModelResourceLocation(ieMetaItem, meta, new ModelResourceLocation(loc, "inventory"));
					}
				} else
				{
					final ResourceLocation loc = new ResourceLocation(AlternatingFlux.MODID, ieMetaItem.itemName);
					ModelBakery.registerItemVariants(ieMetaItem, loc);
					ModelLoader.setCustomMeshDefinition(ieMetaItem, new ItemMeshDefinition()
					{
						@Override
						public ModelResourceLocation getModelLocation(ItemStack stack)
						{
							return new ModelResourceLocation(loc, "inventory");
						}
					});
				}
			} else
			{
				final ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
				ModelBakery.registerItemVariants(item, loc);
				ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition()
				{
					@Override
					public ModelResourceLocation getModelLocation(ItemStack stack)
					{
						return new ModelResourceLocation(loc, "inventory");
					}
				});
			}
		}
	}
	@SubscribeEvent
	public void updateConfig(OnConfigChangedEvent e)
	{
		if (AlternatingFlux.MODID.equals(e.getModID()))
		{
    		ConfigManager.sync(AlternatingFlux.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
    		Config.refresh();
		}
	}
}
