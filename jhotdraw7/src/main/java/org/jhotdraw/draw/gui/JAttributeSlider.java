/*
 * @(#)JAttributeSlider.java
 *
 * Copyright (c) 2007 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the 
 * accompanying license terms.
 */
package org.jhotdraw.draw.gui;

import javax.swing.*;
import org.jhotdraw.gui.AttributeEditor;

/**
 * A JSlider that can be used to edit a double attribute of a Figure.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class JAttributeSlider extends JSlider implements AttributeEditor<Double> {
    private final static long serialVersionUID = 1L;
    private boolean isMultipleValues;
    private Double attributeValue;
    private double scaleFactor = 1d;

    /** Creates new instance. */
    public JAttributeSlider() {
        this(JSlider.VERTICAL, 0, 100, 50);
    }

    public JAttributeSlider(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void setAttributeValue(Double newValue) {
        attributeValue = newValue;
        setValue(attributeValue == null ? 0 : (int) (attributeValue * scaleFactor));
    }

    @Override
    public Double getAttributeValue() {
        return attributeValue;
    }

    public void setScaleFactor(double newValue) {
        scaleFactor = newValue;
    }
    public double getScaleFactor() {
        return scaleFactor;
    }

    @Override
    public void setMultipleValues(boolean newValue) {
        boolean oldValue = isMultipleValues;
        isMultipleValues = newValue;
        firePropertyChange(MULTIPLE_VALUES_PROPERTY, oldValue, newValue);
    }

    @Override
    public boolean isMultipleValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void fireStateChanged() {
        super.fireStateChanged();
        Double oldValue = attributeValue;
        attributeValue =  getValue() / scaleFactor;
        firePropertyChange(ATTRIBUTE_VALUE_PROPERTY, oldValue, attributeValue);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
