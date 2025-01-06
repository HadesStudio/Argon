package dev.astro.argon.animation;

import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.animation.impl.NoneAnimation;
import dev.astro.argon.animation.impl.YinAndYangAnimation;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@Getter
public class AnimationManager {
    private final Map<String, IAnimation> animations;

    public AnimationManager(ArgonPlugin argonPlugin) {
        this.animations = new ConcurrentHashMap<>();
        this.register(new NoneAnimation());
        this.register(new YinAndYangAnimation());
        new AnimationTask(this).runTaskTimer(argonPlugin, 2L, 1L);
    }

    public void register(IAnimation animation) {
        this.animations.put(animation.getName(), animation);
    }
}
