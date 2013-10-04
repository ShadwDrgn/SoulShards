package com.shadwdrgn.soulshards;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "SoulShards", name = "Soul Shards", version = SoulShards.VERSION, dependencies = "required-after:Forge@[7.8.0.716,]")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "SoulShards" }, packetHandler = PacketHandler.class)
public class SoulShards {
	
	public static final String VERSION = "@VERSION@";

	@Instance(value = "SoulShards")
	public static SoulShards instance;

	@SidedProxy(clientSide = "com.shadwdrgn.soulshards.ClientProxy", serverSide = "com.shadwdrgn.soulshards.CommonProxy")
	public static CommonProxy proxy;

	public static class Config {
		public static int idBlankShard = 8076;
		public static int idShard = 8075;
		public static int idCage = 1475;
		public static int nextTier = 200;
	}

	public static class Items {
		public static ItemBlankShard itemBlankShard;
		public static ItemShard itemShard;
	}

	public static class Blocks {
		public static BlockCage blockCage;
	}

	public static final Enchantment eSoulStealer = new EnchantmentSoulStealer(85, 10);

	@EventHandler
	public void preinit(FMLPreInitializationEvent e) {
		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();

		Config.idCage = config.getBlock("Soul_Cage", Config.idCage).getInt(Config.idCage);
		Config.idShard = config.getItem(Configuration.CATEGORY_ITEM, "Soul_Shard", Config.idShard).getInt(Config.idShard);
		Config.idBlankShard = config.getItem(Configuration.CATEGORY_ITEM, "Blank_Soul_Shard", Config.idBlankShard).getInt(Config.idBlankShard);
		Config.nextTier = config.get(Configuration.CATEGORY_GENERAL, "between_Tiers", Config.nextTier).getInt(Config.nextTier);

		config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		Blocks.blockCage = new BlockCage(Config.idCage);
		Items.itemBlankShard = new ItemBlankShard(Config.idBlankShard);

		Items.itemShard = new ItemShard(Config.idShard);

		MinecraftForge.EVENT_BUS.register(new HandlerForgeEvents());
		LanguageRegistry.instance().addStringLocalization("enchantment.soulstealer", "Soul Stealer");
		LanguageRegistry.addName(Blocks.blockCage, "Soul Cage");

		LanguageRegistry.addName(Items.itemBlankShard, "Soul Shard");
		LanguageRegistry.addName(Items.itemShard, "Soul Shard");

		GameRegistry.registerBlock(Blocks.blockCage, "soulcage");
		GameRegistry.addRecipe(new ItemStack(Blocks.blockCage, 1), new Object[] { "XXX", "X X", "XXX", 'X', Block.fenceIron });

		TileEntity.addMapping(TESoulCage.class, "cage");

		EntityRegistry.registerModEntity(EntitySpawnedBlaze.class, "SpawnedBlaze", 1, SoulShards.instance, 160, 5, true);
		EntityRegistry.registerModEntity(EntitySpawnedSkeleton.class, "SpawnedSkeleton", 2, SoulShards.instance, 160, 5, true);
		EntityRegistry.registerModEntity(EntitySpawnedZombie.class, "SpawnedZombie", 3, SoulShards.instance, 160, 5, true);
	}
}
