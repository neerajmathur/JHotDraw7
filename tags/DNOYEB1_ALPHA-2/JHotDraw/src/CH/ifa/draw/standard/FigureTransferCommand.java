/*
 * @(#)FigureTransferCommand.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	� by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.standard;

import CH.ifa.draw.util.*;
import CH.ifa.draw.framework.*;

/**
 * Common base clase for commands that transfer figures
 * between a drawing and the clipboard.
 *
 * @version <$CURRENT_VERSION$>
 */
public abstract class FigureTransferCommand extends AbstractCommand {

	/**
	 * Constructs a drawing command.
	 * @param name the command name
	 * @param newDrawingEditor the DrawingEditor which manages the views
	 */
	protected FigureTransferCommand(String name, DrawingEditor newDrawingEditor) {
		super(name, newDrawingEditor);
	}

   /**
	* Deletes the selection from the drawing.
	*/
	protected FigureEnumeration deleteFigures(FigureEnumeration fe) {
	   DeleteFromDrawingVisitor deleteVisitor = new DeleteFromDrawingVisitor(view().drawing());
		while (fe.hasNextFigure()) {
			fe.nextFigure().visit(deleteVisitor);
		}

		view().clearSelection();
		return deleteVisitor.getDeletedFigures();
	}

   /**
	* Copies the FigureEnumeration to the clipboard.
	*/
	protected void copyFigures(FigureEnumeration fe, int figureCount) {
		Clipboard.getClipboard().setContents(new StandardFigureSelection(fe, figureCount));
	}

   /**
	* Inserts an enumeration of figures and translates them by the
	* given offset.
	*/
	protected FigureEnumeration insertFigures(FigureEnumeration fe, int dx, int dy) {
		return view().insertFigures(fe, dx, dy, false);
	}
}

