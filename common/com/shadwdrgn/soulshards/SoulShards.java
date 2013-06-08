package com.shadwdrgn.soulshards;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
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
    public static final String VERSION = "@VERSION@.@BUILD_NUMBER@";

    @Instance
    public static SoulShards instance;

    @SidedProxy(clientSide = "com.shadwdrgn.soulshards.ClientProxy", serverSide = "com.shadwdrgn.soulshards.CommonProxy")
    public static CommonProxy proxy;

    public static int idBlankShard, idShard;
    public static int idShadowElement;
    public static int idShadowBlade, idShadowShovel, idShadowPickaxe,
            idShadowAxe, idShadowHoe;
    public static int idCage;
    public static int nextTier;

    public static Item itemBlankShard, itemShard;
    public static Item itemShadowElement;
    public static Item itemShadowBlade, itemShadowShovel, itemShadowPickaxe,
            itemShadowAxe, itemShadowHoe;
    public static Block blockCage;

    public static final Enchantment eSoulStealer = new EnchantmentSoulStealer(
            85, 10);

    @PreInit
    public void preinit(FMLPreInitializationEvent e) {
        Configuration config = new Configuration(
                e.getSuggestedConfigurationFile());
        config.load();

        idCage = config.getBlock("Soul_Cage", 1475).getInt(1475);

        idShard = config.getItem(Configuration.CATEGORY_ITEM, "Soul_Shard",
                8075).getInt(8075);
        idBlankShard = config.getItem(Configuration.CATEGORY_ITEM,
                "Blank_Soul_Shard", 8076).getInt(8076);

        idShadowElement = config.getItem(Configuration.CATEGORY_ITEM,
                "Shadow_Element", 8086).getInt(8086);
        idShadowBlade = config.getItem(Configuration.CATEGORY_ITEM,
                "Shadow_Sword", 8087).getInt(8087);
        idShadowShovel = config.getItem(Configuration.CATEGORY_ITEM,
                "Shadow_Shovel", 8088).getInt(8088);
        idShadowPickaxe = config.getItem(Configuration.CATEGORY_ITEM,
                "Shadow_Pickaxe", 8089).getInt(8089);
        idShadowAxe = config.getItem(Configuration.CATEGORY_ITEM, "Shadow_Axe",
                8090).getInt(8090);
        idShadowHoe = config.getItem(Configuration.CATEGORY_ITEM, "Shadow_Hoe",
                8091).getInt(8091);

        nextTier = config.get(Configuration.CATEGORY_GENERAL, "between_Tiers",
                200).getInt(200);

        config.save();
    }

    @Init
    public void init(FMLInitializationEvent e) {
        blockCage = new BlockCage(idCage).setHardness(5.0F)
                .setStepSound(new StepSound("stone", 1.0F, 1.5F))
                .setUnlocalizedName("cage");

        itemBlankShard = new ItemBlankShard(idBlankShard)
                .setUnlocalizedName("blankSoulShard");
        itemShard = new ItemShard(idShard).setMaxStackSize(1)
                .setUnlocalizedName("soulShard");

        // itemShadowElement = new
        // ItemShadowElement(idShadowElement).setIconIndex(0x32).setUnlocalizedName("shadowElement");

        itemShadowBlade = new ItemShadowBlade(idShadowBlade,
                EnumToolMaterial.EMERALD).setUnlocalizedName("shadowBlade");
        itemShadowPickaxe = new ItemShadowPickaxe(idShadowPickaxe,
                EnumToolMaterial.EMERALD).setUnlocalizedName("shadowPickaxe");
        // itemShadowAxe = new ItemShadowAxe(idShadowAxe,
        // EnumToolMaterial.EMERALD).setIconIndex(0x63).setUnlocalizedName("shadowAxe");
        // itemShadowHoe = new ItemShadowHoe(idShadowHoe,
        // EnumToolMaterial.EMERALD).setIconIndex(0x73).setUnlocalizedName("shadowHoe");

        MinecraftForge.EVENT_BUS.register(new HandlerForgeEvents());
        LanguageRegistry.instance().addStringLocalization(
                "enchantment.soulstealer", "Soul Stealer");
        LanguageRegistry.addName(blockCage, "Soul Cage");

        LanguageRegistry.addName(itemBlankShard, "Soul Shard");
        LanguageRegistry.addName(itemShard, "Soul Shard");

        LanguageRegistry.addName(itemShadowBlade, "Shadow Blade");
        // LanguageRegistry.addName(itemShadowShovel, "Shadow Shovel");
        LanguageRegistry.addName(itemShadowPickaxe, "Shadow Pickaxe");
        // LanguageRegistry.addName(itemShadowAxe, "Shadow Axe");
        // LanguageRegistry.addName(itemShadowHoe, "Shadow Hoe");

        GameRegistry.registerBlock(blockCage, "soulcage");
        GameRegistry.addRecipe(new ItemStack(blockCage, 1), new Object[] {
                "XXX", "X X", "XXX", 'X', Block.fenceIron });

        TileEntity.addMapping(TESoulCage.class, "cage");

        EntityRegistry.registerModEntity(EntitySpawnedBlaze.class,
                "SpawnedBlaze", 1, SoulShards.instance, 160, 5, true);
        EntityRegistry.registerModEntity(EntitySpawnedSkeleton.class,
                "SpawnedSkeleton", 2, SoulShards.instance, 160, 5, true);
        EntityRegistry.registerModEntity(EntitySpawnedZombie.class,
                "SpawnedZombie", 3, SoulShards.instance, 160, 5, true);
    }
}
