package com.github.glowstone.io.SlackSponge.Server;

import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.Events.SlackCommandEvent;
import com.github.glowstone.io.SlackSponge.Events.SlackMessageEvent;
import com.github.glowstone.io.SlackSponge.Models.SlackRequest;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.spongepowered.api.Sponge;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SlackReceiveServlet extends HttpServlet {

    /**
     * Handle a POST request
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        SlackRequest slackRequest = new SlackRequest(request);

        if (!slackRequest.getUserId().equals("USLACKBOT")) {

            if (isValidCommandToken(slackRequest.getCommand(), slackRequest.getToken())) {
                SlackCommandEvent event = new SlackCommandEvent(slackRequest.getUsername(), slackRequest.getCommand(), slackRequest.getText());
                Sponge.getEventManager().post(event);
                return;
            }

            if (isValidMessageToken(slackRequest.getToken())) {
                SlackMessageEvent event = new SlackMessageEvent(slackRequest.getChannelName(), slackRequest.getUsername(), slackRequest.getText());
                Sponge.getEventManager().post(event);
                return;
            }

            SlackSponge.getLogger().error("Token from Slack is invalid.");
        }
    }

    /**
     * Does this request contain a valid message token?
     *
     * @param token String
     * @return boolean
     */
    private boolean isValidMessageToken(String token) {
        String defaultToken = SlackSponge.getDefaultConfig().get().getNode(DefaultConfig.WEBHOOK_SETTINGS, "token").getString("");
        return (!token.isEmpty() && token.equals(defaultToken));
    }

    /**
     * Does this request contain a valid command token?
     *
     * @param command String
     * @param token   String
     * @return boolean
     */
    private boolean isValidCommandToken(String command, String token) {
        // TODO: add more verification here for command tokens
        return (!command.isEmpty() && !token.isEmpty());
    }

}
