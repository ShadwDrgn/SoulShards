package com.shadwdrgn.soulshards;

import java.util.Calendar;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntitySpawnedZombie extends EntityZombie {
    boolean special = false;

    public EntitySpawnedZombie(World par1World) {
        super(par1World);
    }

    @Override
    public void initCreature() {
        this.setCanPickUpLoot(rand.nextFloat() < pickUpLootProability[worldObj.difficultySetting]);

        if (special) {
            this.setVillager(true);
        }

        this.addRandomArmor();
        this.func_82162_bC();

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