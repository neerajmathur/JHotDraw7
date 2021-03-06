/* @(#)NoLayoutNoConnectionsDrawingModel.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw.model;

import java.util.Objects;
import javafx.scene.transform.Transform;
import org.jhotdraw.collection.MapAccessor;
import org.jhotdraw.draw.key.DirtyBits;
import org.jhotdraw.draw.key.DirtyMask;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.key.FigureMapAccessor;

/**
 * This drawing model assumes that the drawing contains no figures which perform
 layouts but has getDependentFigures between figures.
 * <p>
 Further assumes that a connection figure has no further getDependentFigures.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class ConnectionsNoLayoutDrawingModel extends AbstractDrawingModel {

    @Override
    public void setRoot(Drawing root) {
        this.root = root;
        fire(DrawingModelEvent.rootChanged(this, root));
    }

    @Override
    public void removeFromParent(Figure child) {
        Drawing oldDrawing = child.getDrawing();
        Figure parent = child.getParent();
        if (parent != null) {
            int index = parent.getChildren().indexOf(child);
            if (index != -1) {
                parent.getChildren().remove(index);
                fire(DrawingModelEvent.figureRemovedFromParent(this, parent, child, index));
                fire(DrawingModelEvent.nodeInvalidated(this, parent));
            }
        }
        Drawing newDrawing = child.getDrawing();
        if (oldDrawing != newDrawing) {
            if (oldDrawing != null) {
                fire(DrawingModelEvent.figureRemovedFromDrawing(this, oldDrawing, child));
            }
            if (newDrawing != null) {
                fire(DrawingModelEvent.figureAddedToDrawing(this, newDrawing, child));
            }
        }
    }

    @Override
    public void insertChildAt(Figure child, Figure parent, int index) {
        Drawing oldDrawing = child.getDrawing();
        if (child.getParent() != null) {
            child.getParent().remove(child);
        }
        parent.getChildren().add(index, child);
        fire(DrawingModelEvent.figureAddedToParent(this, parent, child, index));
        fire(DrawingModelEvent.nodeInvalidated(this, parent));
        Drawing newDrawing = child.getDrawing();
        if (oldDrawing != newDrawing) {
            if (oldDrawing != null) {
                fire(DrawingModelEvent.figureRemovedFromDrawing(this, oldDrawing, child));
            }
            if (newDrawing != null) {
                fire(DrawingModelEvent.figureAddedToDrawing(this, newDrawing, child));
            }
        }
    }

    @Override
    public void disconnect(Figure figure) {
        for (Figure connectedFigure : figure.getDependentFigures()) {
            fire(DrawingModelEvent.nodeInvalidated(this, connectedFigure));

        }
        figure.disconnectDependantsAndProviders();
        fire(DrawingModelEvent.nodeInvalidated(this, figure));
    }

    @Override
    public <T> T set(Figure figure, MapAccessor<T> key, T newValue) {
        T oldValue = figure.set(key, newValue);
        if (!Objects.equals(oldValue, newValue)) {
            final DirtyMask dm;
            if (key instanceof FigureMapAccessor) {
                FigureMapAccessor<T> fk = (FigureMapAccessor<T>) key;
                dm = fk.getDirtyMask();
            } else {
                dm = DirtyMask.EMPTY;
            }

            if (dm.containsOneOf(DirtyBits.NODE)) {
                fire(DrawingModelEvent.nodeInvalidated(this, figure));
            }
            if (dm.containsOneOf(DirtyBits.LAYOUT)) {
                fire(DrawingModelEvent.layoutInvalidated(this, figure));
            }
            if (dm.containsOneOf(DirtyBits.CONNECTION_LAYOUT)) {
                for (Figure c : figure.getDependentFigures()) {
                    fire(DrawingModelEvent.layoutInvalidated(this, c));
                }
            }
        }

        return oldValue;
    }

    @Override
    public void reshape(Figure figure, Transform transform) {
        figure.reshape(transform);
        fire(DrawingModelEvent.subtreeNodesInvalidated(this, figure));
        for (Figure f : figure.preorderIterable()) {
            for (Figure c : f.getDependentFigures()) {
                fire(DrawingModelEvent.layoutInvalidated(this, c));
            }
        }
    }

    @Override
    public void reshape(Figure figure, double x, double y, double width, double height) {
        figure.reshape(x, y, width, height);
        fire(DrawingModelEvent.subtreeNodesInvalidated(this, figure));
        for (Figure f : figure.preorderIterable()) {
            for (Figure c : f.getDependentFigures()) {
                fire(DrawingModelEvent.layoutInvalidated(this, c));
            }
        }
    }

    @Override
    public void layout(Figure figure) {
        figure.updateLayout();
        fire(DrawingModelEvent.subtreeNodesInvalidated(this, figure));
        for (Figure f : figure.preorderIterable()) {
            for (Figure c : f.getDependentFigures()) {
                fire(DrawingModelEvent.layoutInvalidated(this, c));
            }
        }
    }

    @Override
    public void applyCss(Figure figure) {
        figure.updateCss();
        fire(DrawingModelEvent.subtreeNodesInvalidated(this, figure));
        for (Figure f : figure.preorderIterable()) {
            for (Figure c : f.getDependentFigures()) {
                fire(DrawingModelEvent.layoutInvalidated(this, c));
            }
        }
    }
}
