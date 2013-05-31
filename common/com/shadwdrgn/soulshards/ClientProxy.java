package com.shadwdrgn.soulshards;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxy extends CommonProxy {

    @Override
    public void playerInteract(PlayerInteractEvent e) {
        if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            return;
        if (e.entityPlayer.worldObj.getBlockId(e.x, e.y, e.z) != Block.glowStone.blockID)
            return;
        if (!isSoulRune(e.entityPlayer.worldObj, e.x, e.y, e.z))
            return;
        if (e.entityPlayer.getHeldItem() == null)
            return;
        if (e.entityPlayer.getHeldItem().itemID != Item.diamond.itemID)
            return;
        if (!e.entityPlayer.worldObj.isRemote)
            return;
        PacketDispatcher.sendPacketToServer(packetSoulRune(e.x, e.y, e.z));
        /*
         * //e.entityPlayer.inventory.decrStackSize(e.entityPlayer.inventory.,
         * par2) e.entityPlayer.worldObj.setBlock(e.x, e.y, e.z,
         * Block.whiteStone.blockID); e.entityPlayer.worldObj.setBlock(e.x+1,
         * e.y, e.z, Block.whiteStone.blockID);
         * e.entityPlayer.worldObj.setBlock(e.x-1, e.y, e.z,
         * Block.whiteStone.blockID); e.entityPlayer.worldObj.setBlock(e.x, e.y,
         * e.z+1, Block.whiteStone.blockID);
         * e.entityPlayer.worldObj.setBlock(e.x, e.y, e.z-1,
         * Block.whiteStone.blockID); ItemStack iSS = new
         * ItemStack(SoulShards.itemBlankShard, 1); EntityItem ei = new
         * EntityItem(e.entityPlayer.worldObj, e.x, e.y, e.z, iSS);
         * //e.entityPlayer.inventory.addItemStackToInventory(iSS);
         */
        e.setCanceled(true);
    }

    private boolean isSoulRune(World w, int x, int y, int z) {
        if (w.getBlockId(x + 2, y, z) != Block.whiteStone.blockID)
            return false;
        if (w.getBlockId(x - 2, y, z) != Block.whiteStone.blockID)
            return false;
        if (w.getBlockId(x, y, z + 2) != Block.whiteStone.blockID)
            return false;
        if (w.getBlockId(x, y, z - 2) != Block.whiteStone.blockID)
            return false;
        if (w.getBlockId(x + 1, y, z + 1) != Block.whiteStone.blockID)
            return false;
        if (w.getBlockId(x + 1, y, z - 1) != Block.whiteStone.blockID)
            return false;
        if (w.getBlockId(x - 1, y, z + 1) != Block.whiteStone.blockID)
            return false;
        if (w.getBlockId(x - 1, y, z - 1) != Block.whiteStone.blockID)
            return false;
        if (w.getBlockId(x + 1, y, z) != Block.netherrack.blockID)
            return false;
        if (w.getBlockId(x - 1, y, z) != Block.netherrack.blockID)
            return false;
        if (w.getBlockId(x, y, z + 1) != Block.netherrack.blockID)
            return false;
        if (w.getBlockId(x, y, z - 1) != Block.netherrack.blockID)
            return false;
        return true;
    }

    private Packet250CustomPayload packetSoulRune(int x, int y, int z) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            outputStream.writeInt(x);
            outputStream.writeInt(y);
            outputStream.writeInt(z);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "SoulShards";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        return packet;
    }
}
