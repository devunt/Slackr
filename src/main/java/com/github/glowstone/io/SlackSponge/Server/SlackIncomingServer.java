package com.github.glowstone.io.SlackSponge.Server;

import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class SlackIncomingServer {

    private Server server = null;

    /**
     * SlackIncomingServer constructor
     */
    public SlackIncomingServer() {

        int port = SlackSponge.getDefaultConfig().get().getNode(DefaultConfig.SLACK_SETTINGS, "port").getInt(8765);
        this.server = new Server(port);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(SlackReceiveServlet.class, "/*");

        try {
            server.start();
        } catch (Exception e) {
            SlackSponge.getLogger().error("Error starting SlackSponge server: " + e.getMessage());
        }
    }

}
