package com.github.glowstone.io.SlackSponge.Listeners;

import com.github.glowstone.io.SlackSponge.Events.SlackCommandEvent;
import com.github.glowstone.io.SlackSponge.Server.SlackSender;
import org.spongepowered.api.event.Listener;

public class SlackCommandListener {

    public static final String COMMAND_MC = "mc";

    @Listener
    public void onSlackCommand(SlackCommandEvent event) {

        switch (event.getCommand().toLowerCase().substring(1)) {

            case COMMAND_MC:
                // do mc command here
                break;

            default:
                SlackSender.getInstance().sendCommandResponse("Sponge doesn't know how to handle this command.");
                break;
        }
    }

}
