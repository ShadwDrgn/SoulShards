package com.shadwdrgn.soulshards;

import java.util.Random;

import net.minecraft.block.BlockMobSpawner;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCage extends BlockMobSpawner {
	private Random random = new Random();

	public BlockCage(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity createNewTileEntity(World par1World) {
		return new TESoulCage();
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return blockID;
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return 1;
	}

	@Override
	protected void dropXpOnBlockBreak(World par1World, int par2, int par3, int par4, int par5) {}

	@Override
	public void onNeighborBlockChange(World w, int x, int y, int z, int par5) {
		TESoulCage te = (TESoulCage)w.getBlockTileEntity(x, y, z);
		if (w.isBlockIndirectlyGettingPowered(x, y, z)) {
			te.signal = true;
		} else {
			te.signal = false;
		}
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		TESoulCage var7 = (TESoulCage)par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 == null) {
			super.breakBlock(par1World, par2, par3, par4, par5, par6);
			return;
		}
		for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
			ItemStack var9 = var7.getStackInSlot(var8);
			if (var9 == null) {
				continue;
			}
			float var10 = random.nextFloat() * 0.8F + 0.1F;
			float var11 = random.nextFloat() * 0.8F + 0.1F;
			float var12 = random.nextFloat() * 0.8F + 0.1F;
			while (var9.stackSize > 0) {
				int var13 = random.nextInt(21) + 10;
				if (var13 > var9.stackSize) {
					var13 = var9.stackSize;
				}
				var9.stackSize -= var13;
				EntityItem var14 = new EntityItem(par1World, par2 + var10, par3
						+ var11, par4 + var12, new ItemStack(var9.itemID, var13, var9.getItemDamage()));
				// if (var9.hasTagCompound())
				// var14.func_92014_d().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
				var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
				float var15 = 0.05F;
				var14.motionX = (float)random.nextGaussian() * var15;
				var14.motionY = (float)random.nextGaussian() * var15 + 0.2F;
				var14.motionZ = (float)random.nextGaussian() * var15;
				par1World.spawnEntityInWorld(var14);
			}
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
}
