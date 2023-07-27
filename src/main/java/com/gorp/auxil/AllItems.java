package com.gorp.auxil;

import com.gorp.auxil.content.DebugGogglesItem;
import com.gorp.auxil.content.computing.card.CardItem;
import com.gorp.auxil.content.computing.card.DeckItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;

/**
 * The registering class for items.
 * Registers all items added.
 */
public class AllItems {
    private static final AuxilRegistrate REGISTRATE = Auxiliaries.registrate();
    public static ItemEntry<? extends Item> debug_goggles, card, deck;
    
    static {
        debug_goggles = REGISTRATE
                .item("debug_goggles", DebugGogglesItem::new)
                .lang("Debug Goggles")
                .defaultModel()
                .register();
    
        card = REGISTRATE
                .item("card", CardItem::new)
                .lang("Punch Card")
                .defaultModel()
                .register();
    
        deck = REGISTRATE
                .item("deck", DeckItem::new)
                .lang("Card Deck")
                .defaultModel()
                .register();
    }
    
    public static void register() {}
}
