package com.github.glowstone.io.slackr.Server;

import com.github.glowstone.io.slackr.Events.SlackRequestEvent;
import com.github.glowstone.io.slackr.Models.SlackRequest;
import com.github.glowstone.io.slackr.Slackr;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class SlackReceiveServlet extends HttpServlet {

    private static final String[] validParams = {
            "token", "team_id", "team_domain", "channel_id",  "channel_name", "user_id", "user_name", "text", "timestamp", "trigger_word", "service_id",
            "command", "response_url", "bot_name", "bot_id"
    };

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

        if (!isValidSlackMessage(request)) {
            return;
        }

        int requestType = request.getParameter("command") != null ? SlackRequest.REQUEST_TYPE_COMMAND : SlackRequest.REQUEST_TYPE_WEBHOOK;
        SlackRequest slackRequest = new SlackRequest(request, requestType);

        SlackRequestEvent event = new SlackRequestEvent(slackRequest);
        Sponge.getEventManager().post(event);
    }

    /**
     * Are the incoming parameters valid?
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    private boolean isValidSlackMessage(HttpServletRequest request) {

        Enumeration<String> payload = request.getParameterNames();
        ArrayList<String> invalidParams = new ArrayList<>();
        while (payload.hasMoreElements()) {
            String param = payload.nextElement();
            if (!Arrays.asList(validParams).contains(param)) {
                invalidParams.add(param);
            }
        }

        if (!invalidParams.isEmpty()) {
            Slackr.getLogger().error("The incoming payload contains invalid parameters: " + StringUtils.join(invalidParams, ", "));
            return false;
        }

        return true;
    }

}
