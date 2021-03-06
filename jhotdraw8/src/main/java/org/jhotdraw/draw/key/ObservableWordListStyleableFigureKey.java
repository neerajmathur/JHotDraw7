/* @(#)ObservableWordListStyleableFigureKey.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw.key;

import java.util.List;
import java.util.function.Function;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.styleable.StyleablePropertyBean;
import org.jhotdraw.text.Converter;
import org.jhotdraw.text.CssObservableWordListConverter;
import org.jhotdraw.text.StyleConverterConverterWrapper;
import org.jhotdraw.styleable.StyleableMapAccessor;

/**
 * ObservableWordListStyleableFigureKey.
 *
 * @author Werner Randelshofer
 */
public class ObservableWordListStyleableFigureKey extends SimpleFigureKey<ObservableList<String>> implements StyleableMapAccessor<ObservableList<String>> {

    private final static long serialVersionUID = 1L;

    private final CssMetaData<?, ObservableList<String>> cssMetaData;

    /**
     * Creates a new instance with the specified name and with null as the
     * default value.
     *
     * @param name The name of the key.
     */
    public ObservableWordListStyleableFigureKey(String name) {
        this(name, null);
    }

    /**
     * Creates a new instance with the specified name and default value.
     *
     * @param name The name of the key.
     * @param defaultValue The default value.
     */
    public ObservableWordListStyleableFigureKey(String name, ObservableList<String> defaultValue) {
        this(name, DirtyMask.of(DirtyBits.NODE), defaultValue);
    }

    /**
     * Creates a new instance with the specified name, mask and default value.
     *
     * @param name The name of the key.
     * @param mask The dirty mask.
     * @param defaultValue The default value.
     */
    public ObservableWordListStyleableFigureKey(String name, DirtyMask mask, ObservableList<String> defaultValue) {
        super(name, List.class, new Class<?>[]{Double.class}, mask, defaultValue);
        /*
         StyleablePropertyFactory factory = new StyleablePropertyFactory(null);

         Function<Styleable, StyleableProperty<ObservableList<String>>> function = s -> {
         StyleablePropertyBean spb = (StyleablePropertyBean) s;
         return spb.getStyleableProperty(this);
         };
         boolean inherits = false;
         String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
         final StyleConverter<ParsedValue[], ObservableList<String>> converter
         = DoubleListStyleConverter.getInstance();
         CssMetaData<Styleable, ObservableList<String>> md
         = new SimpleCssMetaData<Styleable, ObservableList<String>>(property, function,
         converter, defaultValue, inherits);
         cssMetaData = md;*/
        Function<Styleable, StyleableProperty<ObservableList<String>>> function = s -> {
            StyleablePropertyBean spb = (StyleablePropertyBean) s;
            return spb.getStyleableProperty(this);
        };
        boolean inherits = false;
        String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
        final StyleConverter<String, ObservableList<String>> converter
                = new StyleConverterConverterWrapper<ObservableList<String>>(new CssObservableWordListConverter());
        CssMetaData<Styleable, ObservableList<String>> md
                = new SimpleCssMetaData<Styleable, ObservableList<String>>(property, function,
                        converter, defaultValue, inherits);
        cssMetaData = md;
    }

    @Override
    public CssMetaData<?, ObservableList<String>> getCssMetaData() {
        return cssMetaData;

    }

    private Converter<ObservableList<String>> converter;

    @Override
    public Converter<ObservableList<String>> getConverter() {
        if (converter == null) {
            converter = new CssObservableWordListConverter();
        }
        return converter;
    }    
}
