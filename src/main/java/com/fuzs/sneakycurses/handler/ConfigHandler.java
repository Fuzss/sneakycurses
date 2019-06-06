package com.fuzs.sneakycurses.handler;

import com.fuzs.sneakycurses.SneakyCurses;
import com.fuzs.sneakycurses.helper.EnumConfigColor;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = SneakyCurses.MODID)
@Mod.EventBusSubscriber
public class ConfigHandler {

	@Config.Name("Hide Curses")
	@Config.Comment("Hides curse enchantments from the item tooltip.")
	public static boolean a1HideCurses = true;

	@Config.Name("Shift Shows Curses")
	@Config.Comment("Show curses on an item if any shift key is pressed when they would otherwise be hidden.")
	public static boolean a21ShiftCurses = false;

	@Config.Name("Hide Enchantment Glint")
	@Config.Comment("Hide the enchantment glint in case an item is only enchanted with curses.")
	public static boolean a2HideGlint = true;

	@Config.Name("Hide Glint Blacklist")
	@Config.Comment("Blacklist for items that will even have an enchantment glint when only containing curses.")
	public static String[] a3hideGlintBlacklist = new String[] { "minecraft:enchanted_book" };

	@Config.Name("Curse Glint Color")
	@Config.Comment("Set the enchantment glint color for items that contain curses. Darker colors are less visible.")
	public static EnumConfigColor a4CurseGlintColor = EnumConfigColor.RED;

	@Config.Name("Quark Compat")
	@Config.Comment("Prioritise colored runes from the Quark mod over the custom curse glint color from this mod.")
	public static boolean a51QuarkCompat = true;

	@Config.Name("Item Name Color")
	@Config.Comment("Change the name color of cursed items. Vanilla default is \"magenta\", \"default\" will remove the name color from items solely containing curses.")
	public static EnumConfigColor a5NameColor = EnumConfigColor.BROWN;

	@Config.Name("Disguise NBT Tag")
	@Config.Comment("Remove one nbt tag entry in case the item is only enchanted with curses. Remove it completely when no entries are left.")
	public static boolean hideNBT = false;
	
	@SubscribeEvent
	public static void saveConfig(ConfigChangedEvent evt) {
		if (evt.getModID().equals(SneakyCurses.MODID)) {
			ConfigManager.sync(SneakyCurses.MODID, Type.INSTANCE);
		}
	}
	
}
