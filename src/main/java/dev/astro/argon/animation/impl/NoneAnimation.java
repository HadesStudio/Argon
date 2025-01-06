package dev.astro.argon.animation.impl;

import com.cryptomorin.xseries.XMaterial;
import dev.astro.argon.animation.AnimationProfile;
import dev.astro.argon.animation.IAnimation;
import org.bukkit.Location;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
public class NoneAnimation extends IAnimation {
    public NoneAnimation() {
        super("None", XMaterial.BARRIER);
    }

    @Override
    public void play(AnimationProfile animationProfile, Location location) {
        // Do nothing
    }
}
