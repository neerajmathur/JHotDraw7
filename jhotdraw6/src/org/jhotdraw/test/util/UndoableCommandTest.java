/*
 * @(#)Test.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	� by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.test.util;

// JUnitDoclet begin import
import org.jhotdraw.standard.DeleteCommand;
import org.jhotdraw.test.JHDTestCase;
import org.jhotdraw.util.UndoableCommand;
// JUnitDoclet end import

/*
 * Generated by JUnitDoclet, a tool provided by
 * ObjectFab GmbH under LGPL.
 * Please see www.junitdoclet.org, www.gnu.org
 * and www.objectfab.de for informations about
 * the tool, the licence and the authors.
 */

// JUnitDoclet begin javadoc_class
/**
 * TestCase UndoableCommandTest is generated by
 * JUnitDoclet to hold the tests for UndoableCommand.
 * @see org.jhotdraw.util.UndoableCommand
 */
// JUnitDoclet end javadoc_class
public class UndoableCommandTest
// JUnitDoclet begin extends_implements
extends JHDTestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private UndoableCommand undoablecommand;
	// JUnitDoclet end class

	/**
	 * Constructor UndoableCommandTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public UndoableCommandTest(String name) {
		// JUnitDoclet begin method UndoableCommandTest
		super(name);
		// JUnitDoclet end method UndoableCommandTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public UndoableCommand createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new UndoableCommand(new DeleteCommand("Delete", getDrawingEditor()));
		// JUnitDoclet end method testcase.createInstance
	}

	/**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
	protected void setUp() throws Exception {
		// JUnitDoclet begin method testcase.setUp
		super.setUp();
		undoablecommand = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		undoablecommand = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method execute()
	/**
	 * Method testExecute is testing execute
	 * @see org.jhotdraw.util.UndoableCommand#execute()
	 */
	// JUnitDoclet end javadoc_method execute()
	public void testExecute() throws Exception {
		// JUnitDoclet begin method execute
		// JUnitDoclet end method execute
	}

	// JUnitDoclet begin javadoc_method isExecutable()
	/**
	 * Method testIsExecutable is testing isExecutable
	 * @see org.jhotdraw.util.UndoableCommand#isExecutable()
	 */
	// JUnitDoclet end javadoc_method isExecutable()
	public void testIsExecutable() throws Exception {
		// JUnitDoclet begin method isExecutable
		// JUnitDoclet end method isExecutable
	}

	// JUnitDoclet begin javadoc_method name()
	/**
	 * Method testName is testing name
	 * @see org.jhotdraw.util.UndoableCommand#name()
	 */
	// JUnitDoclet end javadoc_method name()
	public void testName() throws Exception {
		// JUnitDoclet begin method name
		// JUnitDoclet end method name
	}

	// JUnitDoclet begin javadoc_method getDrawingEditor()
	/**
	 * Method testGetDrawingEditor is testing getDrawingEditor
	 * @see org.jhotdraw.util.UndoableCommand#getDrawingEditor()
	 */
	// JUnitDoclet end javadoc_method getDrawingEditor()
	public void testGetDrawingEditor() throws Exception {
		// JUnitDoclet begin method getDrawingEditor
		// JUnitDoclet end method getDrawingEditor
	}

	// JUnitDoclet begin javadoc_method view()
	/**
	 * Method testView is testing view
	 * @see org.jhotdraw.util.UndoableCommand#view()
	 */
	// JUnitDoclet end javadoc_method view()
	public void testView() throws Exception {
		// JUnitDoclet begin method view
		// JUnitDoclet end method view
	}

	// JUnitDoclet begin javadoc_method figureSelectionChanged()
	/**
	 * Method testFigureSelectionChanged is testing figureSelectionChanged
	 * @see org.jhotdraw.util.UndoableCommand#figureSelectionChanged(org.jhotdraw.framework.DrawingView)
	 */
	// JUnitDoclet end javadoc_method figureSelectionChanged()
	public void testFigureSelectionChanged() throws Exception {
		// JUnitDoclet begin method figureSelectionChanged
		// JUnitDoclet end method figureSelectionChanged
	}

	// JUnitDoclet begin javadoc_method setUndoActivity()
	/**
	 * Method testSetGetUndoActivity is testing setUndoActivity
	 * and getUndoActivity together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.util.UndoableCommand#setUndoActivity(org.jhotdraw.util.Undoable)
	 * @see org.jhotdraw.util.UndoableCommand#getUndoActivity()
	 */
	// JUnitDoclet end javadoc_method setUndoActivity()
	public void testSetGetUndoActivity() throws Exception {
		// JUnitDoclet begin method setUndoActivity getUndoActivity
		// Do nothing: UndoCommand.setUndoActivity() is a no-op
		// JUnitDoclet end method setUndoActivity getUndoActivity
	}

	// JUnitDoclet begin javadoc_method addCommandListener()
	/**
	 * Method testAddCommandListener is testing addCommandListener
	 * @see org.jhotdraw.util.UndoableCommand#addCommandListener(org.jhotdraw.util.CommandListener)
	 */
	// JUnitDoclet end javadoc_method addCommandListener()
	public void testAddCommandListener() throws Exception {
		// JUnitDoclet begin method addCommandListener
		// JUnitDoclet end method addCommandListener
	}

	// JUnitDoclet begin javadoc_method removeCommandListener()
	/**
	 * Method testRemoveCommandListener is testing removeCommandListener
	 * @see org.jhotdraw.util.UndoableCommand#removeCommandListener(org.jhotdraw.util.CommandListener)
	 */
	// JUnitDoclet end javadoc_method removeCommandListener()
	public void testRemoveCommandListener() throws Exception {
		// JUnitDoclet begin method removeCommandListener
		// JUnitDoclet end method removeCommandListener
	}

	// JUnitDoclet begin javadoc_method createEventDispatcher()
	/**
	 * Method testCreateEventDispatcher is testing createEventDispatcher
	 * @see org.jhotdraw.util.UndoableCommand#createEventDispatcher()
	 */
	// JUnitDoclet end javadoc_method createEventDispatcher()
	public void testCreateEventDispatcher() throws Exception {
		// JUnitDoclet begin method createEventDispatcher
		// JUnitDoclet end method createEventDispatcher
	}

	// JUnitDoclet begin javadoc_method commandExecuted()
	/**
	 * Method testCommandExecuted is testing commandExecuted
	 * @see org.jhotdraw.util.UndoableCommand#commandExecuted(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method commandExecuted()
	public void testCommandExecuted() throws Exception {
		// JUnitDoclet begin method commandExecuted
		// JUnitDoclet end method commandExecuted
	}

	// JUnitDoclet begin javadoc_method commandExecutable()
	/**
	 * Method testCommandExecutable is testing commandExecutable
	 * @see org.jhotdraw.util.UndoableCommand#commandExecutable(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method commandExecutable()
	public void testCommandExecutable() throws Exception {
		// JUnitDoclet begin method commandExecutable
		// JUnitDoclet end method commandExecutable
	}

	// JUnitDoclet begin javadoc_method commandNotExecutable()
	/**
	 * Method testCommandNotExecutable is testing commandNotExecutable
	 * @see org.jhotdraw.util.UndoableCommand#commandNotExecutable(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method commandNotExecutable()
	public void testCommandNotExecutable() throws Exception {
		// JUnitDoclet begin method commandNotExecutable
		// JUnitDoclet end method commandNotExecutable
	}

	// JUnitDoclet begin javadoc_method testVault
	/**
	 * JUnitDoclet moves marker to this method, if there is not match
	 * for them in the regenerated code and if the marker is not empty.
	 * This way, no test gets lost when regenerating after renaming.
	 * <b>Method testVault is supposed to be empty.</b>
	 */
	// JUnitDoclet end javadoc_method testVault
	public void testVault() throws Exception {
		// JUnitDoclet begin method testcase.testVault
		// JUnitDoclet end method testcase.testVault
	}

}
