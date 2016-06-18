package com.github.glowstone.io.SlackSponge.Listeners;

import com.github.glowstone.io.SlackSponge.Events.SlackMessageEvent;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SlackMessageListener {

    /**
     * Handle an incoming message event from Slack
     *
     * @param event SlackMessageEvent
     */
    @Listener
    public void onSlackMessage(SlackMessageEvent event) {
        Text.Builder message = Text.builder();
        message.append(Text.of(TextColors.WHITE, "[", TextColors.AQUA, "Slack", TextColors.WHITE, "]"));
        message.append(Text.of(TextColors.WHITE, " " + event.getMessage()));

        SlackSponge.getInstance().getGame().getServer().getBroadcastChannel().send(message.build());
    }
}
