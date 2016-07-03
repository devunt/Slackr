package com.github.glowstone.io.slackr.Runnables;

import com.github.glowstone.io.slackr.Models.SlackCommandSource;
import com.github.glowstone.io.slackr.Models.SlackMessageChannel;
import com.github.glowstone.io.slackr.Models.SlackRequest;
import com.github.glowstone.io.slackr.Server.SlackSender;
import com.github.glowstone.io.slackr.Slackr;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Optional;
import java.util.UUID;

public class SlackCommandRunnable implements Runnable {

    private SlackRequest request;

    /**
     * SlackCommandRunnable constructor.
     *
     * @param request SlackRequest
     */
    public SlackCommandRunnable(SlackRequest request) {
        this.request = request;
    }

    @Override
    public void run() {

        String command = request.getText();
        command = command.startsWith("/") ? command.substring(1) : command;

        // Get the player's uuid
        UUID uuid;
        String uuidString = Slackr.getPlayerConfig().getPlayerId(request.getUserId());
        try {
            uuid = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            Slackr.getLogger().error(e.getMessage());
            return;
        }

        // Get the user storage service
        Optional<UserStorageService> optionalService = Sponge.getServiceManager().provide(UserStorageService.class);
        if (!optionalService.isPresent()) {
            Slackr.getLogger().error("User storage service not found.");
            return;
        }

        // Get the user
        UserStorageService userStorageService = optionalService.get();
        Optional<User> optionalUser = userStorageService.get(uuid);
        if (!optionalUser.isPresent()) {
            Slackr.getLogger().error("User not found.");
            return;
        }

        // Get the command source
        User user = optionalUser.get();
        CommandSource commandSource = new SlackCommandSource(user);

        // Set command sources's message channel
        SlackMessageChannel slackChannel = new SlackMessageChannel();
        slackChannel.addMember(commandSource);
        commandSource.setMessageChannel(slackChannel);

        // Process the command
        CommandManager commandManager = Sponge.getCommandManager();
        commandManager.process(commandSource, command);

        // Send command response
        SlackSender.getInstance(request.getResponseUrl()).sendCommandResponse(slackChannel.getCommandResult());
        slackChannel.clearMembers();
    }

}
