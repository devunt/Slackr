package com.github.glowstone.io.slackr.Listeners;

import com.github.glowstone.io.slackr.Server.SlackSender;
import com.github.glowstone.io.slackr.Slackr;
import com.github.glowstone.io.slackr.Utilities.FormatMessageUtil;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Optional;

public class OutgoingMessageListener {

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
        String formattedText = FormatMessageUtil.formatOutgoingMessage(text);

        if (player.isPresent()) {
            String username = player.get().getName();
            SlackSender.getWebhookInstance().sendMessage(formattedText, username);
        }
    }

    /**
     * Handle player death messages
     *
     * @param event DestructEntityEvent.Death
     */
    @Listener
    public void onPlayerDeathEvent(DestructEntityEvent.Death event) {
        Entity target = event.getTargetEntity();
        if (target instanceof Player) {
            if (Slackr.getDefaultConfig().allowDeathMessages()) {
                SlackSender.getWebhookInstance().sendMessage(event.getMessage().toPlain());
            }
        }
    }

    /**
     * Handle player join messages
     *
     * @param event ClientConnectionEvent.Join
     */
    @Listener
    public void onPlayerJoinEvent(ClientConnectionEvent.Join event) {
        if (Slackr.getDefaultConfig().allowJoinMessages()) {
            SlackSender.getWebhookInstance().sendMessage(event.getMessage().toPlain());
        }
    }

    /**
     * Handle player leave messages
     *
     * @param event ClientConnectionEvent.Disconnect
     */
    @Listener
    public void onPlayerLeaveEvent(ClientConnectionEvent.Disconnect event) {
        if (Slackr.getDefaultConfig().allowLeaveMessages()) {
            SlackSender.getWebhookInstance().sendMessage(event.getMessage().toPlain());
        }
    }

}
