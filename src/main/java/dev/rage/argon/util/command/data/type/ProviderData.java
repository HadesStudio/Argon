package dev.rage.argon.util.command.data.type;

import dev.rage.argon.util.command.data.Data;
import dev.rage.argon.util.command.provider.Provider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProviderData implements Data {

    private final String name;
    private final String defaultValue;
    private final Class<?> type;
    private final boolean wildCard;
    private final Provider provider;
    private final List<String> completionFlags;
    private final int index;

}
