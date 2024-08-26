package dev.astro.argon.util.command.data.type;

import dev.astro.argon.util.command.data.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FlagData implements Data {

    private final List<String> names;
    private final boolean defaultValue;
    private final String description;
    private final boolean hidden;
    private final int index;

}
