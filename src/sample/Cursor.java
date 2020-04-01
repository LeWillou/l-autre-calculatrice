package sample;

import java.util.List;

import javafx.scene.control.TextArea;

public class Cursor {
	private int pos;
	
	
	public void moveLeft(TextArea myScreen)
	{
		String tmpText;
		System.out.println("Move Left");
		myScreen.selectPositionCaret(this.pos);
		tmpText = myScreen.getText(myScreen.getCaretPosition()-1,myScreen.getCaretPosition());
		myScreen.deleteText(myScreen.getCaretPosition()-2,myScreen.getCaretPosition());
		myScreen.appendText("|");
        myScreen.appendText(tmpText);
        this.pos = myScreen.getCaretPosition()-2;
		
	}
	
	
	public void writeAtCursor(TextArea myScreen, List<String> myTextAreaElements, String myText)
	{
		myTextAreaElements.add(myText);
		myScreen.selectPositionCaret(this.pos);
		myScreen.deleteText(myScreen.getCaretPosition()-1,myScreen.getCaretPosition());
        myScreen.appendText(myText);
        myScreen.appendText("|");
        this.pos++;
	}
	
	public Cursor(TextArea myScreen)
	{
		this.pos = 1;
		myScreen.setText("|");
	}
}
