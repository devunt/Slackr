package com.github.glowstone.io.slackr.Commands;

import com.github.glowstone.io.slackr.Slackr;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class RegisterCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        Player player = (Player) src;
        String token = args.<String>getOne("token").get();

        if (Slackr.getPlayerConfig().isPlayerRegistered(player)) {
            player.sendMessage(Text.of(TextColors.YELLOW, "You have already verified your Slack account."));
            return CommandResult.empty();
        }

        if (!Slackr.getPlayerConfig().registerPlayer(player, token)) {
            player.sendMessage(Text.of(TextColors.YELLOW, "The token you entered is invalid, please try again."));
            return CommandResult.empty();
        }

        player.sendMessage(Text.of(TextColors.GREEN, "Thanks for verifying your Slack account!"));
        return CommandResult.success();
    }

}
