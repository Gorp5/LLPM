package com.gorp.auxil.content.computing.card;

import net.minecraft.item.Item;

public class DeckItem extends Item {
    public DeckItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    public static String instrutionKey = "INSTRUCTIONS";
}
