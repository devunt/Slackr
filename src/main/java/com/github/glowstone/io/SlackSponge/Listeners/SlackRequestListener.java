package com.github.glowstone.io.SlackSponge.Listeners;

import com.github.glowstone.io.SlackSponge.Commands.SlackCommandSource;
import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.Events.SlackRequestEvent;
import com.github.glowstone.io.SlackSponge.Models.SlackMessageChannel;
import com.github.glowstone.io.SlackSponge.Models.SlackRequest;
import com.github.glowstone.io.SlackSponge.Server.SlackSender;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import net.gpedro.integrations.slack.SlackMessage;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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

        if (!SlackSponge.getPlayerConfig().isSlackUserRegistered(request.getUserId())) {
            String token = SlackSponge.getPlayerConfig().generateSlackUserToken(request.getUserId());
            String message = "You have not verified you Slack account with this Minecraft server."
                    + " Please run \"/slack register " + token + "\" in game to verify your Slack account";
            SlackSender.getInstance(request.getResponseUrl()).sendCommandResponse(message);
            return;
        }

        StringBuilder message = new StringBuilder();
        switch (request.getCommand().toLowerCase().substring(1)) {

            case "mcommand":

                if (!SlackSponge.getPlayerConfig().hasCommandPrivileges(request.getUserId())) {
                    message.append("You do not have privileges to send commands to this Minecraft server.");
                    break;
                }

                if (request.getText().isEmpty()) {
                    message.append("Invalid command. Usage: /mcommand <command>");
                    break;
                }

                Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();
                taskBuilder.execute(() -> {
                    Optional<SlackCommandSource> optionalCommandSource = this.getCommandSource(request);
                    if (optionalCommandSource.isPresent()) {
                        SlackMessageChannel messageChannel = new SlackMessageChannel();
                        SlackCommandSource commandSource = optionalCommandSource.get();
                        messageChannel.getMembers().add(commandSource);
                        commandSource.setMessageChannel(messageChannel);
                        CommandManager commandManager = Sponge.getCommandManager();
                        commandManager.process(commandSource, request.getText());
                        SlackSender.getInstance(request.getResponseUrl()).sendCommandResponse(messageChannel.getCommandResult());
                        messageChannel.getMembers().remove(commandSource);
                    }
                }).name("SlackSponge - Process command from Slack").submit(SlackSponge.getInstance());
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

        SlackSender.getInstance(request.getResponseUrl()).sendCommandResponse(message.toString());
    }

    private Optional<SlackCommandSource> getCommandSource(SlackRequest request) {
        String uuid = SlackSponge.getPlayerConfig().get().getNode(request.getUserId(), "player").getString("");
        Optional<UserStorageService> optionalService = Sponge.getServiceManager().provide(UserStorageService.class);
        if (optionalService.isPresent()) {
            UserStorageService userStorageService = optionalService.get();
            Optional<User> optionalUser = userStorageService.get(UUID.fromString(uuid));
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return Optional.of(new SlackCommandSource(user));
            }
        }

        return Optional.empty();
    }

}
