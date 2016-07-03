package com.github.glowstone.io.SlackSponge.Utilities;

import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatMessageUtil {

    private static final Pattern PATTERN_TYPE_USERNAME = Pattern.compile("<@(\\w+)>");
    private static final Pattern PATTERN_TYPE_PLAYER = Pattern.compile("@(\\w+)");

    /**
     * Format incoming messages from Slack.
     *
     * @param text String
     * @return String
     */
    public static String formatIncomingMessage(String text) {
        String result = text;
        Matcher matcher = PATTERN_TYPE_USERNAME.matcher(text);
        while (matcher.find()) {
            String match = matcher.group();
            String slackId = matcher.group(1);
            result = result.replace(match, getPlayerName(slackId));
        }
        return result;
    }

    /**
     * Format outgoing messages to Slack.
     *
     * @param text String
     * @return String
     */
    public static String formatOutgoingMessage(String text) {
        String result = text;
        Matcher matcher = PATTERN_TYPE_PLAYER.matcher(text);
        while (matcher.find()) {
            String match = matcher.group();
            String player = matcher.group(1);
            result = result.replace(match, getSlackLink(player));
        }
        return result;
    }

    /**
     * Get a Player's name from the incoming slack id.
     *
     * @param slackId String
     * @return String
     */
    private static String getPlayerName(String slackId) {
        String playerId = SlackSponge.getPlayerConfig().getPlayerId(slackId);
        if (playerId == null) {
            return slackId;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(playerId);
        } catch (Exception e) {
            return slackId;
        }

        Optional<UserStorageService> optionalService = Sponge.getServiceManager().provide(UserStorageService.class);
        if (!optionalService.isPresent()) {
            return slackId;
        }

        UserStorageService userStorageService = optionalService.get();
        Optional<User> optionalUser = userStorageService.get(uuid);
        if (!optionalUser.isPresent()) {
            return slackId;
        }

        User user = optionalUser.get();
        return user.getName();
    }

    /**
     * Get this Player's Slack id.
     *
     * @param playerName String
     * @return String
     */
    private static String getSlackLink(String playerName) {
        Optional<UserStorageService> optionalService = Sponge.getServiceManager().provide(UserStorageService.class);
        if (!optionalService.isPresent()) {
            return playerName;
        }

        UserStorageService userStorageService = optionalService.get();
        Optional<User> optionalUser = userStorageService.get(playerName);
        if (!optionalUser.isPresent()) {
            return playerName;
        }

        User user = optionalUser.get();
        String slackId = SlackSponge.getPlayerConfig().get().getNode(user.getIdentifier(), "slackId").getString("");
        return (slackId.isEmpty()) ? playerName : "<@" + slackId + ">";
    }
}
