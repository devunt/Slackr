package com.github.glowstone.io.SlackSponge.Listeners;

import com.github.glowstone.io.SlackSponge.Events.SlackCommandEvent;
import com.github.glowstone.io.SlackSponge.Server.SlackSender;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;

import java.util.Collection;

public class SlackCommandListener {

    private static final String COMMAND_MC = "mc";
    private static final String COMMAND_TEST = "test";

    /**
     * Handle and incoming command event from Slack
     *
     * @param event SlackCommandEvent
     */
    @Listener
    public void onSlackCommand(SlackCommandEvent event) {

        switch (event.getCommand().toLowerCase().substring(1)) {

            case COMMAND_MC:
                Collection<Player> players = Sponge.getGame().getServer().getOnlinePlayers();
                StringBuilder message = new StringBuilder();
                message.append(String.valueOf(players.size()) + ", Player(s): ");
                for (Player p : players) {
                    message.append(p.getName());
                }
                SlackSender.getInstance().sendCommandResponse(message.toString());
                break;

            case COMMAND_TEST:
                SlackSender.getInstance().sendCommandResponse("test received!");
                break;

            default:
                SlackSender.getInstance().sendCommandResponse("Sponge doesn't know how to handle this command.");
                break;
        }
    }

}
