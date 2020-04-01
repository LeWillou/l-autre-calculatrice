package sample;

import java.util.List;

import javafx.scene.control.TextArea;

public class Cursor {
	private int pos;
	
	
	public void moveLeft(TextArea myScreen)
	{
		String tmpText;
		System.out.println("Move Left");
		myScreen.selectEnd();
		tmpText = myScreen.getText(myScreen.getCaretPosition()-2,myScreen.getCaretPosition()-1);
		myScreen.deleteText(myScreen.getCaretPosition()-2,myScreen.getCaretPosition());
		myScreen.appendText("|");
        myScreen.appendText(tmpText);
		
	}
	
	
	public void writeAtCursor(TextArea myScreen, List<String> myTextAreaElements, String myText)
	{
		myTextAreaElements.add(myText);
		myScreen.selectEnd();
		myScreen.deleteText(myScreen.getCaretPosition()-1,myScreen.getCaretPosition());
        myScreen.appendText(myText);
        myScreen.appendText("|");
	}
	
	public Cursor(TextArea myScreen)
	{
		this.pos = 0;
		myScreen.setText("|");
	}
}
