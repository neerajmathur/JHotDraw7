/*
 * @(#)TextTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	� by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.figures;

import CH.ifa.draw.framework.*;
import CH.ifa.draw.standard.*;
import CH.ifa.draw.util.FloatingTextField;
import CH.ifa.draw.util.UndoableAdapter;
import CH.ifa.draw.util.Undoable;
import java.awt.*;


/**
 * Tool to create new or edit existing text figures.
 * The editing behavior is implemented by overlaying the
 * Figure providing the text with a FloatingTextField.<p>
 * A tool interaction is done once a Figure that is not
 * a TextHolder is clicked.
 *
 * @see TextHolder
 * @see FloatingTextField
 *
 * @version <$CURRENT_VERSION$>
 */
public class TextTool extends CreationTool {

	private FloatingTextField   myTextField;
	private TextHolder  myTypingTarget;

	public TextTool(DrawingEditor newDrawingEditor, Figure prototype) {
		super(newDrawingEditor, prototype);
	}

	/**
	 * If the pressed figure is a TextHolder it can be edited otherwise
	 * a new text figure is created.
	 */
	public void mouseDown(DrawingViewMouseEvent dvme)
	{
		setView( dvme.getDrawingView() );
		Figure pressedFigure = drawing().findFigureInside(dvme.getX(), dvme.getY());
		TextHolder textHolder = null;
		if (pressedFigure != null) {
			textHolder = pressedFigure.getTextHolder();
		}

		if ((textHolder != null) && textHolder.acceptsTyping()) {
			beginEdit(textHolder);
		}
		else if (getTypingTarget() != null) {
			editor().toolDone();
		}
		else {
			super.mouseDown(dvme);
			// update view so the created figure is drawn before the floating text
			// figure is overlaid. (Note, fDamage should be null in StandardDrawingView
			// when the overlay figure is drawn because a JTextField cannot be scrolled)
			view().checkDamage();
			beginEdit(getCreatedFigure().getTextHolder());
		}
	}

	public void mouseDrag(DrawingViewMouseEvent dvme) {
	}

	public void mouseUp(DrawingViewMouseEvent dvme) {
		if (!isActive()) {
			editor().toolDone();
		}
	}

	/**
	 * Terminates the editing of a text figure.
	 */
	public void deactivate() {
		endEdit();
        super.deactivate();
	}

	/**
	 * Sets the text cursor.
	 */
	public void activate() {
		super.activate();
		// JDK1.1 TEXT_CURSOR has an incorrect hot spot
		//view().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
	}

	/**
	 * Test whether the text tool is currently activated and is displaying
	 * a overlay TextFigure for accepting input.
	 *
	 * @return true, if the text tool has a accepting target TextFigure for its input, false otherwise
	 */
	public boolean isActive() {
		return (getTypingTarget() != null);
	}

	protected void beginEdit(TextHolder figure) {
		if (getFloatingTextField() == null) {
			setFloatingTextField(createFloatingTextField());
		}

		if (figure != getTypingTarget() && getTypingTarget() != null) {
			endEdit();
		}

		getFloatingTextField().createOverlay((Container)view(), figure.getFont());
		getFloatingTextField().setBounds(fieldBounds(figure), figure.getText());

		setTypingTarget(figure);

		setUndoActivity(createUndoActivity());
	}

	protected void endEdit() {
		if (getTypingTarget() != null) {
			if (getFloatingTextField().getText().length() > 0) {
				getTypingTarget().setText(getFloatingTextField().getText());
			}
			else {
				drawing().orphan(getAddedFigure());
			}

			TextTool.UndoActivity undoActivity = ((TextTool.UndoActivity)getUndoActivity());
			if ((undoActivity.getOriginalText() != null) || (getTypingTarget().getText() != null)) {
				// put created figure into a figure enumeration
				getUndoActivity().setAffectedFigures(
					new SingleFigureEnumerator(getAddedFigure()));
				undoActivity.setBackupText(getTypingTarget().getText());
			}
			else {
				setUndoActivity(null);
			}

			setTypingTarget(null);
			getFloatingTextField().endOverlay();
		}
	}

	private Rectangle fieldBounds(TextHolder figure) {
		Rectangle box = figure.textDisplayBox();
		int nChars = figure.overlayColumns();
		Dimension d = getFloatingTextField().getPreferredSize(nChars);
		return new Rectangle(box.x, box.y, d.width, d.height);
	}

	protected void setTypingTarget(TextHolder newTypingTarget) {
		myTypingTarget = newTypingTarget;
	}

	protected TextHolder getTypingTarget() {
		return myTypingTarget;
	}

	private FloatingTextField createFloatingTextField() {
		return new FloatingTextField();
	}

	private void setFloatingTextField(FloatingTextField newFloatingTextField) {
		myTextField = newFloatingTextField;
	}

	protected FloatingTextField getFloatingTextField() {
		return myTextField;
	}

	/**
	 * Factory method for undo activity
	 */
	protected Undoable createUndoActivity() {
		return new TextTool.UndoActivity(view(), getTypingTarget().getText());
	}

	public static class UndoActivity extends UndoableAdapter {
		private String myOriginalText;
		private String myBackupText;

		public UndoActivity(DrawingView newDrawingView, String newOriginalText) {
			super(newDrawingView);
			setOriginalText(newOriginalText);
			setUndoable(true);
			setRedoable(true);
		}

		/*
		 * Undo the activity
		 * @return true if the activity could be undone, false otherwise
		 */
		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			getDrawingView().clearSelection();

			if (!isValidText(getOriginalText())) {
				FigureEnumeration fe  = getAffectedFigures();
				while (fe.hasNextFigure()) {
					getDrawingView().drawing().orphan(fe.nextFigure());
				}
			}
			// add text figure if it has been removed (no backup text)
			else if (!isValidText(getBackupText())) {
				FigureEnumeration fe  = getAffectedFigures();
				while (fe.hasNextFigure()) {
					getDrawingView().add(fe.nextFigure());
				}
				setText(getOriginalText());
			}
			else {
				setText(getOriginalText());
			}

			return true;
		}

		/*
		 * Redo the activity
		 * @return true if the activity could be redone, false otherwise
		 */
		public boolean redo() {
			if (!super.redo()) {
				return false;
			}

			getDrawingView().clearSelection();

			// the text figure did exist but was remove
			if (!isValidText(getBackupText())) {
				FigureEnumeration fe  = getAffectedFigures();
				while (fe.hasNextFigure()) {
					getDrawingView().drawing().orphan(fe.nextFigure());
				}
			}
			// the text figure didn't exist before
			else if (!isValidText(getOriginalText())) {
				FigureEnumeration fe  = getAffectedFigures();
				while (fe.hasNextFigure()) {
					getDrawingView().drawing().add(fe.nextFigure());
					setText(getBackupText());
				}
			}
			else {
				setText(getBackupText());
			}

			return true;
		}

		protected boolean isValidText(String toBeChecked) {
			return ((toBeChecked != null) && (toBeChecked.length() > 0));
		}

		protected void setText(String newText) {
			FigureEnumeration fe = getAffectedFigures();
			while (fe.hasNextFigure()) {
				Figure currentFigure = fe.nextFigure();
				if (currentFigure.getTextHolder() != null) {
					currentFigure.getTextHolder().setText(newText);
				}
			}
		}

		public void setBackupText(String newBackupText) {
			myBackupText = newBackupText;
		}

		public String getBackupText() {
			return myBackupText;
		}

		public void setOriginalText(String newOriginalText) {
			myOriginalText = newOriginalText;
		}

		public String getOriginalText() {
			return myOriginalText;
		}
	}
}

