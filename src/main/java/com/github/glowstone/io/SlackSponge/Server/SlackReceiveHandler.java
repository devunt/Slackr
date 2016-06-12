package com.github.glowstone.io.SlackSponge.Server;

import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.Events.SlackMessageEvent;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.spongepowered.api.Sponge;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SlackReceiveHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (request.getMethod().equals("POST")) {
            String username = request.getParameter("user_name");

            if (!username.equals("slackbot")) {
                String channel = request.getParameter("channel_name");
                String text = request.getParameter("text");
                String token = request.getParameter("token");
                String configToken = SlackSponge.getDefaultConfig().get().getNode(DefaultConfig.SLACK_SETTINGS, "token").getString("");

                if (!token.isEmpty() && token.equals(configToken)) {
                    SlackMessageEvent event = new SlackMessageEvent(channel, username, text);
                    Sponge.getEventManager().post(event);

                } else {
                    SlackSponge.getLogger().error("Token from Slack is invalid.");
                }
            }
        }
    }

}
