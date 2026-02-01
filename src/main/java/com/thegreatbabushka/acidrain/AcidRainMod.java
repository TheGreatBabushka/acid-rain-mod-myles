package com.thegreatbabushka.acidrain;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
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
        }
    }
}
