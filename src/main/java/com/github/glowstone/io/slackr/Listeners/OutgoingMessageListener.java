package com.github.glowstone.io.slackr.Listeners;

import com.github.glowstone.io.slackr.Configs.DefaultConfig;
import com.github.glowstone.io.slackr.Runnables.NotificationSoundConsumer;
import com.github.glowstone.io.slackr.Server.SlackSender;
import com.github.glowstone.io.slackr.Slackr;
import com.github.glowstone.io.slackr.Utilities.FormatMessageUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;

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

        if (Slackr.getDefaultConfig().get().getNode(DefaultConfig.GENERAL_SETTINGS, "notificationSound").getBoolean(false)) {
            ArrayList<Player> taggedPlayers = getTaggedPlayers(text);
            for (Player tagged : taggedPlayers) {
                Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();
                taskBuilder.execute(new NotificationSoundConsumer(tagged)).intervalTicks(2).name("Slackr - notification").submit(Slackr.getInstance());
            }
        }

        if (player.isPresent()) {
            String username = player.get().getName();
            SlackSender.getWebhookInstance().sendMessage(formattedText, username);
        }
    }

    /**
     * Get tagged players from message.
     *
     * @param text String
     * @return ArrayList<Player>
     */
    private ArrayList<Player> getTaggedPlayers(String text) {
        ArrayList<Player> players = new ArrayList<>();
        Matcher matcher = FormatMessageUtil.PATTERN_TYPE_PLAYER.matcher(text);
        Collection<Player> onlinePlayers = Sponge.getServer().getOnlinePlayers();
        while (matcher.find()) {
            String playerName = matcher.group(1);
            for (Player player : onlinePlayers) {
                if (player.getName().toLowerCase().equals(playerName)) {
                    players.add(player);
                }
            }
        }

        return players;
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
                String text = event.getMessage().toPlain();
                String formattedText = String.format("[%s]", text);

                String username = ((Player) target).getName();
                SlackSender.getWebhookInstance().sendMessage(formattedText, username);
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
            Cause cause = event.getCause();
            Optional<Player> player = cause.first(Player.class);

            String text = event.getMessage().toPlain();
            String formattedText = String.format("[%s]", text);

            if (player.isPresent()) {
                String username = player.get().getName();
                SlackSender.getWebhookInstance().sendMessage(formattedText, username);
            }
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
            Cause cause = event.getCause();
            Optional<Player> player = cause.first(Player.class);

            String text = event.getMessage().toPlain();
            String formattedText = String.format("[%s]", text);

            if (player.isPresent()) {
                String username = player.get().getName();
                SlackSender.getWebhookInstance().sendMessage(formattedText, username);
            }
        }
    }
}
