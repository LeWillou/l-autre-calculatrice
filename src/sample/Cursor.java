package sample;

import javafx.scene.control.TextArea;

public class Cursor {
	private int pos;
	
	public void writeAtCursor(TextArea myScreen , String myText)
	{
		
	}
	
	public Cursor(TextArea myScreen)
	{
		this.pos = 0;
		myScreen.setText("|");
	}
}
