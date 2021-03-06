/* @(#)FileURIChooser.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui;

import java.io.File;
import java.net.URI;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * FileURIChooser.
 * @author Werner Randelshofer
 */
public class FileURIChooser implements URIChooser {

    /** The associated file chooser object. */
    private final FileChooser chooser = new FileChooser();

    public enum Mode {

        OPEN, SAVE
    };

    private Mode mode;

    public FileURIChooser() {
        this(Mode.OPEN);
    }

    public FileURIChooser(Mode newValue) {
        mode = newValue;
    }

    public void setMode(Mode newValue) {
        mode = newValue;
    }

    public Mode getMode() {
        return mode;
    }

    public FileChooser getFileChooser() {
        return chooser;
    }

    @Override
    public URI showDialog(Window parent) {
        File f = null;
        switch (mode) {
            case OPEN:
                f = chooser.showOpenDialog(parent);
                break;
            case SAVE:
                f = chooser.showSaveDialog(parent);
                break;
        }
        return f == null ? null : f.toURI();
    }
}
