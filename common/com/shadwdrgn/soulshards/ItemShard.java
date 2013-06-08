package com.shadwdrgn.soulshards;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemShard extends Item {
    public static class MobInfo {
        String c;
        boolean special;

        public MobInfo(String c, boolean special) {
            this.c = c;
            this.special = special;
        }
    }

    static HashMap<String, Integer> homeDims = new HashMap<String, Integer>();
    static {
        homeDims.put((String) EntityList.classToStringMapping.get(EntityBlaze.class), -1);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityGhast.class), -1);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityPigZombie.class), -1);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityMagmaCube.class), -1);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityEnderman.class), 1);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityCaveSpider.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntitySilverfish.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntitySkeleton.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntitySpider.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntitySlime.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityZombie.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityCreeper.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityWitch.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntitySheep.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityCow.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityMooshroom.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityChicken.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityBat.class),0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityOcelot.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityWolf.class), 0);
        homeDims.put((String) EntityList.classToStringMapping.get(EntityPig.class), 0);
        homeDims.put("entBrainyZombie", 0);
        homeDims.put("entFirebat", -1);
        
        homeDims.put("TwilightForest.Wild Boar", 0);
        homeDims.put("TwilightForest.Bighorn Sheep", 0);
        homeDims.put("TwilightForest.Wild Deer", 0);
        homeDims.put("TwilightForest.Redcap", 0);
        homeDims.put("TwilightForest.Swarm Spider", 0);
        homeDims.put("TwilightForest.Skeleton Druid", 0);
        homeDims.put("TwilightForest.Hostile Wolf", 0);
        homeDims.put("TwilightForest.Twilight Wraith", 0);
        homeDims.put("TwilightForest.Hedge Spider", 0);
        homeDims.put("TwilightForest.Penguin", 0);
        homeDims.put("TwilightForest.Twilight Kobold", 0);
        homeDims.put("TwilightForest.Minotaur", 0);
        homeDims.put("TwilightForest.Fire Beetle", 0);
        homeDims.put("TwilightForest.Slime Beetle", 0);
        homeDims.put("TwilightForest.Pinch Beetle", 0);
        homeDims.put("TwilightForest.Maze Slime", 0);
        homeDims.put("TwilightForest.Redcap Sapper", 0);
        homeDims.put("TwilightForest.Mini Ghast", 0);
        homeDims.put("TwilightForest.Tower Ghast", 0);
        //homeDims.put("TwilightForest.Tower Golem", 0);
        homeDims.put("TwilightForest.Tower Termite", 0);
        homeDims.put("TwilightForest.Tower Broodling", 0);
    }

    protected ItemShard(int par1) {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.maxStackSize = 1;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer par2EntityPlayer,
            List list, boolean par4) {
        initTags(is);
        Integer charges = getCharge(is);
        Integer tier = (int) ((Math.log(charges) / Math.log(2))) - 5;
        tier = (tier < 0) ? 0 : tier;
        if (charges == 0) {
            tier = 0;
        }

        if (!ItemShard.getType(is).isEmpty()) {
            if (ItemShard.getSpecial(is)) {
                if (ItemShard.getType(is)
                        .equals(EntityList.classToStringMapping
                                .get(EntityZombie.class))
                        && ItemShard.getSpecial(is)) {
                    list.add("Zombie Villager");
                } else if (ItemShard.getType(is).equals(
                        EntityList.classToStringMapping
                                .get(EntitySkeleton.class))
                        && ItemShard.getSpecial(is)) {
                    list.add("Wither Skeleton");
                }
            } else {
                list.add(getName(is));
            }
        }
        list.add("Tier: " + Integer.toString(tier));
        if (charges > 0) {
            list.add("Killed: " + Integer.toString(charges));
        }
    }

    static Integer getCharge(ItemStack is) {
        NBTTagCompound tag = is.getTagCompound();
        return tag.getInteger("charge");
    }
    static void setCharge(ItemStack is, int c) {
        initTags(is);
        NBTTagCompound tag = is.getTagCompound();
        
        tag.setInteger("charge", c);
    }
    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World w, int x,
            int y, int z, int face, float par8, float par9, float par10) {
        if (!w.isRemote) {
            
             if (w.getBlockId(x, y, z) == Block.stone.blockID) {
             w.setBlock(x, y, z, Block.mobSpawner.blockID);
             TileEntityMobSpawner t =
             (TileEntityMobSpawner)w.getBlockTileEntity(x, y, z);
             MobSpawnerBaseLogic m = t.func_98049_a();
             m.setMobID("CaveSpider");
             return true;
             }
             
            if (w.getBlockId(x, y, z) == SoulShards.blockCage.blockID
                    && ItemShard.getType(is) != null) {
                if (ItemShard.getType(is).isEmpty())
                    return false;
                Integer charges = getCharge(is);
                Integer tier = (int) ((Math.log(charges) / Math.log(2))) - 5;
                // Integer tier = charges / SoulShards.nextTier;
                tier = (tier < 0) ? 0 : tier;
                if (tier == 0)
                    return false;
                // -1 = nether
                // 1 = end
                // 0 = overworld
                if ((tier < 4 && canSpawnhere(w.provider.dimensionId, is))
                        || tier >= 4) {
                    TESoulCage te = (TESoulCage) w.getBlockTileEntity(x, y, z);
                    // te.signal = w.isBlockIndirectlyGettingPowered(x, y, z) ||
                    // w.isBlockGettingPowered(x, y, z);
                    te.signal = w.isBlockIndirectlyGettingPowered(x, y, z);
                    te.setMobType(ItemShard.getType(is),
                            ItemShard.getSpecial(is));
                    te.setTier(tier);
                    w.markBlockForUpdate(x, y, z);
                    te.rCount();
                    te.inv[0] = is.copy();
                    is.stackSize = 0;
                    return true;
                }
            } else if (w.getBlockId(x, y, z) == Block.mobSpawner.blockID && ItemShard.getType(is) != null) {
                TileEntityMobSpawner tems = (TileEntityMobSpawner) w.getBlockTileEntity(x, y, z);
                MobSpawnerBaseLogic msbl = tems.func_98049_a(); 
                String m = msbl.getEntityNameToSpawn();
                if (!m.isEmpty() && m.equals(ItemShard.getType(is))) {
                    Integer i = getCharge(is) + SoulShards.nextTier;
                    setCharge(is, (i >= 1024) ? 1024 : i);
                    w.setBlockToAir(x, y, z);
                    // w.setBlock(x, y, z, 0);
                    w.playAuxSFX(
                            2001,
                            x,
                            y,
                            z,
                            Block.mobSpawner.blockID
                                    + (w.getBlockMetadata(x, y, z) << 12));
                }
            }
        }

        return false;
    }

    public static void setType(ItemStack is, String t, String n,
            boolean bSpecialMob) {
        NBTTagCompound tag = initTags(is);
        tag.setString("mobtype", t);
        tag.setString("mobname", n);
        tag.setBoolean("specialmob", bSpecialMob);
    }

    public static String getType(ItemStack is) {
        NBTTagCompound tag = initTags(is);
        return tag.getString("mobtype");
    }

    public static Class getMobClass(ItemStack is) {
        NBTTagCompound tag = initTags(is);
        return EntityList.stringToClassMapping.get(tag.getString("mobtype"))
                .getClass();
    }

    public static boolean getSpecial(ItemStack is) {
        NBTTagCompound tag = initTags(is);
        return tag.getBoolean("specialmob");
    }

    public static String getName(ItemStack is) {
        NBTTagCompound tag = initTags(is);
        return tag.getString("mobname");
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    public static NBTTagCompound initTags(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
            tag.setString("mobtype", "");
            tag.setString("mobname", "");
            tag.setBoolean("specialmob", false);
            tag.setInteger("charge", 0);
        }

        return tag;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
            List par3List) {
        ItemStack is = new ItemStack(par1, 1, 0);
        par3List.add(is);
        for (int i = 1; i <= 5; i++) {
            is = new ItemStack(par1, 1, 0);
            setCharge(is, (int)Math.pow(2, (i + 5)));
            par3List.add(is);
        }
    }

    public boolean canSpawnhere(int d, ItemStack is) {
        String type = getType(is);
        if (!homeDims.containsKey(getType(is)))
            return false;
        if (d != -1
                && type.equals(EntityList.classToStringMapping
                        .get(EntitySkeleton.class)) && getSpecial(is))
            return false;
        if (d == -1
                && type.equals(EntityList.classToStringMapping
                        .get(EntitySkeleton.class)) && getSpecial(is))
            return true;
        if (d == -1 || d == 1)
            return homeDims.get(type) == d;
        else
            return homeDims.get(type) == 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack,
            ItemStack par2ItemStack) {
        return false;
    }
    /*
     * @Override public ItemStack getContainerItemStack(ItemStack itemStack) {
     * ItemStack rIS = new ItemStack(getContainerItem());
     * rIS.setItemDamage(itemStack.getItemDamage() + 1); return rIS; }
     * 
     * @Override public boolean doesContainerItemLeaveCraftingGrid(ItemStack
     * par1ItemStack) { return false; }
     */
}
