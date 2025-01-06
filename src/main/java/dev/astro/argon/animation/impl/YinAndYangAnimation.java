package dev.astro.argon.animation.impl;

import com.cryptomorin.xseries.XMaterial;
import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.particle.type.ParticleType;
import dev.astro.argon.ArgonPlugin;
import dev.astro.argon.animation.AnimationProfile;
import dev.astro.argon.animation.IAnimation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
public class YinAndYangAnimation extends IAnimation {
    public static final int PARTICLE_COUNT = 50;
    public static final double RADIUS = 1.0;
    public static final double DOT_DARIUS = 0.2;

    public YinAndYangAnimation() {
        super("YinAndYang", XMaterial.WHITE_WOOL);
    }

    @Override
    public void play(AnimationProfile animationProfile, Location location) {
        ParticleNativeAPI particleAPI = ArgonPlugin.get().getParticleNativeAPI();
        List<Player> players = location.getWorld().getPlayers();

        animationProfile.setYinAndYangRotationAngle(animationProfile.getYinAndYangRotationAngle() + Math.toRadians(2));

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double angle = (2 * Math.PI / PARTICLE_COUNT) * i;

            spawnParticle(players, animationProfile, location, particleAPI.LIST_1_8.REDSTONE, angle, true);
            spawnParticle(players, animationProfile, location, particleAPI.LIST_1_8.SMOKE_NORMAL, angle, false);
        }

        drawDot(players, animationProfile, location, particleAPI.LIST_1_8.REDSTONE, RADIUS / 2, true);
        drawDot(players, animationProfile, location, particleAPI.LIST_1_8.SMOKE_NORMAL, RADIUS / 2, false);
    }

    private void spawnParticle(List<Player> players, AnimationProfile animationProfile, Location center, ParticleType particle, double angle, boolean isWhite) {
        double adjustedAngle = angle + (isWhite ? 0 : Math.PI);

        double x = RADIUS * Math.cos(adjustedAngle + animationProfile.getYinAndYangRotationAngle());
        double z = RADIUS * Math.sin(adjustedAngle + animationProfile.getYinAndYangRotationAngle());
        double y = isWhite ? 0.1 : -0.1;

        Location location = center.clone().add(x, y, z);
        particle.packet(true, location.getX(), location.getY(), location.getZ(), 0, 0, 0, 0, 1).sendInRadiusTo(players, 30);
    }

    private void drawDot(List<Player> players, AnimationProfile animationProfile, Location center, ParticleType particle, double offset, boolean isWhite) {
        double x = offset * Math.cos(animationProfile.getYinAndYangRotationAngle() + (isWhite ? 0 : Math.PI));
        double z = offset * Math.sin(animationProfile.getYinAndYangRotationAngle() + (isWhite ? 0 : Math.PI));
        double y = isWhite ? 0.1 : -0.1;

        Location location = center.clone().add(x, y, z);
        particle.packet(true, location.getX(), location.getY(), location.getZ(), 0, 0, 0, 0, 1).sendInRadiusTo(players, 30);
    }
}
