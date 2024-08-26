package dev.astro.argon.crate.command.provider;

import dev.astro.argon.crate.Crate;
import dev.astro.argon.crate.CrateHandler;
import dev.astro.argon.util.command.provider.Provider;
import dev.astro.argon.util.command.provider.exception.ProviderException;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrateProvider implements Provider<Crate> {

    private final CrateHandler crateHandler;

    @Override
    public Crate provide(CommandSender sender, String source) {
        Crate crate = crateHandler.getByName(source);
        if (crate != null) {
            return crate;
        }

        throw new ProviderException("Crate named '<source>' was not found!", source);
    }

    @Override
    public List<String> suggest(CommandSender sender, List<String> flags) {
        List<String> completions = new ArrayList<>();

        for (Crate crate : crateHandler.getCrates()) {
            completions.add(crate.getName());
        }

        return completions;
    }
}
