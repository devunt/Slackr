package com.github.glowstone.io.slackr.Server;

import com.github.glowstone.io.slackr.Configs.DefaultConfig;
import com.github.glowstone.io.slackr.Slackr;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class SlackIncomingServer {

    private Server server = null;

    /**
     * SlackIncomingServer constructor
     */
    public SlackIncomingServer() {

        int port = Slackr.getDefaultConfig().get().getNode(DefaultConfig.GENERAL_SETTINGS, "port").getInt(8765);
        this.server = new Server(port);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(SlackReceiveServlet.class, "/*");

        try {
            server.start();
        } catch (Exception e) {
            Slackr.getLogger().error("Error starting slackr server: " + e.getMessage());
        }
    }

}
