package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mariuszgromada.math.mxparser.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("L'autre calculatrice");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();

    }


    public static void main(String[] args) {
        System.out.println("Coucou c'st DAvide Lafarge");
        launch(args);
        Expression eh = new Expression("5^2 * 7^3 * 11^1 * 67^1 * 49201^1");
        Expression ew = new Expression("71^1 * 218549^1 * 6195547^1");
        String h = mXparser.numberToAsciiString( eh.calculate() );
        String w = mXparser.numberToAsciiString( ew.calculate() );
        mXparser.consolePrintln(h + " " + w);
    }
}
