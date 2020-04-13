package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    protected List<String> textAreaElements = new ArrayList<String>();
    Iterator<String> myIter = textAreaElements.iterator();

    @FXML public Button keyOne, keyTwo, keyThree, keyFour, keyFive, keySix, keySeven, keyEight, keyNine, keyZero, keyPlus, keyMinus, keyMultiply, keyDivide, keyResult;
    @FXML public Button keyDown, keyUp, keyLeft, keyRight, keyOK;
    @FXML public Button keyFunction;
    @FXML public TextArea screen;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        screen.setEditable(false);
        Cursor myCursor = new Cursor(screen);
        textAreaElements.add(" ");

        keyFunction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               textAreaElements.clear();
               screen.setText("");
            }
        });
        keyLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               myCursor.moveLeft(screen);
            }
        });
        keyRight.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent actionEvent) {
        		myCursor.moveRight(screen);
        	}
        });
        keyUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               
            }
        });
        
        keyOne.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               myCursor.writeAtCursor(screen,textAreaElements,"1");
            }
        });
        keyTwo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"2");
            }
        });
        keyThree.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"3");
            }
        });
        keyFour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"4");
            }
        });
        keyFive.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"5");
            }
        });
        keySix.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"6");
            }
        });
        keySeven.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"7");
            }
        });
        keyEight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"8");
            }
        });
        keyNine.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"9");
            }
        });
        keyZero.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"0");
            }
        });

        keyPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"+");
            }
        });
        keyMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"-");
            }
        });
        keyMultiply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"*");
            }
        });
        keyDivide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                myCursor.writeAtCursor(screen,textAreaElements,"/");
            }
        });


        keyResult.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	String randomText;
            	while(myIter.hasNext())
            	{
            		randomText=myIter.next();
            		System.out.println(randomText);
            	}
                /*textAreaElements.add(" ");
                String toAdd = null;
                int counter = 0;
                List<String> parsedTextArea = new ArrayList<String>();
                while(counter < textAreaElements.size()){
                    if(!textAreaElements.get(counter).matches("\\d+")){
                        for(int j = 0; j <= counter - 1; j++){
                            toAdd += textAreaElements.get(j);
                        }
                        parsedTextArea.add(toAdd);
                        toAdd = null;
                        parsedTextArea.add(textAreaElements.get(counter));
                        for(int j = 0; j == counter; j++){
                            textAreaElements.remove(j);
                        }
                        counter = 0;
                    }
                    else{
                        counter++;
                    }
                }*/
            }
        });

    }
}
