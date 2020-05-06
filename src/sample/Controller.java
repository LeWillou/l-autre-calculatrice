package sample;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
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


    @FXML public TextArea equationDisplayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
