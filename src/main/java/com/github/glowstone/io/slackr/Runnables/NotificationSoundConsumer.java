package com.github.glowstone.io.slackr.Runnables;

import org.spongepowered.api.effect.sound.PitchModulation;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.function.Consumer;

public class NotificationSoundConsumer implements Consumer<Task> {

    private Player player;
    private int repeat = 3;

    /**
     * NotificationSoundConsumer
     *
     * @param player Player
     */
    public NotificationSoundConsumer(Player player) {
        this.player = player;
    }

    @Override
    public void accept(Task task) {
        repeat--;
        player.playSound(SoundTypes.NOTE_STICKS, player.getLocation().getPosition(), 2, PitchModulation.C1);
        if (repeat < 1) {
            task.cancel();
        }
    }
}
