/* @(#)SimpleDrawing.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw.draw;

import java.util.ArrayList;
import java.util.List;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.StyleableFigure;
import org.jhotdraw.draw.figure.LockableFigure;
import org.jhotdraw.draw.figure.AbstractCompositeFigure;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.jhotdraw.styleable.StyleableStyleManager;
import org.jhotdraw.css.StyleManager;
import org.jhotdraw.draw.css.FigureStyleManager;
import org.jhotdraw.draw.figure.NonTransformableFigure;
import org.jhotdraw.text.CColor;

/**
 * SimpleDrawing.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SimpleDrawing extends AbstractCompositeFigure 
        implements Drawing, StyleableFigure, LockableFigure, NonTransformableFigure {

    /**
     * The style manager is created lazily. 
     */
    private StyleManager<Figure> styleManager = null;

    public SimpleDrawing() {
    }

    @Override
    public Node createNode(RenderContext drawingView) {
        Group g = new Group();
        Rectangle background = new Rectangle();
        background.setId("background");
        g.getProperties().put("background", background);
        return g;
    }

    @Override
    public void updateNode(RenderContext v, Node n) {
        Group g = (Group) n;
        //applyTransformableFigureProperties(n);
        applyStyleableFigureProperties(v, n);
        
        Bounds bounds = getBoundsInLocal();
        Rectangle page = (Rectangle) g.getProperties().get("background");
        page.setX(bounds.getMinX());
        page.setY(bounds.getMinY());
        page.setWidth(bounds.getWidth());
        page.setHeight(bounds.getHeight());
        CColor cclr = getStyled(BACKGROUND);
        page.setFill(cclr==null?null:cclr.getColor());

        List<Node> nodes = new ArrayList<Node>(getChildren().size());
        nodes.add(page);
        for (Figure child : getChildren()) {
            nodes.add(v.getNode(child));
        }
        ObservableList<Node> group = ((Group) n).getChildren();
        if (! group.equals(nodes)) {
            group.setAll(nodes);
        }
    }

    @Override
    public Bounds getBoundsInLocal() {
        return new BoundingBox(0.0, 0.0, get(WIDTH), get(HEIGHT));

    }

    @Override
    public void reshape(Transform transform) {
        for (Figure child : getChildren()) {
            child.reshape(transform);
        }
    }

    @Override
    public boolean isLayoutable() {
        return false;
    }

    @Override
    public StyleManager<Figure> getStyleManager() {
        if (styleManager == null) {
            styleManager = createStyleManager();
            styleManager.setStylesheets(StyleOrigin.USER_AGENT, get(DOCUMENT_HOME), get(USER_AGENT_STYLESHEETS));
            styleManager.setStylesheets(StyleOrigin.AUTHOR, get(DOCUMENT_HOME), get(AUTHOR_STYLESHEETS));
            styleManager.setStylesheets(StyleOrigin.INLINE, get(INLINE_STYLESHEETS));
        }
        return styleManager;
    }
    protected StyleManager<Figure> createStyleManager() {
        if (true) return new FigureStyleManager();
        
        
        StyleManager<?> ret= (StyleManager<Styleable>)new StyleableStyleManager();
        
        // We can safely cast StylebleStyleManager to StyleManager<Figure>
        // because the Figure interface extends the Styleable interface.
        @SuppressWarnings("unchecked")
        StyleManager<Figure> rf=(StyleManager<Figure>)ret;
        return rf;
    }

    @Override
    public void stylesheetNotify() {
        if (styleManager != null) {
            styleManager.setStylesheets(StyleOrigin.USER_AGENT, get(DOCUMENT_HOME), get(USER_AGENT_STYLESHEETS));
            styleManager.setStylesheets(StyleOrigin.AUTHOR, get(DOCUMENT_HOME), get(AUTHOR_STYLESHEETS));
            styleManager.setStylesheets(StyleOrigin.INLINE, get(INLINE_STYLESHEETS));
        }
        super.stylesheetNotify();
    }
}
