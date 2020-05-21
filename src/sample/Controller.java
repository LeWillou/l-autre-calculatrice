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
import org.mariuszgromada.math.mxparser.parsertokens.Token;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {
    @FXML public TextArea equationDisplayer;
    @FXML public TableView expressionsTable;
    @FXML public Button drawExpression;
    @FXML public LineChart graphDisplayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Argument x = new Argument("x");
        //set degrees mode for trigo
        mXparser.setDegreesMode();
        ArrayList<Double> resultsList = new ArrayList();
        ArrayList<Function> expressionsList = new ArrayList<Function>();
        ArrayList<Double> normalizedPoints = new ArrayList<Double>();


        drawExpression.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                String[] splitTextArea = equationDisplayer.getText().split("");

                if(equationDisplayer.getText().contains("x")) {
                    Function f = new Function(equationDisplayer.getText());

                    Expression e = new Expression(equationDisplayer.getText());
                    ArrayList<Token> tokens = (ArrayList<Token>) e.getCopyOfInitialTokens();

                    StringBuffer newExpression = new StringBuffer();
                    newExpression.append(f.getFunctionName() + "(x) =");

                    if (tokens.get(5).tokenId == -1){
                        for(int j = 5; j < tokens.size(); j++) {
                            for (int i = 0; i < expressionsList.size() ; i++) {
                                if (tokens.get(j).tokenStr.equals(expressionsList.get(i).getFunctionName())) {
                                    newExpression.append(expressionsList.get(i).getFunctionExpressionString() + " ");
                                    if(j + 4 < tokens.size()){
                                        newExpression.append(tokens.get(j+4).tokenStr);
                                    }
                                }
                            }
                        }
                        f = new Function(newExpression.toString());
                    }

                    UserDefinedFunction function = new UserDefinedFunction(f.getFunctionName(), f.getFunctionExpressionString());
                    expressionsList.add(f);
                    expressionsTable.getItems().add(function);
                    equationDisplayer.clear();
                    final XYChart.Series<Double, Double> drawFunction = new XYChart.Series<>();
                    int j = 0;
                    double d;

                    int iMax = 0;

                    for (int i = -200; i <= 200; i++) {
                        if(f.calculate(i) > -10 && f.calculate(i) < 10) {
                            resultsList.add(f.calculate(i));
                            iMax = i;
                        }
                    }

                    System.out.println(f.getFunctionExpressionString());

                    if(resultsList.size() < 401){
                        for(float k = -iMax; k <= iMax; k += (resultsList.get(resultsList.size()-1)/400)){
                            Expression normalizePointsNumber = new Expression(f.getFunctionName() + "(" + Float.toString(k) + ")", f);
                            if(!Double.toString(normalizePointsNumber.calculate()).equals("NaN")) {
                                normalizedPoints.add(normalizePointsNumber.calculate());
                                System.out.println(normalizedPoints.get(j));
                                drawFunction.getData().add(new XYChart.Data<Double, Double>(Double.parseDouble(Float.toString(k)), Double.parseDouble(normalizedPoints.get(j).toString())));
                                j++;
                            }
                        }
                        normalizedPoints.removeAll(normalizedPoints);
                    }

                    else{
                        for(int i = -200; i <= 200; i++) {
                            drawFunction.getData().add(new XYChart.Data<Double, Double>(Double.parseDouble(Integer.toString(i)), Double.parseDouble(resultsList.get(j).toString())));
                            j++;
                        }
                    }

                    resultsList.removeAll(resultsList);
                    graphDisplayer.getData().add(drawFunction);

                }

                else{
                    boolean isFound = false;

                    String functionArgument = null;
                    Matcher m = Pattern.compile("\\((.*?)\\)").matcher(equationDisplayer.getText());

                    while(m.find()){
                        functionArgument = m.group(1);
                    }

                    for(int i = 0; i < expressionsList.size(); i++){
                        if(splitTextArea[0].equals(expressionsList.get(i).getFunctionName())){
                            Expression found = new Expression(expressionsList.get(i).getFunctionName() + "(" + functionArgument + ")", expressionsList.get(i));
                            equationDisplayer.appendText(" = " + found.calculate());
                            isFound = true;
                        }
                    }
                    if(!isFound){
                        equationDisplayer.clear();
                        equationDisplayer.appendText("Function not found. Try to define it before.");
                    }
                }

            }
        });
        equationDisplayer.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                keyEvent.consume();

                String[] splitTextArea = equationDisplayer.getText().split("");

                if(equationDisplayer.getText().contains("x")) {
                    Function f = new Function(equationDisplayer.getText());

                    Expression e = new Expression(equationDisplayer.getText());
                    ArrayList<Token> tokens = (ArrayList<Token>) e.getCopyOfInitialTokens();

                    StringBuffer newExpression = new StringBuffer();
                    newExpression.append(f.getFunctionName() + "(x) =");

                    if (tokens.get(5).tokenId == -1){
                        for(int j = 5; j < tokens.size(); j++) {
                            for (int i = 0; i < expressionsList.size() ; i++) {
                                if (tokens.get(j).tokenStr.equals(expressionsList.get(i).getFunctionName())) {
                                    newExpression.append(expressionsList.get(i).getFunctionExpressionString() + " ");
                                    if(j + 4 < tokens.size()){
                                        newExpression.append(tokens.get(j+4).tokenStr);
                                    }
                                }
                            }
                        }
                        f = new Function(newExpression.toString());
                    }

                    UserDefinedFunction function = new UserDefinedFunction(f.getFunctionName(), f.getFunctionExpressionString());
                    expressionsList.add(f);
                    expressionsTable.getItems().add(function);
                    equationDisplayer.clear();
                    final XYChart.Series<Double, Double> drawFunction = new XYChart.Series<>();
                    int j = 0;
                    double d;

                    int iMax = 0;

                    for (int i = -200; i <= 200; i++) {
                        if(f.calculate(i) > -10 && f.calculate(i) < 10) {
                            resultsList.add(f.calculate(i));
                            iMax = i;
                        }
                    }

                    System.out.println(f.getFunctionExpressionString());

                    if(resultsList.size() < 401){
                        for(float k = -iMax; k <= iMax; k += (resultsList.get(resultsList.size()-1)/400)){
                            Expression normalizePointsNumber = new Expression(f.getFunctionName() + "(" + Float.toString(k) + ")", f);
                            if(!Double.toString(normalizePointsNumber.calculate()).equals("NaN")) {
                                normalizedPoints.add(normalizePointsNumber.calculate());
                                System.out.println(normalizedPoints.get(j));
                                drawFunction.getData().add(new XYChart.Data<Double, Double>(Double.parseDouble(Float.toString(k)), Double.parseDouble(normalizedPoints.get(j).toString())));
                                j++;
                            }
                        }
                        normalizedPoints.removeAll(normalizedPoints);
                    }

                    else{
                        for(int i = -200; i <= 200; i++) {
                            drawFunction.getData().add(new XYChart.Data<Double, Double>(Double.parseDouble(Integer.toString(i)), Double.parseDouble(resultsList.get(j).toString())));
                            j++;
                        }
                    }

                    resultsList.removeAll(resultsList);
                    graphDisplayer.getData().add(drawFunction);

                }

                else{
                    boolean isFound = false;

                    String functionArgument = null;
                    Matcher m = Pattern.compile("\\((.*?)\\)").matcher(equationDisplayer.getText());

                    while(m.find()){
                        functionArgument = m.group(1);
                    }

                    for(int i = 0; i < expressionsList.size(); i++){
                        if(splitTextArea[0].equals(expressionsList.get(i).getFunctionName())){
                            Expression found = new Expression(expressionsList.get(i).getFunctionName() + "(" + functionArgument + ")", expressionsList.get(i));
                            equationDisplayer.appendText(" = " + found.calculate());
                            isFound = true;
                        }
                    }
                    if(!isFound){
                        equationDisplayer.clear();
                        equationDisplayer.appendText("Function not found. Try to define it before.");
                    }
                }
            }
        });
    }
}
