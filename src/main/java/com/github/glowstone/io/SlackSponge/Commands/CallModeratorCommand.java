package com.github.glowstone.io.SlackSponge.Commands;

import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.Server.SlackSender;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CallModeratorCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        Player player = (Player) src;
        String message = args.getOne("message").isPresent() ? (String) args.getOne("message").get() : "";

        if (message.isEmpty()) {
            player.sendMessage(Text.of(TextColors.RED, "Invalid command. Usage: /callmod <message>"));
            return CommandResult.success();
        }

        String channel = SlackSponge.getDefaultConfig().get().getNode(DefaultConfig.GENERAL_SETTINGS, "callModChannel").getString("");
        String username = player.getName();
        if (!channel.isEmpty()) {
            SlackSender.getWebhookInstance().sendChannelMessage(channel, username, message);
            player.sendMessage(Text.of(TextColors.GREEN, "Your message has been sent!"));
        }
        return CommandResult.success();
    }

}
