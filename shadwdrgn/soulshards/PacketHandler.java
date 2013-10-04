package com.shadwdrgn.soulshards;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager,
            Packet250CustomPayload packet, Player player) {
        if (packet.channel.equals("SoulShards")) {
            handlePacket(player, packet);
        }
    }

    private void handlePacket(Player player, Packet250CustomPayload packet) {
        EntityPlayer thePlayer = (EntityPlayer) player;
        DataInputStream inputStream = new DataInputStream(
                new ByteArrayInputStream(packet.data));
        int x, y, z;

        try {
            x = inputStream.readInt();
            y = inputStream.readInt();
            z = inputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (thePlayer.getHeldItem().itemID != Item.diamond.itemID)
            return;
        thePlayer.worldObj.setBlock(x, y, z, Block.whiteStone.blockID);
        thePlayer.worldObj.setBlock(x + 1, y, z, Block.whiteStone.blockID);
        thePlayer.worldObj.setBlock(x - 1, y, z, Block.whiteStone.blockID);
        thePlayer.worldObj.setBlock(x, y, z + 1, Block.whiteStone.blockID);
        thePlayer.worldObj.setBlock(x, y, z - 1, Block.whiteStone.blockID);
        thePlayer.getHeldItem().stackSize--;
        ItemStack iSS = new ItemStack(SoulShards.itemBlankShard, 1);
        EntityItem ei = new EntityItem(thePlayer.worldObj, x, y + 1, z, iSS);
        thePlayer.worldObj.spawnEntityInWorld(ei);
    }

}
