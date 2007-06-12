/*
 * @(#)ApplicationModel.java  1.0  June 10, 2006
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

package org.jhotdraw.application;

import java.util.*;
import javax.swing.*;
/**
 * ApplicationModel.
 *
 * @author Werner Randelshofer.
 * @version 1.0 June 10, 2006 Created.
 */
public interface ApplicationModel {
    /**
     * Returns the name of the application.
     */
    public String getName();
    /**
     * Returns the version of the application.
     */
    public String getVersion();
    /**
     * Returns the copyright of the application.
     */
    public String getCopyright();
    
    /**
     * Creates a new documentView for the application.
     */
    public DocumentView createProject();
    
    public void initProject(DocumentOrientedApplication a, DocumentView p);
    
    public void initApplication(DocumentOrientedApplication a);
    /**
     * Puts an action with the specified id.
     */
    public void putAction(String id, Action action);
    /**
     * Returns the action with the specified id.
     */
    public Action getAction(String id);
    /**
     * Creates tool bars.
     * <p>
     * Depending on the document interface of the application, this method
     * may be invoked only once for the application, or for each opened documentView.
     * <p>
     * 
     * @param a DocumentOrientedApplication.
     * @param p The documentView for which the toolbars need to be created, or null
     * if the toolbar needs to be shared with multiple projects.
     */
    public List<JToolBar> createToolBars(DocumentOrientedApplication a, DocumentView p);
    
    /**
     * Creates menus.
     * <p>
     * Depending on the document interface of the application, this method
     * may be invoked only once for the application, or for each opened documentView.
     * <p>
     * 
     * @param a DocumentOrientedApplication.
     * @param p The documentView for which the toolbars need to be created, or null
     * if the toolbar needs to be shared with multiple projects.
     */
    public List<JMenu> createMenus(DocumentOrientedApplication a, DocumentView p);
}
