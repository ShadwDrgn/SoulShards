package com.shadwdrgn.soulshards;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TESoulCage extends TileEntity implements IInventory {
    ItemStack[] inv = new ItemStack[1];
    private String mobType = "";
    boolean special = false;
    private int mobCount;
    private int delay, count, persistCheckCount = 0;
    public int tier;
    protected boolean signal = false;
    ArrayList<EntityLiving> persistList = new ArrayList<EntityLiving>();

    private static Field presistenceRequired = EntityLiving.class
            .getDeclaredFields()[15];

    public TESoulCage() {
        presistenceRequired.setAccessible(true);
    }

    public void setTier(int t) {
        tier = t;
        if (t == 1) {
            mobCount = 2;
            this.setDelay(20);
        } else if (t < 4) {
            mobCount = 4;
            this.setDelay((t == 2) ? 10 : 5);
        } else {
            mobCount = 6;
            this.setDelay((t == 4) ? 5 : 2);
        }
    }

    public void setMobType(String s, boolean b) {
        mobType = s;
        special = b;
    }

    public String getMobType() {
        return mobType;
    }

    public void setDelay(int i) {
        delay = i * 20;
    }

    public void rCount() {
        count = delay;
    }

    @Override
    public void updateEntity() {
        persistCheckCount++;
        if (persistCheckCount >= 200) {
            persistCheckCount = 0;
            for (int i = 0; i < persistList.size(); i++) {
                EntityLiving el = persistList.get(i);
                if (el.getAge() > 600) {
                    try {
                        presistenceRequired.set(el, false);
                    } catch (Exception Ex) {
                    }
                    persistList.remove(i);
                }
            }

        }
        if (count >= delay) {
            count = 0;
            for (int i = 0; i < mobCount; ++i) {
                /*
                 * Create and spawn our mob group
                 */
                EntityLiving mob = createMobByName(mobType);
                
                if (mob == null) {
                    return;
                }
                
                if (mob instanceof ISpawnedMob) {
                	((ISpawnedMob)mob).postInit();
                }
                
                int numMobs = worldObj.getEntitiesWithinAABB(
                        mob.getClass(),
                        AxisAlignedBB
                                .getAABBPool()
                                .getAABB(xCoord, yCoord, zCoord, xCoord + 1,
                                        yCoord + 1, zCoord + 1)
                                .expand(8.0D, 4.0D, 8.0D)).size();
                if (numMobs >= 2 * mobCount) {
                    count = 0;
                    return;
                }
                /*
                 * get a nice spot to put our mob
                 */
                double x = xCoord
                        + (worldObj.rand.nextDouble() - worldObj.rand
                                .nextDouble()) * 4.0D;
                double y = yCoord + worldObj.rand.nextInt(3) - 1;
                double z = zCoord
                        + (worldObj.rand.nextDouble() - worldObj.rand
                                .nextDouble()) * 4.0D;
                
                mob.setLocationAndAngles(x, y, z,
                        worldObj.rand.nextFloat() * 360.0F, 0.0F);
                if (mob.getCanSpawnHere()
                        || ((tier == 5) && this.getCanSpawnHere(mob))) {
                    if (!mobType.isEmpty() && (tier < 5 || !signal)) {
                        /*
                         * Mark mob as spawned from cage
                         */
                        NBTTagCompound cTag = mob.getEntityData();
                        cTag.setBoolean("mobcage", true);
                        worldObj.spawnEntityInWorld(mob);
                        worldObj.playAuxSFX(2004, xCoord, yCoord, zCoord, 0);
                        if (mob != null) {
                            if (tier >= 3) {
                                try {
                                    presistenceRequired.set(mob, true);
                                } catch (Exception Ex) {
                                }
                                persistList.add(mob);
                            }
                            mob.spawnExplosionParticle();
                        }
                        count = 0;
                    }
                }
            }
        }
        count++;
    }

    private EntityLiving createMobByName(String mobType2) {
    	if (mobType.equals("SpawnedBlaze") || mobType.equals("Blaze")) {
            return new EntitySpawnedBlaze(worldObj);
        } else if (mobType.equals("Skeleton")) {
        	return new EntitySpawnedSkeleton(worldObj, special);
        } else if (mobType.equals("Zombie")) {
        	return new EntitySpawnedZombie(worldObj, special);
        }
        Entity entity = EntityList.createEntityByName(mobType, worldObj);
        if (entity instanceof EntityLiving) {
        	return (EntityLiving)entity;
        }
        return null;
	}

	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        mobType = par1NBTTagCompound.getString("mobType");
        special = par1NBTTagCompound.getBoolean("special");
        mobCount = par1NBTTagCompound.getInteger("mobCount");
        tier = par1NBTTagCompound.getInteger("count");
        delay = par1NBTTagCompound.getInteger("delay");
        tier = par1NBTTagCompound.getInteger("tier");
        signal = par1NBTTagCompound.getBoolean("signal");

    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("mobType", mobType);
        par1NBTTagCompound.setBoolean("special", special);
        par1NBTTagCompound.setInteger("mobCount", mobCount);
        par1NBTTagCompound.setInteger("count", count);
        par1NBTTagCompound.setInteger("delay", delay);
        par1NBTTagCompound.setInteger("tier", tier);
        par1NBTTagCompound.setBoolean("signal", signal);
    }

    public boolean getCanSpawnHere(EntityLiving e) {
        // return e.worldObj.checkIfAABBIsClear(e.boundingBox) &&
        // e.worldObj.getCollidingBoundingBoxes(e, e.boundingBox).isEmpty() &&
        // !e.worldObj.isAnyLiquid(e.boundingBox);
        return e.worldObj.getCollidingBoundingBoxes(e, e.boundingBox).isEmpty()
                && !e.worldObj.isAnyLiquid(e.boundingBox);
    }

    @Override
    public int getSizeInventory() {
        return inv.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inv[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inv[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName() {
        return "invsoulcage";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this
                && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
                        zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isInvNameLocalized() {
        // TODO WTF Is this?
        return false;
    }

    @Override
    public void onInventoryChanged() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }
}
