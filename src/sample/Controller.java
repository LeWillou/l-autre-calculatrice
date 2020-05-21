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

import java.lang.reflect.Array;
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
    @FXML public TableView<UserDefinedFunction> expressionsTable;
    @FXML public Button drawExpression;
    @FXML public LineChart graphDisplayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //--------------------------------------------------------------------------------------------------------------
        //-----------------------
        //Attributes definition |
        //-----------------------
        //Set mode for Sine and Cos functions representations
        mXparser.setRadiansMode();

        //Arrays definition to store user defined functions and calculation results
        ArrayList<Double> resultsList = new ArrayList<Double>();
        ArrayList<Function> expressionsList = new ArrayList<Function>();

        //Array used to make the number of points represented the same for every function
        ArrayList<Double> normalizedPoints = new ArrayList<Double>();
        //--------------------------------------------------------------------------------------------------------------


        //--------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------
        //Calculate and draw user given function by clicking on the button |
        //------------------------------------------------------------------
        //
        drawExpression.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {

                //Used in case the user is asking to calculate a function by giving an explicit integer argument
                String[] splitTextArea = equationDisplayer.getText().split(" ");

                //If the String contains "x", it means the user is asking to define a function and is not asking to
                //calculate it.
                if(equationDisplayer.getText().contains("x")) {

                    //--------------------------------------------------------------------------------------------------
                    //--------------------------------------------------------------
                    //Check if the user gave us user defined or built-in functions |
                    //--------------------------------------------------------------


                    //We give the user given String as argument of the Function constructor, in case the said String
                    //doesn't match the test below, we already have a function definition here.
                    Function f = new Function(equationDisplayer.getText());

                    //We give the user given String as argument of the Expression function constructor to get the
                    //expression tokens so we can make our test right below. We store the tokens in an array to
                    //easy the manipulation.
                    Expression e = new Expression(equationDisplayer.getText());
                    ArrayList<Token> tokens = (ArrayList<Token>) e.getCopyOfInitialTokens();

                    //This StringBuffer will be used as our new String which will be given as argument of the f
                    //constructor in case the expression's tokens match the texs below.
                    StringBuilder newExpression = new StringBuilder();
                    newExpression.append(f.getFunctionName()).append("(x) =");

                    //We are going to test if the expression token at the 5th position of the user given String is
                    //unrecognized by mXparser, which would mean it is a function.
                    //By giving a "f(x) = g(x)" String, we already know f is a function, but we don't know what is
                    //"g" (at the 5th position), se we check the token, which will have a -1 ID (unrecognized).
                    if (tokens.get(5).tokenId == -1){

                        //We carry on the test by beginning at the 5th position, to see if there is another
                        //function call deeper in the String.
                        for(int j = 5; j < tokens.size(); j++) {

                            //We now check if we have the function(s) letter(s) in our array storing all the
                            //expressions the user already defined.
                            for (Function function : expressionsList) {
                                if (tokens.get(j).tokenStr.equals(function.getFunctionName())) {

                                    //The corresponding function is now found, so we append it to our StringBuffer
                                    //so we can replace the function call by the actual function expression in
                                    //our Function constructor argument.
                                    newExpression.append(function.getFunctionExpressionString() + " ");

                                    //If we just found a function matching what the user is asking, we kno there is
                                    //probably an operator right after. So we now check if there is something 4
                                    //tokens after, the 3 remaining tokens after the function call being '(', 'arg' and
                                    //')'
                                    //If there is something, we append it to our String.
                                    if (j + 4 < tokens.size()) {
                                        newExpression.append(tokens.get(j + 4).tokenStr);
                                    }
                                }
                            }
                        }

                        //After the loops, we call the Function constructor with our updated StringBuffer to give as
                        //argument the String with function expressions rather than function calls.
                        f = new Function(newExpression.toString());
                    }
                    //--------------------------------------------------------------------------------------------------


                    //--------------------------------------------------------------------------------------------------
                    //--------------------------------------------
                    //Updating expression database and interface |
                    //--------------------------------------------

                    //We created a new USerDefinedFunction Class so it is way easier to send information to our JavaFX
                    //TableView.
                    UserDefinedFunction function = new UserDefinedFunction(f.getFunctionName(), f.getFunctionExpressionString());

                    //Updating expression array with the user given function
                    expressionsList.add(f);

                    for(int i = 0; i < expressionsList.size(); i++){
                        System.out.print("\n" + expressionsList.get(i).getFunctionName() + " = " + expressionsList.get(i).getFunctionExpressionString() + "\n");
                    }

                    //Updating the TableView with the user given function
                    expressionsTable.getItems().add(function);

                    //Clearing the TextArea so the user can enter something new
                    equationDisplayer.clear();
                    //
                    //--------------------------------------------------------------------------------------------------


                    //--------------------------------------------------------------------------------------------------
                    //------------------------------------------------
                    // Getting down to the real business, part 1 :   |
                    //          plotting our functions               |
                    //------------------------------------------------

                    //Creating the series we will use to display the points on the LineChart
                    final XYChart.Series<Double, Double> drawFunction = new XYChart.Series<>();

                    //Variable used to add the right value of the results array to the LineChart series.
                    int j = 0;

                    //--------------------------------------------------------------------------------------------------
                    //------------------------------------------------------
                    //Rendering the same amount of points for any function |
                    //------------------------------------------------------

                    //Creating the value that will be the highest 'x' the function can reach before reaching the 'y'
                    //boundary.
                    int iMax = 0;

                    //We now calculate all the results the given function would give us.
                    for (int i = -20; i <= 20; i++) {

                        //Only the results between -10 and 10 will be kept.
                        if(f.calculate(i) > -10 && f.calculate(i) < 10) {

                            //We store these results in our results array
                            resultsList.add(f.calculate(i));

                            //iMax will now be the highest 'x' value the function can reach in our results array
                            iMax = i;
                        }
                    }

                    //--------------------------------------------------------------------------------------------------

                    //We now try to make the number of points proportional given the boundaries above. With the right
                    //function, the highest figure of points we can have without changing anything would be 41 (from -20
                    //to 20, going by 0.
                    if(resultsList.size() < 41){

                        //iMax will allow us to set artificially our boundaries directly to the function calculation
                        //so we are sure we will only have values between our 'y' boundaries. To make the figure of
                        //points as the other functions, we divide the highest value of our function before reaching
                        //the boundary by the number of points we actually want to have, which is 41.
                        for(float k = -iMax; k <= iMax; k += (resultsList.get(resultsList.size()-1)/41)){

                            //A bit of dark magic there, concatenating a String so we have a proper Expression
                            //constructor call with our right declaration (being 'f(k)'). Thought I could reach the
                            //same result by calling f.calculate(k) but got faced by a wild error I still don't know
                            //how to fix.
                            Expression normalizePointsNumber = new Expression(f.getFunctionName() + "(" + Float.toString(k) + ")", f);

                            //We don't want in our array (even less do we in our graph) values that actually don't
                            //exist. So we just don't add these said values in our array.
                            if(!Double.toString(normalizePointsNumber.calculate()).equals("NaN")) {

                                //We add our newly created points in another array, fearing the constant update
                                //would make our program feeling a bit buggy during execution. Not optimum but working.
                                normalizedPoints.add(normalizePointsNumber.calculate());

                                //Adding our newly created points directly to our LineChart series. We were forced
                                //to create the normalizedPoints array as the 'Expression.calculate()' method
                                //cannot be cast to String, and we needed a String to be cast to Double.
                                //We are using the 'j' variable here to navigate in our array to give the series the
                                //right values.
                                drawFunction.getData().add(new XYChart.Data<Double, Double>(Double.parseDouble(Float.toString(k)), Double.parseDouble(normalizedPoints.get(j).toString())));
                                j++;
                            }
                        }

                        //We clear our array so we won't duplicate our function when adding new functions.
                        normalizedPoints.removeAll(normalizedPoints);
                    }

                    //If we already have the perfect figure of points in our array, we just add them directly to our
                    //LineChart series.
                    else{
                        for(int i = -20; i <= 20; i++) {
                            drawFunction.getData().add(new XYChart.Data<Double, Double>(Double.parseDouble(Integer.toString(i)), Double.parseDouble(resultsList.get(j).toString())));
                            j++;
                        }
                    }

                    //Clearing the results array so we won't create duplicates when adding new functions.
                    resultsList.removeAll(resultsList);
                    graphDisplayer.getData().add(drawFunction);

                }

                //------------------------------------------------------------------------------------------------------


                //------------------------------------------------------------------------------------------------------
                //-----------------------------------------------------------
                //       Getting down to the real business, part 2 :        |
                // calculate a function given an explicit integer argument  |
                //-----------------------------------------------------------

                //If the String does not contain an 'x', we assume the user wants to calculate a function by giving
                //an explicit integer argument.
                else{

                    //This boolean would be updated if the given function name is found in our database.
                    boolean isFound = false;

                    //Just as before, we will take a look at the expression's tokens to get the information we want
                    //in a "clean" way.
                    Expression e = new Expression(equationDisplayer.getText());
                    ArrayList<Token> tokens = new ArrayList<Token>(e.getCopyOfInitialTokens());

                    //We are going to search in our expressions array the function which has the same name as our
                    //very first token, assuming the user will use the first character as the function caller
                    //(like we do in maths).
                    for(Function function : expressionsList){
                        if(tokens.get(0).tokenStr.equals(function.getFunctionName())){

                            //Great, our the function caller is found in our expressions database. We now create a new
                            //expression, giving to the constructor the string given by the user and the name of the
                            //matching function in our database as arguments.
                            Expression found = new Expression(equationDisplayer.getText(), function);

                            //The explicit integer being given by the user, we just calculate our expression and we
                            //append the result in our TextArea. We decided not to create a new box which would just be
                            //used to display the result, the calculation not being the main function of our program.
                            //Besides, we found it pleasantly aesthetic to display our result this way, even more by
                            //pressing the "ENTER" key.
                            equationDisplayer.appendText(" = " + found.calculate());

                            //We update the value of our boolean, the function being found in our database.
                            isFound = true;
                        }
                    }

                    //If we don't find the function caller in our database, we just clear the TextArea and ask the user
                    //to define his function before trying to call it.
                    if(!isFound){
                            equationDisplayer.clear();
                            equationDisplayer.appendText("Function not found. Try defining it before.");
                    }
                }

                //------------------------------------------------------------------------------------------------------

            }
        });




        //--------------------------------------------------------------------------------------------------------------
        //----------------------------------------------------------
        //Calculate and draw user given function by pressing ENTER |
        //----------------------------------------------------------
        //
        equationDisplayer.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                keyEvent.consume();

                //We begin by creating a new equation String that will remove the carriage return character
                //created by the ENTER keyPress. This newly created String will replace all the
                //"equationDisplayer.getText()" in this part of the program. All the other things remain the same.
                String equation = equationDisplayer.getText().replaceAll("\\n", "");

                //Used in case the user is asking to calculate a function by giving an explicit integer argument
                String[] splitTextArea = equation.split(" ");

                //If the String contains "x", it means the user is asking to define a function and is not asking to
                //calculate it.
                if(equation.contains("x")) {

                    //--------------------------------------------------------------------------------------------------
                    //--------------------------------------------------------------
                    //Check if the user gave us user defined or built-in functions |
                    //--------------------------------------------------------------

                    //We give the user given String as argument of the Function constructor, in case the said String
                    //doesn't match the test below, we already have a function definition here.
                    Function f = new Function(equation);

                    //We give the user given String as argument of the Expression function constructor to get the
                    //expression tokens so we can make our test right below. We store the tokens in an array to
                    //easy the manipulation.
                    Expression e = new Expression(equation);
                    ArrayList<Token> tokens = (ArrayList<Token>) e.getCopyOfInitialTokens();

                    //This StringBuffer will be used as our new String which will be given as argument of the f
                    //constructor in case the expression's tokens match the texs below.
                    StringBuilder newExpression = new StringBuilder();
                    newExpression.append(f.getFunctionName()).append("(x) =");

                    //We are going to test if the expression token at the 5th position of the user given String is
                    //unrecognized by mXparser, which would mean it is a function.
                    //By giving a "f(x) = g(x)" String, we already know f is a function, but we don't know what is
                    //"g" (at the 5th position), se we check the token, which will have a -1 ID (unrecognized).
                    if (tokens.get(5).tokenId == -1){

                        //We carry on the test by beginning at the 5th position, to see if there is another
                        //function call deeper in the String.
                        for(int j = 5; j < tokens.size(); j++) {

                            //We now check if we have the function(s) letter(s) in our array storing all the
                            //expressions the user already defined.
                            for (Function function : expressionsList) {
                                if (tokens.get(j).tokenStr.equals(function.getFunctionName())) {

                                    //The corresponding function is now found, so we append it to our StringBuffer
                                    //so we can replace the function call by the actual function expression in
                                    //our Function constructor argument.
                                    newExpression.append(function.getFunctionExpressionString() + " ");

                                    //If we just found a function matching what the user is asking, we kno there is
                                    //probably an operator right after. So we now check if there is something 4
                                    //tokens after, the 3 remaining tokens after the function call being '(', 'arg' and
                                    //')'
                                    //If there is something, we append it to our String.
                                    if (j + 4 < tokens.size()) {
                                        newExpression.append(tokens.get(j + 4).tokenStr);
                                    }
                                }
                            }
                        }

                        //After the loops, we call the Function constructor with our updated StringBuffer to give as
                        //argument the String with function expressions rather than function calls.
                        f = new Function(newExpression.toString());
                    }
                    //--------------------------------------------------------------------------------------------------


                    //--------------------------------------------------------------------------------------------------
                    //--------------------------------------------
                    //Updating expression database and interface |
                    //--------------------------------------------

                    //We created a new USerDefinedFunction Class so it is way easier to send information to our JavaFX
                    //TableView.
                    UserDefinedFunction function = new UserDefinedFunction(f.getFunctionName(), f.getFunctionExpressionString());

                    //Updating expression array with the user given function
                    expressionsList.add(f);

                    for(int i = 0; i < expressionsList.size(); i++){
                        System.out.print("\n" + expressionsList.get(i).getFunctionName() + " = " + expressionsList.get(i).getFunctionExpressionString() + "\n");
                    }

                    //Updating the TableView with the user given function
                    expressionsTable.getItems().add(function);

                    //Clearing the TextArea so the user can enter something new
                    equationDisplayer.clear();
                    //
                    //--------------------------------------------------------------------------------------------------


                    //--------------------------------------------------------------------------------------------------
                    //------------------------------------------------
                    // Getting down to the real business, part 1 :   |
                    //          plotting our functions               |
                    //------------------------------------------------

                    //Creating the series we will use to display the points on the LineChart
                    final XYChart.Series<Double, Double> drawFunction = new XYChart.Series<>();

                    //Variable used to add the right value of the results array to the LineChart series.
                    int j = 0;

                    //--------------------------------------------------------------------------------------------------
                    //------------------------------------------------------
                    //Rendering the same amount of points for any function |
                    //------------------------------------------------------

                    //Creating the value that will be the highest 'x' the function can reach before reaching the 'y'
                    //boundary.
                    int iMax = 0;

                    //We now calculate all the results the given function would give us.
                    for (int i = -20; i <= 20; i++) {

                        //Only the results between -10 and 10 will be kept.
                        if(f.calculate(i) > -10 && f.calculate(i) < 10) {

                            //We store these results in our results array
                            resultsList.add(f.calculate(i));

                            //iMax will now be the highest 'x' value the function can reach in our results array
                            iMax = i;
                        }
                    }

                    //--------------------------------------------------------------------------------------------------

                    //We now try to make the number of points proportional given the boundaries above. With the right
                    //function, the highest figure of points we can have without changing anything would be 41 (from -20
                    //to 20, going by 0.
                    if(resultsList.size() < 41){

                        //iMax will allow us to set artificially our boundaries directly to the function calculation
                        //so we are sure we will only have values between our 'y' boundaries. To make the figure of
                        //points as the other functions, we divide the highest value of our function before reaching
                        //the boundary by the number of points we actually want to have, which is 41.
                        for(float k = -iMax; k <= iMax; k += (resultsList.get(resultsList.size()-1)/41)){

                            //A bit of dark magic there, concatenating a String so we have a proper Expression
                            //constructor call with our right declaration (being 'f(k)'). Thought I could reach the
                            //same result by calling f.calculate(k) but got faced by a wild error I still don't know
                            //how to fix.
                            Expression normalizePointsNumber = new Expression(f.getFunctionName() + "(" + Float.toString(k) + ")", f);

                            //We don't want in our array (even less do we in our graph) values that actually don't
                            //exist. So we just don't add these said values in our array.
                            if(!Double.toString(normalizePointsNumber.calculate()).equals("NaN")) {

                                //We add our newly created points in another array, fearing the constant update
                                //would make our program feeling a bit buggy during execution. Not optimum but working.
                                normalizedPoints.add(normalizePointsNumber.calculate());

                                //Adding our newly created points directly to our LineChart series. We were forced
                                //to create the normalizedPoints array as the 'Expression.calculate()' method
                                //cannot be cast to String, and we needed a String to be cast to Double.
                                //We are using the 'j' variable here to navigate in our array to give the series the
                                //right values.
                                drawFunction.getData().add(new XYChart.Data<Double, Double>(Double.parseDouble(Float.toString(k)), Double.parseDouble(normalizedPoints.get(j).toString())));
                                j++;
                            }
                        }

                        //We clear our array so we won't duplicate our function when adding new functions.
                        normalizedPoints.removeAll(normalizedPoints);
                    }

                    //If we already have the perfect figure of points in our array, we just add them directly to our
                    //LineChart series.
                    else{
                        for(int i = -20; i <= 20; i++) {
                            drawFunction.getData().add(new XYChart.Data<Double, Double>(Double.parseDouble(Integer.toString(i)), Double.parseDouble(resultsList.get(j).toString())));
                            j++;
                        }
                    }

                    //Clearing the results array so we won't create duplicates when adding new functions.
                    resultsList.removeAll(resultsList);
                    graphDisplayer.getData().add(drawFunction);

                }

                //------------------------------------------------------------------------------------------------------


                //------------------------------------------------------------------------------------------------------
                //-----------------------------------------------------------
                //       Getting down to the real business, part 2 :        |
                // calculate a function given an explicit integer argument  |
                //-----------------------------------------------------------

                //If the String does not contain an 'x', we assume the user wants to calculate a function by giving
                //an explicit integer argument.
                else{

                    //This boolean would be updated if the given function name is found in our database.
                    boolean isFound = false;

                    //Just as before, we will take a look at the expression's tokens to get the information we want
                    //in a "clean" way.
                    Expression e = new Expression(equation);
                    ArrayList<Token> tokens = new ArrayList<Token>(e.getCopyOfInitialTokens());

                    //We are going to search in our expressions array the function which has the same name as our
                    //very first token, assuming the user will use the first character as the function caller
                    //(like we do in maths).
                    for(Function function : expressionsList){
                        if(tokens.get(0).tokenStr.equals(function.getFunctionName())){

                            //Great, our the function caller is found in our expressions database. We now create a new
                            //expression, giving to the constructor the string given by the user and the name of the
                            //matching function in our database as arguments.
                            Expression found = new Expression(equation, function);

                            //The explicit integer being given by the user, we just calculate our expression and we
                            //append the result in our TextArea. We decided not to create a new box which would just be
                            //used to display the result, the calculation not being the main function of our program.
                            //Besides, we found it pleasantly aesthetic to display our result this way, even more by
                            //pressing the "ENTER" key.
                            equationDisplayer.appendText(" = " + found.calculate());

                            //We update the value of our boolean, the function being found in our database.
                            isFound = true;
                        }
                    }

                    //If we don't find the function caller in our database, we just clear the TextArea and ask the user
                    //to define his function before trying to call it.
                    if(!isFound){
                        equationDisplayer.clear();
                        equationDisplayer.appendText("Function not found. Try defining it before.");
                    }
                }

                //------------------------------------------------------------------------------------------------------

            }
        });
    }
}


//----------------------------------------------------------------------------------------------------------------------
//                                                  Created by :
//----------------------------------------------------------------------------------------------------------------------
//----------------------------------|Louis Willems|---------------|Benjamin Maerten|------------------------------------
//----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------|2020|-----------------------------------------------------------