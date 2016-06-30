package com.github.glowstone.io.SlackSponge.Commands;

import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class UnregisterCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        Player player = (Player) src;
        Player target = args.getOne("player").isPresent() ? (Player) args.getOne("player").get() : null;

        // Unregister a target player from slack if command sender has permissions
        if (target != null && !player.equals(target)) {

            if (!player.hasPermission("slack.admin")) {
                player.sendMessage(Text.of(TextColors.RED, "You do not have permissions to unregister another player."));
                return CommandResult.empty();

            } else {

                if (SlackSponge.getPlayerConfig().removeSlackPlayerInformation(target)) {
                    player.sendMessage(Text.of(TextColors.GREEN, target.getName() + "'s Slack information has been removed."));
                } else {
                    player.sendMessage(Text.of(TextColors.RED, "No Slack registration found for " + target.getName() + "."));
                }
                return CommandResult.success();
            }
        }

        // Unregister command sender from slack
        if (SlackSponge.getPlayerConfig().removeSlackPlayerInformation(player)) {
            player.sendMessage(Text.of(TextColors.GREEN, "Your Slack information has been removed."));
        } else {
            player.sendMessage(Text.of(TextColors.RED, "No Slack registration found. You may already be unregistered."));
        }

        return CommandResult.success();
    }

}
