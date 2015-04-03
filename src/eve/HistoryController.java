package eve;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by user on 17.05.14.
 */
public class HistoryController {
    @FXML  LineChart<String, Number> linechart;


    XYChart.Series series;
    Connection sql;
    ResultSet rs;
    public HistoryController() {

    }
    public void initialize () {
        try {
            sql = new DBClass().getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        linechart = new LineChart<String, Number>(xAxis, yAxis);
        linechart.setTitle("History for ID: ");
        linechart.getXAxis().setAutoRanging(true);
        linechart.getYAxis().setAutoRanging(true);
        series = new XYChart.Series();
        series.setName("historydata");

        addValuesToSeries();
    }
    public void setGraph(int id, ObservableList regions, ObservableList type) {



        linechart.getData().add(series);
    }

    private void addValuesToSeries() {
        try {
            //rs = sql.createStatement().executeQuery("SHOW TABLES IN owndb");
            rs = sql.createStatement().executeQuery("SELECT date, high FROM owndb.10000002emdr_h WHERE id=34");
            while(rs.next()) {
                String date = rs.getString(1);
                //System.out.println(rs.getString(2));
                Double price = rs.getDouble(2);
        //        series.getData().add(new XYChart.Data(date, price) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        series.getData().add(new XYChart.Data<String, Number>("Jan", 23));
        series.getData().add(new XYChart.Data<String, Number>("Feb", 14));
        series.getData().add(new XYChart.Data<String, Number>("Mar", 15));
        series.getData().add(new XYChart.Data<String, Number>("Apr", 24));
        series.getData().add(new XYChart.Data<String, Number>("May", 34));
        series.getData().add(new XYChart.Data<String, Number>("Jun", 36));
        series.getData().add(new XYChart.Data<String, Number>("Jul", 22));
        series.getData().add(new XYChart.Data("Aug", 45));
        series.getData().add(new XYChart.Data("Sep", 43));
        series.getData().add(new XYChart.Data("Oct", 17));
        series.getData().add(new XYChart.Data("Nov", 29));
        series.getData().add(new XYChart.Data("Dec", 25));
        // soll zu setGraph
        linechart.getData().addAll(series);

    }

}
