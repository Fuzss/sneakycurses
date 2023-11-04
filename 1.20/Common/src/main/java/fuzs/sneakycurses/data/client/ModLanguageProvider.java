package fuzs.sneakycurses.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.sneakycurses.handler.CurseRevealHandler;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    protected void addTranslations(TranslationBuilder builder) {
        builder.add(CurseRevealHandler.KEY_ITEM_CURSES_REVEALED, "Curses revealed for %s...");
    }
}
