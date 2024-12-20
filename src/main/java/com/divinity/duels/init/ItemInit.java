package com.divinity.duels.init;

import com.divinity.duels.Duels;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Duels.MODID);

    public static Item.Properties getItemProperties() {
        return new Item.Properties().tab(CreativeModeTab.TAB_MISC);
    }
}
