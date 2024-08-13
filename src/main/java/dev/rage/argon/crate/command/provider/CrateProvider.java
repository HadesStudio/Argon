package dev.rage.argon.crate.command.provider;

import dev.rage.argon.crate.Crate;
import dev.rage.argon.crate.CrateHandler;
import dev.rage.argon.util.command.provider.Provider;
import dev.rage.argon.util.command.provider.exception.ProviderException;
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
