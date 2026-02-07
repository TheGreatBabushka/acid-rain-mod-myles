package com.thegreatbabushka.acidrain.entity;

import com.thegreatbabushka.acidrain.AcidRainMod;
import com.thegreatbabushka.acidrain.entity.custom.AcidBossEntity;
import com.thegreatbabushka.acidrain.entity.custom.AcidProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AcidRainMod.MOD_ID);

    public static final RegistryObject<EntityType<AcidBossEntity>> ACID_BOSS =
            ENTITY_TYPES.register("acid_boss", () -> EntityType.Builder.of(AcidBossEntity::new, MobCategory.MONSTER)
                    .sized(2.04f, 2.04f)
                    .build("acid_boss"));

    public static final RegistryObject<EntityType<AcidProjectileEntity>> ACID_PROJECTILE =
            ENTITY_TYPES.register("acid_projectile", () -> EntityType.Builder.<AcidProjectileEntity>of(AcidProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("acid_projectile"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
