package com.thegreatbabushka.acidrain;

import com.mojang.logging.LogUtils;
import com.thegreatbabushka.acidrain.entity.ModEntities;
import com.thegreatbabushka.acidrain.entity.client.AcidBossModel;
import com.thegreatbabushka.acidrain.entity.client.AcidBossRenderer;
import com.thegreatbabushka.acidrain.entity.client.ModModelLayers;
import com.thegreatbabushka.acidrain.entity.custom.AcidBossEntity;
import com.thegreatbabushka.acidrain.item.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AcidRainMod.MOD_ID)
public class AcidRainMod {
    public static final String MOD_ID = "acidrain";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AcidRainMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register entities and items
        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        
        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new AcidRainEvent());
        MinecraftForge.EVENT_BUS.register(new AcidRainBlockDissolver());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Acid Rain Mod common setup");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Add items to creative tabs here
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.ACID_BOSS_SPAWN_EGG);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Acid Rain Mod server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        AcidRainCommand.register(event.getDispatcher());
        LOGGER.info("Acid Rain commands registered");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Acid Rain Mod client setup");
            EntityRenderers.register(ModEntities.ACID_BOSS.get(), AcidBossRenderer::new);
        }

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ModModelLayers.ACID_BOSS_LAYER, AcidBossModel::createBodyLayer);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.ACID_BOSS.get(), AcidBossEntity.createAttributes().build());
        }

        @SubscribeEvent
        public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
            event.register(ModEntities.ACID_BOSS.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Monster::checkMonsterSpawnRules,
                    SpawnPlacementRegisterEvent.Operation.REPLACE);
        }
    }
}
