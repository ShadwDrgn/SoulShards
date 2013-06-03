package com.shadwdrgn.soulshards;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class CommonProxy {
    public void livingDeath(LivingDeathEvent e) {
        if (ItemShard.homeDims.containsKey(EntityList.classToStringMapping
                .get(e.entity.getClass()))
                && e.source.getEntity() instanceof EntityPlayerMP
                && !e.entity.getEntityData().getBoolean("mobcage")) {
            processShard((EntityPlayerMP) e.source.getEntity(), e.entity);
        }
    }

    public void processShard(EntityPlayerMP player, Entity e) {
        // Number of charges gained
        int nSouls = 1 + EnchantmentHelper.getEnchantmentLevel(
                SoulShards.eSoulStealer.effectId,
                ((EntityLiving) player).getHeldItem());
        // TODO: More organized shard processing
        // TODO: getValidShard(player, e)
        boolean bSpecialMob = false;
        if ((e.getClass().equals(EntitySkeleton.class) && ((EntitySkeleton) e)
                .getSkeletonType() == 1)
                || (e.getClass().equals(EntityZombie.class) && ((EntityZombie) e)
                        .isVillager())) {
            bSpecialMob = true;
        }
        for (int i = 0; i < 10; i++) {
            ItemStack is = player.inventory.getStackInSlot(i);

            // Slot is empty.
            if (is == null) {
                continue;
            }

            // Slot is a blank Soul Shard
            // In new code we'd return here.
            if (is.getItem().equals(SoulShards.itemBlankShard)) {

                is.stackSize--;
                if (is.stackSize == 0) {
                    player.inventory.mainInventory[i] = null;
                }
                ItemStack iSS = new ItemStack(SoulShards.itemShard, 1);
                String eClassName = (String) EntityList.classToStringMapping
                        .get(e.getClass());
                ItemShard.setType(iSS, eClassName, e.getEntityName(),
                        bSpecialMob);
                EntityItem ei = new EntityItem(player.worldObj,
                        (player.getPlayerCoordinates().posX),
                        (player.getPlayerCoordinates().posY),
                        (player.getPlayerCoordinates().posZ), iSS);
                player.worldObj.spawnEntityInWorld(ei);
                break;

                // In new code we'd only return one of these if it's valid for
                // the slain mob
            } else if (is.getItem().equals(SoulShards.itemShard)) {
                String eClassName = (String) EntityList.classToStringMapping
                        .get(e.getClass());
                int charge = ItemShard.getCharge(is);
                if (ItemShard.getType(is).isEmpty()) {
                    ItemShard.setType(is, eClassName, e.getEntityName(),
                            bSpecialMob);
                    ItemShard.setCharge(is, (charge >= 1024) ? 1024 : charge + nSouls);
                    break;
                } else if (ItemShard.getType(is).equals(eClassName)
                        && ItemShard.getSpecial(is) == bSpecialMob) {
                    ItemShard.setCharge(is, (charge >= 1024) ? 1024 : charge + nSouls);
                    break;
                }
            }
        }
    }

    public void playerInteract(PlayerInteractEvent e) {
    }
}
