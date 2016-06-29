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

public class AdminCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!(src instanceof Player)) {
            return CommandResult.empty();
        }

        Player player = (Player) src;
        boolean add = args.hasAny("a");
        boolean remove = args.hasAny("r");
        boolean delete = args.hasAny("d");
        Player target = args.getOne("player").isPresent() ? (Player) args.getOne("player").get() : null;

        if ((add && remove && delete) || (add && remove) || (add && delete) || (remove && delete)) {
            player.sendMessage(Text.of(TextColors.RED, "Command invalid, you cannot use multiple flags when running this command."));
            return CommandResult.empty();
        }

        if ((add || remove || delete) && target == null) {
            player.sendMessage(Text.of(TextColors.RED, "Command invalid, please include a player."));
            return CommandResult.empty();
        }

        if (!add && !remove && !delete) {
            if (target == null) {
                // TODO: list players and their statuses
            } else {
                // TODO: show this players status
            }
            return CommandResult.success();
        }

        // Delete this player's Slack information
        if (delete) {
            if (SlackSponge.getPlayerConfig().deleteSlackPlayerInformation(target)) {
                player.sendMessage(Text.of(TextColors.YELLOW, target.getName() + "'s Slack information has been removed."));
            } else {
                player.sendMessage(Text.of(TextColors.RED, target.getName() + " is not a valid Slack user. They may be unregistered."));
            }
            return CommandResult.success();
        }

        // Remove privilege to send command from Slack from this player
        if (remove) {
            if (SlackSponge.getPlayerConfig().setPlayerPrivilages(target, false)) {
                player.sendMessage(Text.of(TextColors.YELLOW, "Removed command privileges from " + target.getName()));
            } else {
                player.sendMessage(Text.of(TextColors.RED, target.getName() + " is not a valid Slack user. They may be unregistered."));
            }
            return CommandResult.success();
        }

        // Add privilege to send command from Slack for this player
        if (SlackSponge.getPlayerConfig().setPlayerPrivilages(target, true)) {
            player.sendMessage(Text.of(TextColors.GREEN, "Removed command privileges from " + target.getName()));
        } else {
            player.sendMessage(Text.of(TextColors.RED, target.getName() + " is not a valid Slack user. They may be unregistered."));
        }
        return CommandResult.success();
    }

}
