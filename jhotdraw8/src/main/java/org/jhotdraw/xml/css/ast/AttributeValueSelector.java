/*
 * @(#)AttributeValueSelector.java
 * Copyright (c) 2014 Supercomputing Systems AG, Schweiz.
 * Alle Rechte vorbehalten. 
 */
package org.jhotdraw.xml.css.ast;

import org.jhotdraw.xml.css.SelectorModel;

/**
 * An "attribute value selector" matches an element if the element has an
 * attribute with the specified name and value.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class AttributeValueSelector extends AttributeSelector {

    private final String attributeName;
    private final String attributeValue;

    public AttributeValueSelector(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    @Override
    protected <T> T match(SelectorModel<T> model, T element) {
        String value = model.getAttribute(element, attributeName);
        return attributeValue.equals(value) ? element : null;
    }
}
