package com.wolfyscript.utilities.bukkit.nbt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import me.wolfyscript.utilities.util.NamespacedKey;

import java.util.Optional;

public class QueryNodeInt extends QueryNodePrimitive<Integer> {

    public static final NamespacedKey TYPE = NamespacedKey.wolfyutilties("int");

    @JsonCreator
    public QueryNodeInt(@JsonProperty("value") JsonNode valueNode, String key, String parentPath) {
        super(TYPE, valueNode.isTextual() ? Integer.parseInt(valueNode.asText().replaceAll("[iI]", "")) : valueNode.numberValue().intValue(), key, parentPath);
    }

    @Override
    protected Optional<Integer> readValue(String path, String key, NBTCompound parent) {
        return Optional.ofNullable(parent.getInteger(key));
    }

    @Override
    protected void applyValue(String path, String key, Integer value, NBTCompound resultContainer) {
        resultContainer.setInteger(key, value);
    }

}
