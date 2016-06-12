package com.github.glowstone.io.SlackSponge.Server;

import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.eclipse.jetty.server.Server;

public class SlackIncomingServer {

    private Server server = null;

    public SlackIncomingServer() {

        int port = SlackSponge.getDefaultConfig().get().getNode(DefaultConfig.SLACK_SETTINGS, "port").getInt(8765);
        this.server = new Server(port);
        server.setHandler(new SlackReceiveHandler());

        try {
            server.start();
        } catch (Exception e) {
            SlackSponge.getLogger().error("Error starting SlackSponge server: " + e.getMessage());
        }
    }

}
