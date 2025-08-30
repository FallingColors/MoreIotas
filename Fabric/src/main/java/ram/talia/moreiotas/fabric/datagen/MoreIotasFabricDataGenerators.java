package ram.talia.moreiotas.fabric.datagen;

import at.petrak.hexcasting.api.HexAPI;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MoreIotasFabricDataGenerators implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator (FabricDataGenerator fabricDataGenerator) {
		HexAPI.LOGGER.info("Starting Fabric-specific datagen");
		
//		fabricDataGenerator.addProvider(new HexalplatRecipes(fabricDataGenerator));
	}
	
	private static TagKey<Item> tag(String s) {
		return tag("c", s);
	}
	private static TagKey<Item> tag(String namespace, String s) {
		return TagKey.create(Registries.ITEM, new ResourceLocation(namespace, s));
	}
}
