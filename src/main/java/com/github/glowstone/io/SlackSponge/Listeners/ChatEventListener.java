package com.github.glowstone.io.SlackSponge.Listeners;

import com.github.glowstone.io.SlackSponge.Server.SlackSender;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.message.MessageChannelEvent;

import java.util.Optional;

public class ChatEventListener {

    /**
     * Handle chat messages from in-game chat channel
     *
     * @param event MessageChannelEvent.Chat
     */
    @Listener
    public void onChatEvent(MessageChannelEvent.Chat event) {
        Cause cause = event.getCause();
        Optional<Player> player = cause.first(Player.class);

        String text = event.getRawMessage().toPlain();

        if (player.isPresent()) {
            String username = player.get().getName();
            SlackSender.getInstance().send(text, username);
        }
    }
}
