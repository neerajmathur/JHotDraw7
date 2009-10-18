/**
 * @(#)WheelsAndSlidersMain.java
 *
 * Copyright (c) 2008 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and
 * contributors of the JHotDraw project ("the copyright holders").
 * You may not use, copy or modify this software, except in
 * accordance with the license agreement you entered into with
 * the copyright holders. For details see accompanying license terms.
 */
package org.jhotdraw.samples.color;

import org.jhotdraw.color.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A demo of color wheels and color sliders using all kinds of color systems.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class WheelsAndSlidersMain extends javax.swing.JPanel {
    private Color color;
    private JLabel colorLabel;
    
    private LinkedList<ColorSliderModel> models;

    private class Handler implements ChangeListener {
        private int adjusting;
        public void stateChanged(ChangeEvent e) {
            if (adjusting++ == 0) {
            ColorSliderModel m = (ColorSliderModel) e.getSource();
            color = m.getColor();
            previewLabel.setBackground(color);
            for (ColorSliderModel c : models) {
                if (c != m) {
                    c.setColor(color);
                }
            }
            }
            adjusting--;
        }
        
    }
    private Handler handler;
    
    /** Creates new form. */
    public WheelsAndSlidersMain() {
        initComponents();

        models = new LinkedList<ColorSliderModel>();
        handler = new Handler();
        
        previewLabel.setOpaque(true);
        
        chooserPanel.add(createColorWheelChooser(new HSVRGBColorSystem()));
        chooserPanel.add(createColorWheelChooser(new HSLRGBColorSystem()));
        chooserPanel.add(createColorWheelChooser(new HSVRYBColorSystem()));
        chooserPanel.add(createColorWheelChooser(new HSLRYBColorSystem()));
        chooserPanel.add(createColorWheelChooser(new HSLRYBColorSystem(), 0, 2, 1));
        chooserPanel.add(createSliderChooser(new HSVRGBColorSystem()));
        chooserPanel.add(createSliderChooser(new RGBColorSystem()));
        chooserPanel.add(createSliderChooser(new CMYKICCColorSystem()));
        chooserPanel.add(createSliderChooser(new CMYKNominalColorSystem()));
    }

    private JPanel createColorWheelChooser(ColorSystem sys) {
        return createColorWheelChooser(sys, 0, 1, 2);
    }
    private JPanel createColorWheelChooser(ColorSystem sys, int angularIndex, int radialIndex, int verticalIndex) {
        JPanel p = new JPanel(new BorderLayout());
        DefaultColorSliderModel m = new DefaultColorSliderModel(sys);
        models.add(m);
        m.addChangeListener(handler);
        JColorWheel w = new JColorWheel();
        w.setAngularComponentIndex(angularIndex);
        w.setRadialComponentIndex(radialIndex);
        w.setVerticalComponentIndex(verticalIndex);
        w.setModel(m);
        JSlider s = new JSlider(JSlider.VERTICAL);
        m.configureSlider(verticalIndex, s);
        p.add(w, BorderLayout.CENTER);
        p.add(s, BorderLayout.EAST);
        return p;
    }
    private JPanel createSliderChooser(ColorSystem sys) {
        JPanel p = new JPanel(new GridLayout(0, 1));
        DefaultColorSliderModel m = new DefaultColorSliderModel(sys);
        models.add(m);
        m.addChangeListener(handler);
        for (int i=0; i < m.getComponentCount(); i++) {
        JSlider s = new JSlider(JSlider.HORIZONTAL);
        m.configureSlider(i, s);
        p.add(s);
        }
        return p;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame f = new JFrame("Wheel");
                f.add(new WheelsAndSlidersMain());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setVisible(true);
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooserPanel = new javax.swing.JPanel();
        previewLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        chooserPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chooserPanel.setLayout(new java.awt.GridLayout(2, 0, 10, 10));
        add(chooserPanel, java.awt.BorderLayout.CENTER);

        previewLabel.setText("Color");
        add(previewLabel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chooserPanel;
    private javax.swing.JLabel previewLabel;
    // End of variables declaration//GEN-END:variables
}
