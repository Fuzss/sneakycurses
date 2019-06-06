package com.fuzs.sneakycurses;

import com.fuzs.sneakycurses.handler.CurseTooltipHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = SneakyCurses.MODID,
        name = SneakyCurses.NAME,
        version = SneakyCurses.VERSION,
        acceptedMinecraftVersions = SneakyCurses.RANGE,
        clientSideOnly = SneakyCurses.CLIENT,
        certificateFingerprint = SneakyCurses.FINGERPRINT
)
public class SneakyCurses
{
    public static final String MODID = "sneakycurses";
    public static final String NAME = "Sneaky Curses";
    public static final String VERSION = "@VERSION@";
    public static final String RANGE = "[1.12.2]";
    public static final boolean CLIENT = true;
    public static final String FINGERPRINT = "@FINGERPRINT@";

    public static final Logger LOGGER = LogManager.getLogger(SneakyCurses.NAME);

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new CurseTooltipHandler());

    }

    @EventHandler
    public void fingerprintViolation(FMLFingerprintViolationEvent event) {
        LOGGER.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}
