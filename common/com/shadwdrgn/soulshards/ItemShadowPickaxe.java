package com.shadwdrgn.soulshards;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemShadowPickaxe extends ItemPickaxe {

    public ItemShadowPickaxe(int par1, EnumToolMaterial par2EnumToolMaterial) {
        super(par1, par2EnumToolMaterial);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public String getTextureFile() {
        return "/soulshards-i.png";
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack rIS) {
        return rIS.itemID == SoulShards.itemShadowElement.itemID ? true : false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack is, World world, int blockID,
            int x, int y, int z, EntityLiving par7EntityLiving) {
        if (par7EntityLiving.isSneaking())
            return super.onBlockDestroyed(is, world, blockID, x, y, z,
                    par7EntityLiving);
        for (int y2 = y - 1; y2 <= y + 1; ++y2) {
            for (int x2 = x - 1; x2 <= x + 1; ++x2) {
                for (int z2 = z - 1; z2 <= z + 1; ++z2) {
                    Block b = Block.blocksList[world.getBlockId(x2, y2, z2)];
                    if (b == null) {
                        continue;
                    }
                    b.dropBlockAsItem(world, x2, y2, z2,
                            world.getBlockMetadata(x2, y2, z2), 0);
                    world.setBlockToAir(x2, y2, z2);
                    // world.setBlockWithNotify(x2, y2, z2, 0);
                    is.damageItem(1, par7EntityLiving);
                }
            }
        }
        world.playSoundEffect(
                x,
                y,
                z,
                "random.explode",
                4.0F,
                (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        world.spawnParticle("hugeexplosion", x, y, z, 1.0D, 0.0D, 0.0D);
        return true;
    }
}
