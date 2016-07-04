package com.github.glowstone.io.slackr.Listeners;

import com.github.glowstone.io.slackr.Configs.DefaultConfig;
import com.github.glowstone.io.slackr.Events.SlackRequestEvent;
import com.github.glowstone.io.slackr.Models.SlackRequest;
import com.github.glowstone.io.slackr.Runnables.NotificationSoundConsumer;
import com.github.glowstone.io.slackr.Runnables.SlackCommandRunnable;
import com.github.glowstone.io.slackr.Server.SlackSender;
import com.github.glowstone.io.slackr.Slackr;
import com.github.glowstone.io.slackr.Utilities.FormatMessageUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;

public class SlackRequestListener {

    /**
     * Handle and incoming SlackRequestEvent
     *
     * @param event SlackRequestEvent
     */
    @Listener
    public void onSlackRequest(SlackRequestEvent event) {

        if (!event.isCancelled()) {
            event.getSlackRequest().setText(FormatMessageUtil.formatIncomingMessage(event.getSlackRequest().getText()));

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
        String webhookToken = Slackr.getDefaultConfig().get().getNode(DefaultConfig.WEBHOOK_SETTINGS, "token").getString("");
        if (request.getToken().isEmpty() || !request.getToken().equals(webhookToken)) {
            Slackr.getLogger().error("The webhook token from Slack doesn't match the webhook token in slackr.conf");
            return;
        }

        if (request.getUserId().equals("USLACKBOT")) {
            // this message can be ignored.
            return;
        }

        String slackMessage = String.format("<%s> %s", request.getUsername(), request.getText());
        String slackDomain = Slackr.getDefaultConfig().get().getNode(DefaultConfig.GENERAL_SETTINGS, "slackTeamDomain").getString("");

        Text.Builder message = Text.builder();
        message.append(Text.of(TextColors.WHITE, "["));

        if (!slackDomain.isEmpty()) {
            try {

                URL url = new URL("https://" + slackDomain);
                Text link = Text.builder("Slack")
                        .onClick(TextActions.openUrl(url))
                        .onHover(TextActions.showText(Text.of(TextColors.BLUE, "Join our slackr channel: " + slackDomain)))
                        .color(TextColors.AQUA)
                        .build();
                message.append(link);

            } catch (MalformedURLException e) {
                message.append(Text.of(TextColors.AQUA, "Slack"));
            }
        }
        message.append(Text.of(TextColors.WHITE, "] " + slackMessage));
        Slackr.getInstance().getGame().getServer().getBroadcastChannel().send(message.build());

        if (Slackr.getDefaultConfig().get().getNode(DefaultConfig.GENERAL_SETTINGS, "notificationSound").getBoolean(false)) {
            ArrayList<Player> taggedPlayers = getTaggedPlayers(request.getText());
            for (Player tagged : taggedPlayers) {
                Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();
                taskBuilder.execute(new NotificationSoundConsumer(tagged)).intervalTicks(2).name("Slacker - notification").submit(Slackr.getInstance());
            }
        }
    }

    /**
     * Handle incoming commands from Slack
     *
     * @param request SlackRequest
     */
    private void handleCommandRequest(SlackRequest request) {

        // Validate command token
        if (!Slackr.getDefaultConfig().get().getNode(DefaultConfig.COMMAND_SETTINGS, "token").getString("").equals(request.getToken())) {
            Slackr.getLogger().error("The command token from Slack doesn't match the command token in slackr.conf");
            return;
        }

        // Validate slackr id is associated with a Player
        if (!Slackr.getPlayerConfig().isSlackUserRegistered(request.getUserId())) {
            String token = Slackr.getPlayerConfig().generateSlackUserToken(request.getUserId());
            String message = String.format("You have not verified you Slack account with this Minecraft server. Please run \"/slackr register %s\" in game to" +
                    " verify your Slack account", token);
            SlackSender.getInstance(request.getResponseUrl()).sendCommandResponse(message);
            return;
        }

        // Validate incoming command
        String command = Slackr.getDefaultConfig().get().getNode(DefaultConfig.COMMAND_SETTINGS, "command").getString("");
        if (!request.getCommand().toLowerCase().substring(1).equals(command.toLowerCase())) {
            String message = "The Minecraft server doesn't know how to handle this command.";
            SlackSender.getInstance(request.getResponseUrl()).sendCommandResponse(message);
            return;
        }

        // Validate command text is not empty
        if (request.getText().isEmpty()) {
            String message = String.format("Invalid command. Usage: /%s <command>", command);
            SlackSender.getInstance(request.getResponseUrl()).sendCommandResponse(message);
            return;
        }

        // Don't process command if there is no response url
        if (request.getResponseUrl().isEmpty()) {
            Slackr.getLogger().error(String.format("Slack command contained no response url: %s", request.getCommand()));
            return;
        }

        // Process the command from Slack
        Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();
        taskBuilder.execute(new SlackCommandRunnable(request)).name("slackr - Process command from Slack").submit(Slackr.getInstance());
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

}
