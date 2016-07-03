package com.github.glowstone.io.slackr.Configs;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class PlayerConfig extends Config {

    /**
     * PlayerConfig constructor
     *
     * @param configDir File
     */
    public PlayerConfig(File configDir) {
        super(configDir);
        setConfigFile(new File(configDir, "players.conf"));
    }

    @Override
    protected void setDefaults() {
        // No defaults
    }

    /**
     * Is this Slack user associated with a Player?
     *
     * @param userId String
     * @return boolean
     */
    public boolean isSlackUserRegistered(String userId) {
        Iterator<Object> it = get().getChildrenMap().keySet().iterator();
        while (it.hasNext()) {
            String playerId = it.next().toString();
            ConfigurationNode node = get().getNode(playerId);
            if (node.getNode("slackId").getString("").equals(userId)) {
                if (node.getNode("token").getString("").equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Is this player associated with a slackr user?
     *
     * @param player Player
     * @return boolean
     */
    public boolean isPlayerRegistered(Player player) {
        return !get().getNode(player.getUniqueId().toString(), "slackId").getString("").isEmpty();
    }

    /**
     * Register a player with this token
     *
     * @param player Player
     * @param token  String
     * @return boolean
     */
    public boolean registerPlayer(Player player, String token) {
        if (isPlayerRegistered(player)) {
            return true;
        }

        if (token.isEmpty()) {
            return false;
        }

        Iterator<Object> it = get().getChildrenMap().keySet().iterator();
        while (it.hasNext()) {
            String temp = it.next().toString();
            if (token.equals(get().getNode(temp, "token").getString(""))) {
                String slackId = get().getNode(temp, "slackId").getString("");
                get().getNode(player.getUniqueId().toString(), "slackId").setValue(slackId);
                get().removeChild(temp);
                save();
                return true;
            }
        }

        return false;
    }

    /**
     * Remove this player's Slack information
     *
     * @param player Player
     * @return boolean
     */
    public boolean removeSlackPlayerInformation(Player player) {
        if (!isPlayerRegistered(player)) {
            return false;
        }

        get().removeChild(player.getUniqueId().toString());
        save();
        return true;
    }

    /**
     * Generate a new slackr user entry with a random token.
     *
     * @param userId String
     * @return String
     */
    public String generateSlackUserToken(String userId) {
        ConfigurationNode node = null;
        Iterator<Object> it = get().getChildrenMap().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            if (get().getNode(key, "slackId").getString("").equals(userId)) {
                node = get().getNode(key);
            }
        }
        if (node == null) {
            node = get().getNode("unknown-player-" + getNextUnknownPlayerNumber());
            node.getNode("slackId").setValue(userId);
        }
        String token = generateToken(6);
        node.getNode("token").setValue(token);
        save();
        return token;
    }

    /**
     * Gets the next unknown-player number
     *
     * @return int
     */
    private int getNextUnknownPlayerNumber() {
        ArrayList<Integer> unknowns = new ArrayList<>();
        Iterator<Object> it = get().getChildrenMap().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            if (key.startsWith("unknown-player-")) {
                unknowns.add(Integer.parseInt(key.replace("unknown-player-", "")));
            }
        }
        if (unknowns.isEmpty()) {
            return 1;
        }
        Collections.sort(unknowns);
        return unknowns.get(unknowns.size() - 1) + 1;
    }

    /**
     * Get the playerId for this Slack user
     *
     * @param slackId String
     * @return String|null
     */
    public String getPlayerId(String slackId) {
        Iterator<Object> it = get().getChildrenMap().keySet().iterator();
        while (it.hasNext()) {
            String playerId = it.next().toString();
            if (get().getNode(playerId, "slackId").getString("").equals(slackId)) {
                return playerId;
            }
        }
        return null;
    }

    /**
     * Generate a random alpha-numeric token
     *
     * @param size int
     * @return String
     */
    private String generateToken(int size) {
        String token = "";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < size; i++) {
            token += characters.charAt((int) (Math.random() * characters.length()));
        }
        return token;
    }
}
