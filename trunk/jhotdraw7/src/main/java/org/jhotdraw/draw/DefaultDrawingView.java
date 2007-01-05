/*
 * @(#)DefaultDrawingView.java  3.2  2006-12-26
 *
 * Copyright (c) 1996-2006 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 */

package org.jhotdraw.draw;

import org.jhotdraw.gui.datatransfer.*;
import org.jhotdraw.util.*;
import org.jhotdraw.undo.*;
import org.jhotdraw.io.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.geom.*;
import java.awt.color.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.jhotdraw.app.EditableComponent;
import org.jhotdraw.geom.*;
import org.jhotdraw.xml.*;
import org.jhotdraw.xml.XMLTransferable;
/**
 * The DefaultDrawingView is suited for viewing drawings with a small number
 * of Figures.
 *
 * @author Werner Randelshofer
 * @version 3.2 2006-12-26 Rewrote storage and clipboard support.
 * <br>3.1 2006-12-17 Added printing support.
 * <br>3.0.2 2006-07-03 Constrainer must be a bound property.
 * <br>3.0.1 2006-06-11 Draw handles when this DrawingView is the focused
 * drawing view of the DrawingEditor.
 * <br>3.0 2006-02-17 Reworked to support multiple drawing views in a
 * DrawingEditor.
 * <br>2.0 2006-01-14 Changed to support double precision coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public class DefaultDrawingView
        extends JComponent
        implements DrawingView, DrawingListener, HandleListener, EditableComponent {
    private Drawing drawing;
    private Set<Figure> dirtyFigures = new HashSet<Figure>();
    private Set<Figure> selectedFigures = new HashSet<Figure>();
    private int rainbow = 0;
    private LinkedList<Handle> selectionHandles = new LinkedList<Handle>();
    
    private Handle secondaryHandleOwner;
    private LinkedList<Handle> secondaryHandles = new LinkedList<Handle>();
    private boolean handlesAreValid = true;
    private Dimension preferredSize;
    private double scaleFactor = 1;
    private Point2D.Double translate = new Point2D.Double(0,0);
    private int detailLevel;
    private DrawingEditor editor;
    private Constrainer constrainer = new GridConstrainer(1,1);
    private JLabel emptyDrawingLabel;
    
    /** Creates new instance. */
    public DefaultDrawingView() {
        initComponents();
        setToolTipText("dummy"); // Set a dummy tool tip text to turn tooltips on
        setFocusable(true);
        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                repaint();
            }
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();

        setLayout(null);

        setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-END:initComponents
    
    public Drawing getDrawing() {
        return drawing;
    }
    
    public String getToolTipText(MouseEvent evt) {
        Handle handle = findHandle(evt.getPoint());
        if (handle != null) {
            return handle.getToolTipText(evt.getPoint());
        }
        Figure figure = findFigure(evt.getPoint());
        if (figure != null) {
            return figure.getToolTipText(viewToDrawing(evt.getPoint()));
        }
        return null;
    }
    
    public void setEmptyDrawingMessage(String newValue) {
        String oldValue = (emptyDrawingLabel == null) ? null : emptyDrawingLabel.getText();
        if (newValue == null) {
            emptyDrawingLabel = null;
        } else {
            emptyDrawingLabel = new JLabel(newValue);
            emptyDrawingLabel.setHorizontalAlignment(JLabel.CENTER);
        }
        firePropertyChange("emptyDrawingMessage", oldValue, newValue);
        repaint();
    }
    public String getEmptyDrawingMessage() {
        return (emptyDrawingLabel == null) ? null : emptyDrawingLabel.getText();
    }
    
    /**
     * Paints the drawing view.
     * Uses rendering hints for fast painting. Paints the background, the
     * grid, the drawing, the handles and the current tool.
     */
    public void paintComponent(Graphics gr) {
        
        Graphics2D g = (Graphics2D) gr;
        
        // Set rendering hints for speed
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, (Options.isFractionalMetrics()) ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, (Options.isTextAntialiased()) ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        
        drawBackground(g);
        drawGrid(g);
        drawDrawing(g);
        
        drawHandles(g);
        drawTool(g);
    }
    /**
     * Prints the drawing view.
     * Uses high quality rendering hints for printing. Only prints the drawing.
     * Doesn't print the background, the grid, the handles and the tool.
     */
    public void printComponent(Graphics gr) {
        
        Graphics2D g = (Graphics2D) gr;
        
        // Set rendering hints for quality
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, (Options.isFractionalMetrics()) ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, (Options.isTextAntialiased()) ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        
        //drawBackground(g);
        //drawGrid(g);
        drawDrawing(g);
        
        //drawHandles(g);
        //drawTool(g);
    }
    
    protected void drawBackground(Graphics2D g) {
       /*
        rainbow = (rainbow + 10) % 360;
        g.setColor(
        new Color(Color.HSBtoRGB((float) (rainbow / 360f), 0.3f, 1.0f))
        );
        */
        // Position of the zero coordinate point on the view
        int x = (int) (-translate.x * scaleFactor);
        int y = (int) (-translate.y * scaleFactor);
        
        int w = getWidth();
        int h = getHeight();
        
        g.setColor(getBackground());
        g.fillRect(x, y, w - x, h - y);
        
        // Draw a gray background for the area which is at
        // negative view coordinates.
        if (y > 0) {
            g.setColor(new Color(0xf0f0f0));
            g.fillRect(0, 0, w, y);
        }
        if (x > 0) {
            g.setColor(new Color(0xf0f0f0));
            g.fillRect(0, 0, x, h);
        }
    }
    
    protected void drawGrid(Graphics2D g) {
        constrainer.draw(g, this);
    }
    
    protected void drawDrawing(Graphics2D gr) {
        if (drawing != null) {
            if (drawing.getFigureCount() == 0 && emptyDrawingLabel != null) {
                emptyDrawingLabel.setBounds(0, 0, getWidth(), getHeight());
                emptyDrawingLabel.paint(gr);
            } else {
                Graphics2D g = (Graphics2D) gr.create();
                AffineTransform tx = g.getTransform();
                tx.translate(-translate.x * scaleFactor, -translate.y * scaleFactor);
                tx.scale(scaleFactor, scaleFactor);
                g.setTransform(tx);
                
                drawing.setFontRenderContext(g.getFontRenderContext());
                drawing.draw(g);
                
                g.dispose();
            }
        }
    }
    
    protected void drawHandles(java.awt.Graphics2D g) {
        if (editor != null && editor.getFocusedView() == this) {
            validateHandles();
            for (Handle h : getSelectionHandles()) {
                h.draw(g);
            }
            for (Handle h : getSecondaryHandles()) {
                h.draw(g);
            }
        }
    }
    
    protected void drawTool(Graphics2D g) {
        if (editor != null && editor.getView() == this && editor.getTool() != null) {
            editor.getTool().draw(g);
        }
    }
    
    public void setDrawing(Drawing d) {
        if (this.drawing != null) {
            this.drawing.removeDrawingListener(this);
            clearSelection();
        }
        this.drawing = d;
        if (this.drawing != null) {
            this.drawing.addDrawingListener(this);
        }
        invalidateDimension();
        invalidate();
        if (getParent() != null) getParent().validate();
        repaint();
    }
    
    protected void repaint(Rectangle2D.Double r) {
        Rectangle vr = drawingToView(r);
        vr.grow(1, 1);
        repaint(vr);
    }
    
    public void areaInvalidated(DrawingEvent evt) {
        repaint(evt.getInvalidatedArea());
        invalidateDimension();
    }
    public void areaInvalidated(HandleEvent evt) {
        repaint(evt.getInvalidatedArea());
        invalidateDimension();
    }
    public void figureAdded(DrawingEvent evt) {
        // Repaint the whole drawing to remove the message label
        if (evt.getDrawing().getFigureCount() == 1) {
            repaint();
        } else {
            repaint(evt.getInvalidatedArea());
        }
        invalidateDimension();
    }
    public void figureRemoved(DrawingEvent evt) {
        // Repaint the whole drawing to draw the message label
        if (evt.getDrawing().getFigureCount() == 0) {
            repaint();
        } else {
            repaint(evt.getInvalidatedArea());
        }
        removeFromSelection(evt.getFigure());
        invalidateDimension();
    }
    public void invalidate() {
        invalidateDimension();
        super.invalidate();
    }
    
    /**
     * Adds a figure to the current selection.
     */
    public void addToSelection(Figure figure) {
        selectedFigures.add(figure);
        invalidateHandles();
        fireSelectionChanged();
        repaint();
    }
    /**
     * Adds a collection of figures to the current selection.
     */
    public void addToSelection(Collection<Figure> figures) {
        selectedFigures.addAll(figures);
        invalidateHandles();
        fireSelectionChanged();
        repaint();
    }
    
    /**
     * Removes a figure from the selection.
     */
    public void removeFromSelection(Figure figure) {
        if (selectedFigures.remove(figure)) {
            invalidateHandles();
            fireSelectionChanged();
        }
        repaint();
    }
    
    /**
     * If a figure isn't selected it is added to the selection.
     * Otherwise it is removed from the selection.
     */
    public void toggleSelection(Figure figure) {
        if (selectedFigures.contains(figure)) {
            selectedFigures.remove(figure);
        } else {
            selectedFigures.add(figure);
        }
        fireSelectionChanged();
        invalidateHandles();
        repaint();
    }
    
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setCursor(Cursor.getPredefinedCursor(b ? Cursor.DEFAULT_CURSOR : Cursor.WAIT_CURSOR));
    }
    
    
    /**
     * Selects all figures.
     */
    public void selectAll() {
        selectedFigures.clear();
        selectedFigures.addAll(drawing.getFigures());
        invalidateHandles();
        fireSelectionChanged();
        repaint();
    }
    /**
     * Clears the current selection.
     */
    public void clearSelection() {
        if (getSelectionCount()  > 0) {
            selectedFigures.clear();
            invalidateHandles();
            fireSelectionChanged();
        }
        repaint();
    }
    /**
     * Test whether a given figure is selected.
     */
    public boolean isFigureSelected(Figure checkFigure) {
        return selectedFigures.contains(checkFigure);
    }
    
    /**
     * Gets the current selection as a FigureSelection. A FigureSelection
     * can be cut, copied, pasted.
     */
    public Collection<Figure> getSelectedFigures() {
        return Collections.unmodifiableSet(selectedFigures);
    }
    /**
     * Gets the number of selected figures.
     */
    public int getSelectionCount() {
        return selectedFigures.size();
    }
    
    /**
     * Gets the currently active selection handles.
     */
    private java.util.List<Handle> getSelectionHandles() {
        validateHandles();
        return Collections.unmodifiableList(selectionHandles);
    }
    /**
     * Gets the currently active secondary handles.
     */
    private java.util.List<Handle> getSecondaryHandles() {
        validateHandles();
        return Collections.unmodifiableList(secondaryHandles);
    }
    
    /**
     * Invalidates the handles.
     */
    private void invalidateHandles() {
        if (handlesAreValid) {
            handlesAreValid = false;
            
            Rectangle invalidatedArea = null;
            for (Handle handle : selectionHandles) {
                handle.removeHandleListener(this);
                if (invalidatedArea == null) {
                    invalidatedArea = handle.getDrawBounds();
                } else {
                    invalidatedArea.add(handle.getDrawBounds());
                }
                handle.dispose();
            }
            selectionHandles.clear();
            secondaryHandles.clear();
            
            switch (selectedFigures.size()) {
                case 0 :
                    if (invalidatedArea != null) {
                        repaint(invalidatedArea);
                    }
                    break;
                case 1 :
                    if (invalidatedArea != null) {
                        repaint(invalidatedArea);
                    }
                    //validateHandles();
                    break;
                default :
                    repaint();
                    break;
            }
        }
    }
    /**
     * Validates the handles.
     */
    private void validateHandles() {
        if (! handlesAreValid) {
            handlesAreValid = true;
            
            Rectangle invalidatedArea = null;
            int level = detailLevel;
            do {
                for (Figure figure : getSelectedFigures()) {
                    for (Handle handle : figure.createHandles(level)) {
                        handle.setView(this);
                        selectionHandles.add(handle);
                        handle.addHandleListener(this);
                        if (invalidatedArea == null) {
                            invalidatedArea = handle.getBounds();
                        } else {
                            invalidatedArea.add(handle.getBounds());
                        }
                    }
                }
            } while (level-- > 0 && selectionHandles.size() == 0);
            detailLevel = level + 1;
            
            if (invalidatedArea != null) {
                repaint(invalidatedArea);
            }
        }
        
    }
    /**
     * Finds a handle at a given coordinates.
     * @return A handle, null if no handle is found.
     */
    public Handle findHandle(Point p) {
        validateHandles();
        
        for (Handle handle : new ReversedList<Handle>(getSecondaryHandles())) {
            if (handle.contains(p)) {
                return handle;
            }
        }
        for (Handle handle : new ReversedList<Handle>(getSelectionHandles())) {
            if (handle.contains(p)) {
                return handle;
            }
        }
        return null;
    }
    /**
     * Gets compatible handles.
     * @return A collection containing the handle and all compatible handles.
     */
    public Collection<Handle> getCompatibleHandles(Handle master) {
        validateHandles();
        
        HashSet<Figure> owners = new HashSet<Figure>();
        LinkedList<Handle> compatibleHandles = new LinkedList<Handle>();
        owners.add(master.getOwner());
        compatibleHandles.add(master);
        
        for (Handle handle : getSelectionHandles()) {
            if (! owners.contains(handle.getOwner())
            && handle.isCombinableWith(master)) {
                owners.add(handle.getOwner());
                compatibleHandles.add(handle);
            }
        }
        return compatibleHandles;
        
    }
    /**
     * Finds a figure at a given coordinates.
     * @return A figure, null if no figure is found.
     */
    public Figure findFigure(Point p) {
        return getDrawing().findFigure(viewToDrawing(p));
    }
    
    public Collection<Figure> findFigures(Rectangle r) {
        return getDrawing().findFigures(viewToDrawing(r));
    }
    
    public Collection<Figure> findFiguresWithin(Rectangle r) {
        return getDrawing().findFiguresWithin(viewToDrawing(r));
    }
    
    
    
    
    public void addFigureSelectionListener(FigureSelectionListener fsl) {
        listenerList.add(FigureSelectionListener.class, fsl);
    }
    
    public void removeFigureSelectionListener(FigureSelectionListener fsl) {
        listenerList.remove(FigureSelectionListener.class, fsl);
    }
    /**
     *  Notify all listenerList that have registered interest for
     * notification on this event type.
     */
    protected void fireSelectionChanged() {
        if (listenerList.getListenerCount() > 0) {
            FigureSelectionEvent event = null;
            // Notify all listeners that have registered interest for
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length-2; i>=0; i-=2) {
                if (listeners[i] == FigureSelectionListener.class) {
                    // Lazily create the event:
                    if (event == null)
                        event = new FigureSelectionEvent(this);
                    ((FigureSelectionListener)listeners[i+1]).selectionChanged(event);
                }
            }
        }
    }
    
    public void handleRequestRemove(HandleEvent e) {
        selectionHandles.remove(e.getHandle());
        e.getHandle().dispose();
        invalidateHandles();
        //validateHandles();
        repaint(e.getInvalidatedArea());
    }
    
    protected void invalidateDimension() {
        preferredSize = null;
    }
    
    public Constrainer getConstrainer() {
        return constrainer;
    }
    
    public void setConstrainer(Constrainer newValue) {
        Constrainer oldValue = constrainer;
        constrainer = newValue;
        repaint();
        firePropertyChange("constrainer", oldValue, newValue);
    }
    
    
    /**
     * Side effect: Changes view Translation!!!
     */
    public Dimension getPreferredSize() {
        if (preferredSize == null) {
            Dimension2DDouble d = new Dimension2DDouble();
            if (drawing != null) {
                translate.x = 0;
                translate.y = 0;
                for (Figure f : drawing.getFigures()) {
                    Rectangle2D.Double r = f.getDrawingArea();
                    d.width = Math.max(d.width, r.x + r.width);
                    d.height = Math.max(d.height, r.y + r.height);
                    translate.x = Math.min(translate.x, r.x);
                    translate.y = Math.min(translate.y, r.y);
                }
            }
            preferredSize = new Dimension(
                    (int) ((d.width + 10 - translate.x) * scaleFactor),
                    (int) ((d.height + 10 - translate.y) * scaleFactor)
                    );
            fireViewTransformChanged();
            repaint();
        }
        return preferredSize;
    }
    /**
     * Converts drawing coordinates to view coordinates.
     */
    public Point drawingToView(Point2D.Double p) {
        return new Point(
                (int) ((p.x - translate.x) * scaleFactor),
                (int) ((p.y - translate.y) * scaleFactor)
                );
    }
    /**
     * Converts view coordinates to drawing coordinates.
     */
    public Point2D.Double viewToDrawing(Point p) {
        return new Point2D.Double(
                p.x / scaleFactor + translate.x,
                p.y / scaleFactor + translate.y
                );
    }
    public Rectangle drawingToView(Rectangle2D.Double r) {
        return new Rectangle(
                (int) ((r.x - translate.x) * scaleFactor),
                (int) ((r.y - translate.y) * scaleFactor),
                (int) (r.width * scaleFactor),
                (int) (r.height * scaleFactor)
                );
    }
    public Rectangle2D.Double viewToDrawing(Rectangle r) {
        return new Rectangle2D.Double(
                r.x / scaleFactor + translate.x,
                r.y / scaleFactor + translate.y,
                r.width / scaleFactor,
                r.height / scaleFactor
                );
    }
    
    public JComponent getComponent() {
        return this;
    }
    
    public double getScaleFactor() {
        return scaleFactor;
    }
    
    public void setScaleFactor(double newValue) {
        double oldValue = scaleFactor;
        scaleFactor = newValue;
        
        fireViewTransformChanged();
        
        firePropertyChange("scaleFactor", oldValue, newValue);
        
        invalidateDimension();
        invalidate();
        if (getParent() != null) getParent().validate();
        repaint();
    }
    
    protected void fireViewTransformChanged() {
        for (Handle handle : selectionHandles) {
            handle.viewTransformChanged();
        }
        for (Handle handle : secondaryHandles) {
            handle.viewTransformChanged();
        }
    }
    
    public void setHandleDetailLevel(int newValue) {
        detailLevel = newValue;
        invalidateHandles();
        repaint();
    }
    public int getHandleDetailLevel() {
        return detailLevel;
    }
    
    public void handleRequestSecondaryHandles(HandleEvent e) {
        //if (e.getHandle() != secondaryHandleOwner) {
        secondaryHandleOwner = e.getHandle();
        secondaryHandles.clear();
        secondaryHandles.addAll(secondaryHandleOwner.createSecondaryHandles());
        for (Handle h : secondaryHandles) {
            h.setView(this);
            h.addHandleListener(this);
        }
        repaint();
        //}
    }
    
    public AffineTransform getDrawingToViewTransform() {
        AffineTransform t = new AffineTransform();
        t.scale(scaleFactor, scaleFactor);
        t.translate(- translate.x, - translate.y);
        return t;
    }
    
    public void copy() {
        if (drawing.getOutputFormats() == null ||
                drawing.getOutputFormats().size() == 0) {
            getToolkit().beep();
            return;
        }
        
        java.util.List<Figure> toBeCopied = drawing.sort(getSelectedFigures());
        if (toBeCopied.size() > 0) {
            try {
                CompositeTransferable transfer = new CompositeTransferable();
                for (OutputFormat format : drawing.getOutputFormats()) {
                    Transferable t = format.createTransferable(toBeCopied, scaleFactor);
                    if (! transfer.isDataFlavorSupported(t.getTransferDataFlavors()[0])) {
                    transfer.add(t);
                    }
                }
                getToolkit().getSystemClipboard().setContents(transfer, transfer);
            } catch (IOException e) {
                e.printStackTrace();
                getToolkit().beep();
            }
        }
    }
    
    public void cut() {
        if (drawing.getOutputFormats() == null ||
                drawing.getOutputFormats().size() == 0) {
            getToolkit().beep();
            return;
        }
        copy();
        delete();
    }
    
    public void delete() {
        ArrayList<Figure> toBeDeleted = new ArrayList<Figure>(getSelectedFigures());
        clearSelection();
        getDrawing().removeAll(toBeDeleted);
    }
    
    public void paste() {
        if (drawing.getInputFormats() == null ||
                drawing.getInputFormats().size() == 0) {
            getToolkit().beep();
            return;
        }
        
        try {
            Transferable transfer = getToolkit().getSystemClipboard().getContents(this);
            // Search for a suitable input format
            for (InputFormat format : drawing.getInputFormats()) {
                for (DataFlavor flavor : transfer.getTransferDataFlavors()) {
                if (format.isDataFlavorSupported(flavor)) {
                    CompositeEdit ce = new CompositeEdit("Paste");
                    getDrawing().fireUndoableEditHappened(ce);
                    java.util.List<Figure> toBeSelected = format.readFigures(transfer);
                    clearSelection();
                    getDrawing().addAll(toBeSelected); 
                    addToSelection(toBeSelected);
                    getDrawing().fireUndoableEditHappened(ce);
                    break;
                }
                }
            }
            
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public void duplicate() {
        Collection<Figure> sorted = getDrawing().sort(getSelectedFigures());
        HashMap<Figure,Figure> originalToDuplicateMap = new HashMap<Figure,Figure>(sorted.size());
        
        clearSelection();
        Drawing drawing = getDrawing();
        ArrayList<Figure> duplicates = new ArrayList<Figure>(sorted.size());
        AffineTransform tx = new AffineTransform();
        tx.translate(5,5);
        for (Figure f : sorted) {
            Figure d = (Figure) f.clone();
            d.basicTransform(tx);
            duplicates.add(d);
            originalToDuplicateMap.put(f, d);
            drawing.add(d);
        }
        for (Figure f : duplicates) {
            f.remap(originalToDuplicateMap);
        }
        addToSelection(duplicates);
    }
    
    public void removeNotify(DrawingEditor editor) {
        this.editor = null;
        repaint();
    }
    
    public void addNotify(DrawingEditor editor) {
        this.editor = editor;
        repaint();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    // End of variables declaration//GEN-END:variables
    
}
