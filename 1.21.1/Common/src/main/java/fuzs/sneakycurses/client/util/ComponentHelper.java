package fuzs.sneakycurses.client.util;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A helper class for converting various text representations used by vanilla to {@link Component}.
 * <p>Useful for text instances returned from {@link net.minecraft.client.StringSplitter} and {@link Component#getVisualOrderText()}.
 */
public class ComponentHelper {

    /**
     * Converts an instance of {@link FormattedText} to a {@link Component}.
     *
     * @param formattedText the text to convert
     * @return the new component
     */
    public static Component toComponent(FormattedText formattedText) {
        return new FormattedContentSink(formattedText).getComponent();
    }

    /**
     * Converts an instance of {@link FormattedCharSequence} to a {@link Component}.
     *
     * @param formattedCharSequence the text to convert
     * @return the new component
     */
    public static Component toComponent(FormattedCharSequence formattedCharSequence) {
        return new FormattedContentSink(formattedCharSequence).getComponent();
    }

    private static class FormattedContentSink implements FormattedText.StyledContentConsumer<Unit>, FormattedCharSink {
        private final List<Map.Entry<StringBuilder, Style>> builders = Lists.newArrayList(Map.entry(new StringBuilder(), Style.EMPTY));
        @Nullable
        private Component component;

        public FormattedContentSink(FormattedText formattedText) {
            if (formattedText instanceof Component) {
                this.component = ((Component) formattedText).copy();
            } else {
                formattedText.visit(this, Style.EMPTY);
            }
        }

        public FormattedContentSink(FormattedCharSequence formattedCharSequence) {
            formattedCharSequence.accept(this);
        }

        @Override
        public Optional<Unit> accept(Style style, String string) {
            Map.Entry<StringBuilder, Style> entry = this.builders.get(this.builders.size() - 1);
            if (entry.getValue() == style) {
                entry.getKey().append(string);
            } else {
                this.builders.add(Map.entry(new StringBuilder(string), style));
            }
            return Optional.empty();
        }

        @Override
        public boolean accept(int width, Style style, int codePoint) {
            this.accept(style, String.valueOf(Character.toChars(codePoint)));
            return true;
        }

        public Component getComponent() {
            if (this.component == null) {
                MutableComponent component = Component.literal(this.builders.get(0).getKey().toString()).setStyle(this.builders.get(0).getValue());
                for (int i = 1; i < this.builders.size(); i++) {
                    Map.Entry<StringBuilder, Style> entry = this.builders.get(i);
                    component.append(Component.literal(entry.getKey().toString()).setStyle(entry.getValue()));
                }
                this.component = component;
            }
            return this.component;
        }
    }
}
