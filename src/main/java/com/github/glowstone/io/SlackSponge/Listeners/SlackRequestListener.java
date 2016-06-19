package com.github.glowstone.io.SlackSponge.Listeners;

import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.Events.SlackRequestEvent;
import com.github.glowstone.io.SlackSponge.Models.SlackRequest;
import com.github.glowstone.io.SlackSponge.Server.SlackSender;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;

public class SlackRequestListener {

    /**
     * Handle and incoming SlackRequestEvent
     *
     * @param event SlackRequestEvent
     */
    @Listener
    public void onSlackRequest(SlackRequestEvent event) {

        if (!event.isCancelled()) {
            switch (event.getSlackRequest().getRequestType()) {

                case SlackRequest.REQUEST_TYPE_WEBHOOK:
                    handleWebhookRequest(event.getSlackRequest());
                    break;

                case SlackRequest.REQUEST_TYPE_COMMAND:
                    handleCommandRequest(event.getSlackRequest());
                    break;

                default:
                    event.setCancelled(true);
                    break;
            }
        }

    }

    /**
     * Handle incoming webhooks from Slack
     *
     * @param request SlackRequest
     */
    private void handleWebhookRequest(SlackRequest request) {

        // Validate webhook token
        String webhookToken = SlackSponge.getDefaultConfig().get().getNode(DefaultConfig.WEBHOOK_SETTINGS, "token").getString("");
        if (request.getToken().isEmpty() || !request.getToken().equals(webhookToken)) {
            SlackSponge.getLogger().error("The webhook token from Slack doesn't match the webhook token in SlackSponge.conf");
            return;
        }

        if (request.getUserId().equals("USLACKBOT")) {
            // this message can be ignored.
            return;
        }

        String slackMessage = String.format("<%s> %s", request.getUsername(), request.getText());

        Text.Builder message = Text.builder();
        message.append(Text.of(TextColors.WHITE, "[", TextColors.AQUA, "Slack", TextColors.WHITE, "]"));
        message.append(Text.of(TextColors.WHITE, " " + slackMessage));

        SlackSponge.getInstance().getGame().getServer().getBroadcastChannel().send(message.build());
    }

    /**
     * Handle incoming commands from Slack
     *
     * @param request SlackRequest
     */
    private void handleCommandRequest(SlackRequest request) {

        // TODO: Validate incoming command token

        StringBuilder message = new StringBuilder();

        switch (request.getCommand().toLowerCase().substring(1)) {

            case "test":
                message.append("test received!");
                break;

            case "mc":
                Collection<Player> players = Sponge.getGame().getServer().getOnlinePlayers();
                message.append(String.valueOf(players.size())).append(", Player(s): ");
                for (Player p : players) {
                    message.append(p.getName());
                }
                break;

            default:
                message.append("Sponge doesn't know how to handle this command.");
                break;

        }

        if (request.getResponseUrl().isEmpty()) {
            SlackSponge.getLogger().error(String.format("Slack command contained no response url: %s", request.getCommand()));
            return;
        }

        SlackSponge.getLogger().info(request.getResponseUrl());
        SlackSender.getInstance(request.getResponseUrl()).sendCommandResponse(message.toString());
    }

}
