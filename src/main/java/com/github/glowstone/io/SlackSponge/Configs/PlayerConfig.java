package com.github.glowstone.io.SlackSponge.Configs;

import org.spongepowered.api.entity.living.player.Player;

import java.io.File;
import java.util.Iterator;

public class PlayerConfig extends Config {

    /**
     * Config constructor
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
     * Is this slack user associated with a player?
     *
     * @return boolean
     */
    public boolean isSlackUserRegistered(String userId) {
        String uuid = get().getNode(userId, "player").getString("");
        return !uuid.isEmpty();
    }

    /**
     * Is this player associated with a slack user?
     *
     * @param player Player
     * @return boolean
     */
    public boolean isPlayerRegistered(Player player) {
        Iterator<Object> it = get().getChildrenMap().keySet().iterator();
        while (it.hasNext()) {
            String userId = it.next().toString();
            if (player.getUniqueId().toString().equals(get().getNode(userId, "player").getString(""))) {
                return true;
            }
        }
        return false;
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

        Iterator<Object> it = get().getChildrenMap().keySet().iterator();
        while (it.hasNext()) {
            String userId = it.next().toString();
            if (token.equals(get().getNode(userId, "token").getString(""))) {
                get().getNode(userId, "player").setValue(player.getUniqueId().toString());
                get().getNode(userId).removeChild("token");
                save();
                return true;
            }
        }

        return false;
    }

    /**
     * Generate a new slack user entry with a random token.
     *
     * @param userId String
     * @return String|null
     */
    public String generateSlackUserToken(String userId) {
        if (!isSlackUserRegistered(userId)) {
            String token = generateToken(6);
            get().getNode(userId, "token").setValue(token);
            save();
            return token;
        }

        return null;
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

        Iterator<Object> it = get().getChildrenMap().keySet().iterator();
        while (it.hasNext()) {
            String userId = it.next().toString();
            if (player.getUniqueId().toString().equals(get().getNode(userId, "player").getString(""))) {
                get().removeChild(userId);
                save();
                return true;
            }
        }

        return false;
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
