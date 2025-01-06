package dev.astro.argon.animation;

import com.github.juliarn.npclib.api.Position;
import dev.astro.argon.ArgonPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@RequiredArgsConstructor
public class AnimationTask extends BukkitRunnable {
    private final Map<Object, AnimationProfile> profiles = new ConcurrentHashMap<>();
    private final AnimationManager animationManager;

    @Override
    public void run() {
        ArgonPlugin.get().getCrateHandler().getCrates().forEach(crate -> {
            ArgonPlugin.get().getNpcRegistrar().getNpcs().forEach((npc, npcProfile) -> {
                if (crate.getName().equals(npcProfile.getCrateName())) {
                    IAnimation animation = this.animationManager.getAnimations().get(npcProfile.getAnimation());
                    if (animation == null) {
                        return;
                    }

                    AnimationProfile profile = this.profiles.computeIfAbsent(npcProfile, k -> new AnimationProfile());
                    Position position = npc.position();
                    animation.play(profile, new Location(npc.world(), position.x(), position.y(), position.z()));
                }
            });


            if (crate.getAnimation() == null || crate.getAnimation().isEmpty()) {
                return;
            }
            IAnimation iAnimation = this.animationManager.getAnimations().get(crate.getAnimation());
            if (iAnimation == null) {
                return;
            }
            crate.getLocations().forEach(location -> {
                AnimationProfile profile = this.profiles.computeIfAbsent(location, k -> new AnimationProfile());
                iAnimation.play(profile, location.clone().add(0.5, 0.0, 0.5));
            });
        });
    }
}
