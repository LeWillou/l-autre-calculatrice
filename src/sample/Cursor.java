package sample;

import java.util.List;

import javafx.scene.control.TextArea;

public class Cursor {
	private int pos;
	
	
	public void moveLeft(TextArea myScreen)
	{
		String tmpText;
		myScreen.selectPositionCaret(this.pos);
		tmpText = myScreen.getText(myScreen.getCaretPosition()-2,myScreen.getCaretPosition()-1);
		//System.out.println("posCaret = " + myScreen.getCaretPosition() + " valeur = " +tmpText);
		myScreen.deleteText(myScreen.getCaretPosition()-2,myScreen.getCaretPosition());
		//System.out.println("After delete, new pos : " + myScreen.getCaretPosition());
		myScreen.insertText(myScreen.getCaretPosition(),"|");
        myScreen.insertText(myScreen.getCaretPosition(),tmpText);
        this.pos = this.pos - 1;
	}
	
	public void moveRight(TextArea myScreen)
	{
		String tmpText;
		myScreen.selectPositionCaret(this.pos);
		tmpText = myScreen.getText(myScreen.getCaretPosition(),myScreen.getCaretPosition()+1);
		//System.out.println("posCaret = " + myScreen.getCaretPosition() + " valeur = " +tmpText);
		myScreen.deleteText(myScreen.getCaretPosition()-1,myScreen.getCaretPosition()+1);
		//System.out.println("After delete, new pos : " + myScreen.getCaretPosition());
		myScreen.insertText(myScreen.getCaretPosition(),tmpText);
        myScreen.insertText(myScreen.getCaretPosition(),"|");
        this.pos = this.pos + 1;
	}
	
	public void testInsert(TextArea myScreen)
	{
		myScreen.insertText(1,"OK");
	}
	
	public void writeAtCursor(TextArea myScreen, List<String> myTextAreaElements, String myText)
	{
		myTextAreaElements.add(myText);
		myScreen.selectPositionCaret(this.pos);
		myScreen.deleteText(myScreen.getCaretPosition()-1,myScreen.getCaretPosition());
        myScreen.insertText(myScreen.getCaretPosition(),myText);
        myScreen.insertText(myScreen.getCaretPosition(),"|");
        this.pos++;
	}
	
	public Cursor(TextArea myScreen)
	{
		this.pos = 1;
		myScreen.setText("|");
	}
}
