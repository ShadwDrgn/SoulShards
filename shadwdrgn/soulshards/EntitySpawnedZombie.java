package com.shadwdrgn.soulshards;

import java.util.Calendar;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntitySpawnedZombie extends EntityZombie implements ISpawnedMob {
    boolean special = false;

    public EntitySpawnedZombie(World world, boolean special) {
        super(world);
        this.special = special;
    }

    public EntitySpawnedZombie(World world) {
        this(world, false);
    }

    
    @Override
    public void postInit() {
        this.setCanPickUpLoot(rand.nextFloat() < 0.55F * this.worldObj.getLocationTensionFactor(this.posX, this.posY, this.posZ));
        if (special) {
            this.setVillager(true);
        }
        this.addRandomArmor();
        this.enchantEquipment();
        if (this.getCurrentItemOrArmor(4) == null) {
            Calendar var1 = worldObj.getCurrentDate();

            if (var1.get(2) + 1 == 10 && var1.get(5) == 31
                    && rand.nextFloat() < 0.25F) {
                this.setCurrentItemOrArmor(4, new ItemStack(
                        rand.nextFloat() < 0.1F ? Block.pumpkinLantern
                                : Block.pumpkin));
                equipmentDropChances[4] = 0.0F;
            }
        }
    }
}
