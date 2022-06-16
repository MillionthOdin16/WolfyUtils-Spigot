/*
 *       ____ _  _ ____ ___ ____ _  _ ____ ____ ____ ____ ___ _ _  _ ____
 *       |    |  | [__   |  |  | |\/| |    |__/ |__| |___  |  | |\ | | __
 *       |___ |__| ___]  |  |__| |  | |___ |  \ |  | |     |  | | \| |__]
 *
 *       CustomCrafting Recipe creation and management tool for Minecraft
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.wolfyscript.utilities.bukkit.nbt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import me.wolfyscript.utilities.util.json.jackson.JacksonUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NBTQuery {

    private final Map<String, QueryNode<?>> nodes = new HashMap<>();

    @JsonCreator
    public NBTQuery(ObjectNode node) {
        node.fields().forEachRemaining(entry -> {
            var key = entry.getKey();
            var subNode = entry.getValue();
            QueryNode.loadFrom(subNode, "", key).ifPresent(queryNode -> {
                nodes.put(key, queryNode);
            });
        });

    }

    public static Optional<NBTQuery> of(File file) {
        try {
            return Optional.ofNullable(JacksonUtil.getObjectMapper().readValue(file, NBTQuery.class));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public NBTCompound computeOn(NBTCompound input) {
        NBTContainer container = new NBTContainer();
        nodes.forEach((key, queryNode) -> queryNode.visit("", key, input, container));
        return container;
    }


}
