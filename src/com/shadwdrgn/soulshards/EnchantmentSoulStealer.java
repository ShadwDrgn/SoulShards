package com.shadwdrgn.soulshards;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public class EnchantmentSoulStealer extends Enchantment {

	protected EnchantmentSoulStealer(int par1, int par2) {
		super(par1, par2, EnumEnchantmentType.weapon);
		this.setName("soulstealer");
	}

	@Override
	public int getMinEnchantability(int par1) {
		return (par1 - 1) * 11;
	}

	@Override
	public int getMaxEnchantability(int par1) {
		return super.getMinEnchantability(par1) + 20;
	}

	/*
	 * @Override public boolean func_92037_a(ItemStack item) { return
	 * (item.itemID == SoulShards.itemCorruptedSword.shiftedIndex); }
	 */
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		// return (stack.itemID == SoulShards.itemCorruptedSword.itemID);
		return false;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}
}
