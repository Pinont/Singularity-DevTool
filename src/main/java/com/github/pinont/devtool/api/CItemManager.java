package com.github.pinont.devtool.api;

import com.github.pinont.singularitylib.api.items.CustomItem;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CItemManager {

    private final Collection<CustomItem> customItems = Collections.synchronizedCollection(List.of());

    public Collection<CustomItem> getCustomItems() {
        return this.customItems;
    }

    public void registerCustomItems(CustomItem... customItems) {
        Collections.addAll(this.customItems, customItems);
    }
}
