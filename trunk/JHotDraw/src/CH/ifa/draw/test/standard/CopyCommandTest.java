package CH.ifa.draw.test.standard;

// JUnitDoclet begin import
import CH.ifa.draw.test.JHDTestCase;
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
* TestCase CopyCommandTest is generated by
* JUnitDoclet to hold the tests for CopyCommand.
* @see CH.ifa.draw.standard.CopyCommand
*/
// JUnitDoclet end javadoc_class
public class CopyCommandTest
// JUnitDoclet begin extends_implements
extends JHDTestCase
// JUnitDoclet end extends_implements
{
  // JUnitDoclet begin class
  // instance variables, helper methods, ... put them in this marker
  CH.ifa.draw.standard.CopyCommand copycommand = null;
  // JUnitDoclet end class
  
  /**
  * Constructor CopyCommandTest is
  * basically calling the inherited constructor to
  * initiate the TestCase for use by the Framework.
  */
  public CopyCommandTest(String name) {
    // JUnitDoclet begin method CopyCommandTest
    super(name);
    // JUnitDoclet end method CopyCommandTest
  }
  
  /**
  * Factory method for instances of the class to be tested.
  */
  public CH.ifa.draw.standard.CopyCommand createInstance() throws Exception {
    // JUnitDoclet begin method testcase.createInstance
    return new CH.ifa.draw.standard.CopyCommand("TestCopy", getDrawingEditor());
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
    copycommand = createInstance();
    // JUnitDoclet end method testcase.setUp
  }
  
  /**
  * Method tearDown is overwriting the framework method to
  * clean up after each single test of this TestCase.
  * It's called from the JUnit framework only.
  */
  protected void tearDown() throws Exception {
    // JUnitDoclet begin method testcase.tearDown
    copycommand = null;
    super.tearDown();
    // JUnitDoclet end method testcase.tearDown
  }
  
  // JUnitDoclet begin javadoc_method execute()
  /**
  * Method testExecute is testing execute
  * @see CH.ifa.draw.standard.CopyCommand#execute()
  */
  // JUnitDoclet end javadoc_method execute()
  public void testExecute() throws Exception {
    // JUnitDoclet begin method execute
    // JUnitDoclet end method execute
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
  
  /**
  * Method to execute the TestCase from command line
  * using JUnit's textui.TestRunner .
  */
  public static void main(String[] args) {
    // JUnitDoclet begin method testcase.main
    junit.textui.TestRunner.run(CopyCommandTest.class);
    // JUnitDoclet end method testcase.main
  }
}
