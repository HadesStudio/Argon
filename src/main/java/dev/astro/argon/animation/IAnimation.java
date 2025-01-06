package dev.astro.argon.animation;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.Location;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@Getter
public abstract class IAnimation {
    private final String name;
    private final XMaterial xMaterial;

    protected IAnimation(String name, XMaterial xMaterial) {
        this.name = name;
        this.xMaterial = xMaterial;
    }

    public abstract void play(AnimationProfile animationProfile, Location location);
}
