package sample;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.mariuszgromada.math.mxparser.mXparser;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML public TextArea equationDisplayer;
    @FXML public TableView expressionsTable;
    @FXML public Button drawExpression;
    @FXML public LineChart graphDisplayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Argument x = new Argument("x");
        mXparser.setDegreesMode();
        ArrayList resultsList = new ArrayList();
        drawExpression.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                Function f = new Function("f", equationDisplayer.getText(), "x");
                Expression a = new Expression(equationDisplayer.getText(), x);
                expressionsTable.getItems().add(a);
                equationDisplayer.clear();
                final XYChart.Series<Double, Double> drawFunction = new XYChart.Series<>();
                int j = 0;
                double d;
                for(int i = -200; i <= 200; i++){
                    Expression e = new Expression("f(" + Integer.toString(i) + ")", f);
                    resultsList.add(e.calculate());
                    d = i;
                    drawFunction.getData().add(new XYChart.Data<Double, Double>(d, Double.parseDouble(resultsList.get(j).toString())));
                    System.out.println(resultsList.get(j));
                    j++;
                }
                graphDisplayer.getData().add(drawFunction);
            }
        });
        equationDisplayer.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                keyEvent.consume();
                Function f = new Function("f", equationDisplayer.getText(), "x");
                Expression a = new Expression(equationDisplayer.getText(), x);
                expressionsTable.getItems().add(a);
                equationDisplayer.clear();
                final XYChart.Series<Double, Double> drawFunction = new XYChart.Series<>();
                int j = 0;
                double d;
                for(int i = -200; i <= 200; i++){
                    Expression e = new Expression("f(" + Integer.toString(i) + ")", f);
                    resultsList.add(e.calculate());
                    d = i;
                    drawFunction.getData().add(new XYChart.Data<Double, Double>(d, Double.parseDouble(resultsList.get(j).toString())));
                    System.out.println(resultsList.get(j));
                    j++;
                }
                graphDisplayer.getData().add(drawFunction);
            }
        });
    }
}
