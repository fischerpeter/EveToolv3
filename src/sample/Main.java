package sample;

import eve.OrderController;
import eve.QueryBuilderController;
import eve.Sql2TableController;
import eve.functions.Regions;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Person> data = FXCollections.observableArrayList();

    public Main () {
        data.add(new Person("a","b"));
        data.add(new Person("c","d"));
        data.add(new Person("e","f"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Test App");







        FXMLLoader loader = new FXMLLoader(Main.class.getResource("rootlayout.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("rootlayout.fxml"));
        rootLayout = (BorderPane) loader.load();
        Scene scene=new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        showPersonOverview();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void showPersonOverview() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("OrdersTable.fxml"));
            AnchorPane overviewPage = (AnchorPane) loader.load();
            //rootLayout.setCenter(overviewPage);

            FXMLLoader loader_table = new FXMLLoader(Main.class.getResource("../eve/Sql2Table.fxml"));
            AnchorPane tablePage = (AnchorPane) loader_table.load();
            rootLayout.setCenter(tablePage);

            FXMLLoader loader_query = new FXMLLoader((Main.class.getResource("../eve/QueryBuilder.fxml")));
            //loader_query = new FXMLLoader(Main.class.getResource("OrdersTable.fxml"));
            //BorderPane queryPane = (BorderPane) loader_query.load();
            AnchorPane queryPane = (AnchorPane) loader_query.load();
            rootLayout.setRight(queryPane);

            FXMLLoader loader_history = new FXMLLoader(getClass().getResource("../eve/History.fxml"));
            AnchorPane historyPane = (AnchorPane) loader_history.load();
            //rootLayout.setBottom(historyPane);

            //
            //QueryBuilderController qbc = loader_query.getController();
            //qbc.setRegions(regions.getRegionNames());

            //PersonController controller = loader.getController();
            OrderController controller = loader.getController();
            controller.buildData();
            Sql2TableController sql2table = loader_table.getController();

            QueryBuilderController query = loader_query.getController();
            query.setSql2Table(sql2table);

            Sql2TableController stc = loader_table.getController();
            //stc.showQuery("Select * from owndb.10000043emdr");
            //stc.showQuery("Select * from owndb.datacollection");
            //stc.showQuery("SELECT 10000043emdr.id, 10000043emdr.name, 10000043emdr.sellprice, 10000002emdr.sellprice, 10000002emdr.sellprice-10000043emdr.sellprice AS profit, 10000002emdr.lastUpdate FROM 10000043emdr INNER JOIN 10000002emdr ON 10000002emdr.id=10000043emdr.id WHERE 10000043emdr.sellprice>0 AND 10000002emdr.sellprice>0 AND 10000002emdr.sellprice-10000043emdr.sellprice>5000000 AND TIMEDIFF(now(),10000002emdr.lastUpdate)<TIME('03:00:00') AND TIMEDIFF(now(),10000043emdr.lastUpdate)<TIME('03:00:00')");
            //stc.showQuery("Select * from owndb.10000002emdr where id < 40");
            //stc.showQuery("SELECT 10000041emdr.id, 10000041emdr.name, 10000041emdr.sellprice, 10000043emdr.sellprice, 10000043emdr.sellprice-10000041emdr.sellprice AS profit, 10000043emdr.lastUpdate FROM 10000041emdr INNER JOIN 10000043emdr ON 10000043emdr.id=10000041emdr.id WHERE 10000041emdr.sellprice>0 AND 10000043emdr.sellprice>0 AND 10000043emdr.sellprice-10000041emdr.sellprice>5000000 AND TIMEDIFF(now(),10000043emdr.lastUpdate)<TIME('0300:00:00') AND TIMEDIFF(now(),10000041emdr.lastUpdate)<TIME('0300:00:00')");

            //controller.setMainApp(this);



        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    public ObservableList<Person> getData () {
        return data;
    }
}
