package sample;

import javafx.scene.control.TableView;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML public TextArea equationDisplayer;
    @FXML public TableView expressionsTable;
    @FXML public Button drawExpression;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Argument x = new Argument("x");
        drawExpression.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                Expression f = new Expression(equationDisplayer.getText(), x);
                expressionsTable.getItems().add(f);
                equationDisplayer.clear();
            }
        });
    }
}
