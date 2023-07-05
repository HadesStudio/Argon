package dev.rage.lootchests.crate.command.provider;

import dev.rage.lootchests.crate.Crate;
import dev.rage.lootchests.crate.CrateHandler;
import dev.rage.lootchests.util.command.provider.Provider;
import dev.rage.lootchests.util.command.provider.exception.ProviderException;
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

        throw new ProviderException(source, "Crate named '<source>' was not found!");
    }

    @Override
    public List<String> suggest(CommandSender sender, String source) {
        List<String> completions = new ArrayList<>();

        for (Crate crate : crateHandler.getCrates()) {
            if (match(crate.getName(), source)) {
                completions.add(crate.getName());
            }
        }

        return completions;
    }
}
