package fuzs.sneakycurses.client.gui.screens.inventory;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class SimpleEnchantmentNames {
   public static final SimpleEnchantmentNames INSTANCE = new SimpleEnchantmentNames();
   private static final ResourceLocation ALT_FONT = new ResourceLocation("minecraft", "alt");
   private static final Style ROOT_STYLE = Style.EMPTY.withFont(ALT_FONT);

   public final RandomSource random = RandomSource.create();
   private final String[] words = new String[]{"the", "elder", "scrolls", "klaatu", "berata", "niktu", "xyzzy", "bless", "curse", "light", "darkness", "fire", "air", "earth", "water", "hot", "dry", "cold", "wet", "ignite", "snuff", "embiggen", "twist", "shorten", "stretch", "fiddle", "destroy", "imbue", "galvanize", "enchant", "free", "limited", "range", "of", "towards", "inside", "sphere", "cube", "self", "other", "ball", "mental", "physical", "grow", "shrink", "demon", "elemental", "spirit", "animal", "creature", "beast", "humanoid", "undead", "fresh", "stale", "phnglui", "mglwnafh", "cthulhu", "rlyeh", "wgahnagl", "fhtagn", "baguette"};

   private SimpleEnchantmentNames() {

   }

   public Component getRandomName(int maxWidth) {
      StringBuilder stringbuilder = new StringBuilder();
      while (stringbuilder.length() <= maxWidth) {
         if (!stringbuilder.isEmpty())
            stringbuilder.append(" ");
         stringbuilder.append(Util.getRandom(this.words, this.random));
      }
      stringbuilder.delete(stringbuilder.lastIndexOf(" "), stringbuilder.length());
      return Component.literal(stringbuilder.toString()).withStyle(ROOT_STYLE);
   }

   public void initSeed(long seed) {
      this.random.setSeed(seed);
   }
}