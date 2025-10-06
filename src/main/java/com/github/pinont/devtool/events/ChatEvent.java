package com.github.pinont.devtool.events;

import com.github.pinont.devtool.methods.SendChat;
import com.github.pinont.singularitylib.api.annotation.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@AutoRegister
public class ChatEvent implements Listener {
    @EventHandler
    public void sendChat(PlayerChatEvent event) {
        SendChat.sendChat(event);
    }
}
