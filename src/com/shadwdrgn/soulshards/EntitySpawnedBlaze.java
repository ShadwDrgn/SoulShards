package com.shadwdrgn.soulshards;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntitySpawnedBlaze extends EntityBlaze implements ISpawnedMob {

    public EntitySpawnedBlaze(World world) {
        super(world);
    }

    @Override
    protected void dropFewItems(boolean par1, int par2) {
        int var3 = rand.nextInt(2 + par2);

        for (int var4 = 0; var4 < var3; ++var4) {
            this.dropItem(Item.blazeRod.itemID, 1);
        }
    }

	@Override
	public void postInit() {
		
	}
}
