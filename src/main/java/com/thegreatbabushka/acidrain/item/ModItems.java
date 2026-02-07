package com.thegreatbabushka.acidrain.item;

import com.thegreatbabushka.acidrain.AcidRainMod;
import com.thegreatbabushka.acidrain.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AcidRainMod.MOD_ID);

    public static final RegistryObject<Item> ACID_BOSS_SPAWN_EGG = ITEMS.register("acid_boss_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ACID_BOSS, 0x7FFF00, 0x228B22,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
