# Slackr

Slacker is a Slack integration for [Sponge](https://www.spongepowered.org) Minecraft servers.

**Features**

* Chat between Minecraft and Slack!

* Send messages to the #admin channel in Slack from game!

* Send Minecraft commands directly from Slack!

## Permissions

"slack.use"

"slack.callmod"

"slack.admin"

## Commands

* `/slack register XXXXXX` - Register you Slack user in Minecraft

* `/slack unregister <player>` - Unregister your Slack user or the Slack user of another player (unregisterring another player requires the "slack.admin" permission)

* `/callmod [message]` - Send a Slack message to the #admin channel in Slack. (the channel is configurable, requires "slack.callmod" permissions)

## Setup

**Chat between Minecraft and Slack**

1. From your Slack account, go to: "Apps and Integrations". Select "Build". Select "Make a custom integration". Select **"Incoming WebHooks"**.

2. Enter the desired channel you want your Minecraft messages to go to. Then Select "Add Incoming Webhook Integration".

3. Copy the `Webhook URL` from this page. You can also change the label of the incoming webhook and the default icon on this page.

4. Select "Save Settings".

5. Paste the `Webhook URL` from step 3 into Slackr.conf on your Minecraft server:

    ```
    "Webhook Settings" {
        playerDeath=true
        playerJoin=true
        playerLeave=true
        token=
        webhookUrl="https://hooks.slack.com/services/XXXXXXXXX/XXXXXXXXX/XXXXXXXXXXXXXXXXXXXXXXXX"
    }
    ```

6. Go back to "Apps and Integrations". Select "Build". Select "Make a Custom Integration". Select **"Outgoing WebHooks"**.

7. Select the desired channel you want your Slack messages to be sent from.

8. Enter your Minecraft servers ip or url with port `8765`: `http://123.456.78.90:8765/` or `http://minecraft.myserver.com:8765/`

9. Copy the `token` from this page. You can also change the label of the outgoing webhook and default icon on this page.

10. Select "Save Settings".

11. Paste the `token` from step 9 into Slackr.conf on you Minecraft server:

    ```
    "Webhook Settings" {
        playerDeath=true
        playerJoin=true
        playerLeave=true
        token="XXXXXXXXXXXXXXXXXXXXXXXX"
        webhookUrl="https://hooks.slack.com/services/XXXXXXXXX/XXXXXXXXX/XXXXXXXXXXXXXXXXXXXXXXXX"
    }
    ```

12. Restart your Minecraft server, or reload the plugin: `/sponge plugins reload`

**Setup Minecraft commands in Slack**

1. From your Slack account, go to: "Apps and Integrations". Select "Build". Select "Make a Custom Integration". Select **"Slash Commands"**.

2. Enter the desired command e.g. `/mc`. Then Select "Add Slash Command Integration".

3. Enter your Minecraft servers ip or url with port `8765`: `http://123.456.78.90:8765/` or `http://minecraft.myserver.com:8765/`

4. Copy the `token` from this page. You can also change the label of the command, default icon, and description text on this page.

5. Select "Save Integration".

6. Paste the `token` from step 4 and enter the command from step 2 into Slackr.conf on your Minecraft server:

    ```
    "Command Settings" {
        command=mc
        token="XXXXXXXXXXXXXXXXXXXXXXXX"
    }
    ```

7. Restart your Minecraft server, or reload the plugin: `/sponge plugins reload`