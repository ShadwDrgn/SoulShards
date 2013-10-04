package com.shadwdrgn.soulshards;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class HandlerForgeEvents {
    @ForgeSubscribe
    public void livingDeath(LivingDeathEvent e) {
        SoulShards.proxy.livingDeath(e);
    }

    @ForgeSubscribe
    public void playerInteract(PlayerInteractEvent e) {
        SoulShards.proxy.playerInteract(e);
    }
}
