package eve;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;


import java.awt.*;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by user on 10.05.14.
 */
public class Sql2TableController {
    @FXML private TableView generalTable;
    @FXML private TextField searchBox;
    @FXML private Label entrieslabel;



    private ObservableList<ObservableList> data;
    private ObservableList<ObservableList> filtereddata= FXCollections.observableArrayList();
    ResultSet rs;
    final Clipboard clipboard = Clipboard.getSystemClipboard().getSystemClipboard();
    Connection sql;

    public void initialize () {


        //generalTable = new TableView();
        //for testing only
        try {
            sql = new DBClass().getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        generalTable.setItems(filtereddata);
        searchBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                updateFilteredData();
            }
        });

        generalTable.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object o2) {
               ClipboardContent content = new ClipboardContent();
               content.putString(((ObservableList) o2).get(1).toString());
               clipboard.setContent(content);
            }
        });


    }


    //public void setResultSet (ResultSet rs) {
    public void showQuery (String query) {

        this.rs = rs;
        //data = null;
        data = FXCollections.observableArrayList();
        //data.clear();
        data.removeAll(data);
        filtereddata.removeAll(filtereddata);
        //generalTable.getColumns().removeAll(data);
        generalTable.getColumns().clear();


        //*
        String SQL = query;
        ResultSet rs = null;
        try {
            rs = sql.createStatement().executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnscount = rsmd.getColumnCount();

            for (int i = 1; i <=columnscount; i++) {
                //System.out.println("Table: "+rsmd.getColumnName(i));
                TableColumn tableColumn = new TableColumn(rsmd.getColumnName(i));
                final int k = i;



                if (rsmd.getColumnType(k)== Type.DOUBLE) {

                    tableColumn.setCellValueFactory(
                            new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<Double>>() {
                                            public ObservableValue<Double> call(TableColumn.CellDataFeatures<ObservableList, String> cellDataFeatures) {
                                                return new SimpleObjectProperty<Double>(Double.parseDouble(cellDataFeatures.getValue().get(k-1).toString()));
                                            }
                            }
                    );
                    tableColumn.setCellFactory(getCustomCellFactoryDouble("black", 0));

                } else if (rsmd.getColumnType(k)== Type.SHORT) {
                    //System.out.println("INT found: "+rsmd.getColumnName(i));
                    tableColumn.setCellValueFactory(
                            new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<Integer>>() {
                                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ObservableList, String> cellDataFeatures) {
                                    return new SimpleObjectProperty<Integer>(Integer.parseInt(cellDataFeatures.getValue().get(k - 1).toString()));
                                }
                            }
                    );
                    tableColumn.setCellFactory(getCustomCellFactoryInt("red", 0));
                }   else {

                    tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                                            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                                                return new SimpleStringProperty(param.getValue().get(k-1).toString());
                                           }

                            });
                    tableColumn.setCellFactory(getCustomCellFactoryString("black"));


                }

                //generalTable.getColumns().addAll(tableColumn);

                generalTable.getColumns().add(tableColumn);


            }


            while (rs.next()) {
                ObservableList row = FXCollections.observableArrayList();
                for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {

                    if (rsmd.getColumnType(j)== Type.DOUBLE) {
                        NumberFormat df = DecimalFormat.getInstance();
                        df = DecimalFormat.getInstance();
                        df.setMinimumFractionDigits(2);
                        df.setMaximumFractionDigits(2);
                        df.setRoundingMode(RoundingMode.DOWN);
                        row.add(rs.getDouble(j));
                    } else {
                        row.add(rs.getString(j));
                    }
                }
                data.add(row);
            }
            //generalTable.setItems(data);
            filtereddata.setAll(data);
            generalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            resizeTable();
            updateEntriesLabel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //*/
        //generalTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        //generalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private Callback<TableColumn<ObservableList, Double>, TableCell<ObservableList, Double>> getCustomCellFactoryDouble(final String color, final int digits) {
        return new Callback<TableColumn<ObservableList, Double>, TableCell<ObservableList, Double>>() {


            @Override
            public TableCell<ObservableList, Double> call(TableColumn<ObservableList, Double> param) {
                TableCell<ObservableList, Double> cell = new TableCell<ObservableList, Double>() {

                    @Override
                    public void updateItem(final Double item, boolean empty) {

                        if (item != null && !color.equals("")) {
                            //setText(item);
                            setStyle("-fx-text-fill: " + color + ";");
                        }

                        try {
                            if (item != null) {
                                //double d = Double.parseDouble(item);
                                //String old = getItem();//.toString();
                                NumberFormat df = DecimalFormat.getInstance();
                                df.setMinimumFractionDigits(digits);
                                df.setMaximumFractionDigits(digits);
                                df.setRoundingMode(RoundingMode.DOWN);
                                setText("33.11");
                                setText(df.format(item));
                                //setStyle("-fx-text-fill: " + color + ";");
                                setStyle("-fx-alignment: CENTER-RIGHT;");
                            }
                        } catch (NumberFormatException nfe) {

                        }





                    }
                };
                return cell;
            }
        };
    }
    private Callback<TableColumn<ObservableList, Integer>, TableCell<ObservableList, Integer>> getCustomCellFactoryInt(final String color, final int digits) {
        return new Callback<TableColumn<ObservableList, Integer>, TableCell<ObservableList, Integer>>() {


            @Override
            public TableCell<ObservableList, Integer> call(TableColumn<ObservableList, Integer> param) {
                TableCell<ObservableList, Integer> cell = new TableCell<ObservableList, Integer>() {

                    @Override
                    public void updateItem(final Integer item, boolean empty) {

                        if (item != null && !color.equals("")) {
                            //setText(item);
                            setStyle("-fx-text-fill: " + color + ";");
                        }

                        try {
                            if (item != null) {
                                //double d = Double.parseDouble(item);
                                //String old = getItem();//.toString();
                                NumberFormat df = DecimalFormat.getInstance();
                                df.setMinimumFractionDigits(digits);
                                df.setMaximumFractionDigits(digits);
                                df.setRoundingMode(RoundingMode.DOWN);
                                setText("33.11");
                                setText(df.format(item));
                                //setStyle("-fx-text-fill: " + color + ";");
                                setStyle("-fx-alignment: CENTER-RIGHT;");
                            }
                        } catch (NumberFormatException nfe) {

                        }





                    }
                };
                return cell;
            }
        };
    }
    private Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>> getCustomCellFactoryString(final String color) {
        return new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {


            @Override
            public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                    @Override
                    public void updateItem(final String item, boolean empty) {

                        if (item != null && !color.equals("")) {
                            setText(item);
                            setStyle("-fx-text-fill: " + color + ";");
                        }

                        try {

                            if (item != null) {
                                //wenn wert eine zahl
                                double d = Double.parseDouble(item);
                                setText(item);
                                setStyle("-fx-text-fill: " + color + ";");
                                setStyle("-fx-alignment: CENTER-RIGHT;");
                            }
                        } catch (NumberFormatException nfe) {

                        }






                    }
                };
                return cell;
            }
        };
    }
    private void updateFilteredData () {
        filtereddata.clear();

        for (ObservableList row : data) {
            if (matchesFilter(row)) {
                filtereddata.add(row);
            }
        }
        updateEntriesLabel();
    }
    private boolean matchesFilter(ObservableList row) {
        String searchString = searchBox.getText();
        //wenn nichts, dann adde alles
        if (searchString == null || searchString.isEmpty()) return true;
        String lowercaseSearchString = searchString.toLowerCase();

        if (row.get(1).toString().toLowerCase().indexOf(lowercaseSearchString) != -1) { // wenn vorhanden dann adde
            return true;
        } else if (row.get(0).toString().toLowerCase().indexOf(lowercaseSearchString) != -1) {
            return true;
        }
        return false;

    }
    public void updateView (String regionbuy, String buybuysell, String regionsell, String sellbuysell, double brokerfees, double salestax) {
        double tax = 1+(brokerfees+salestax)/100;
        try {
            if (regionbuy.equals(regionsell)) {

                sql.createStatement().executeUpdate("DROP VIEW IF EXISTS datacollection");
                String query = "CREATE VIEW datacollection AS " +
                        "SELECT * , " +
                        "(sellprice-buyprice*"+tax+") AS profit, " +
                        "(sellprice-buyprice*"+tax+")*100/buyprice AS margin " +
                        "FROM " +regionbuy+"emdr "
                        //"INNER JOIN averageVolume\n" +
                        //"ON "+getTableByName(sg.getRegionSelection())+".id = averageVolume.typeID;"
                        ;
                System.out.println(query);
                sql.createStatement().executeUpdate(query);
            } else {

                sql.createStatement().executeUpdate("DROP VIEW IF EXISTS datacollection");
                String query = "CREATE VIEW datacollection AS " +
                        "SELECT " +
                        regionbuy+"emdr.id , " +
                        regionbuy+"emdr.name, " +
                        //regionbuy+"emdr.name, " +
                        regionbuy+"emdr."+buybuysell+" as buyprice, " +
                        regionsell+"emdr."+sellbuysell+" as sellprice, " +
                        //regionbuy+"emdr.lastUpdate as updatebuy, " +
                        //regionsell+"emdr.lastUpdate as updatesell, " +
                        regionsell+"emdr.lastUpdate as lastUpdate, " +
                        regionbuy+"emdr.marketgroupid, " +
                        "("+regionsell+"emdr."+buybuysell+"-"+regionbuy+"emdr."+buybuysell+"*"+tax+") AS profit, " +
                        "("+regionsell+"emdr."+sellbuysell+"-"+regionbuy+"emdr."+buybuysell+"*"+tax+")*100/"+regionbuy+"emdr."+buybuysell+" AS margin " +
                        "FROM " +regionbuy+"emdr " +
                        "INNER JOIN "+regionsell+"emdr " +
                        "ON "+regionbuy+"emdr.id = "+regionsell+"emdr.id "
                        ;
                System.out.println(query);
                sql.createStatement().executeUpdate(query);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void clearData () {
        data.removeAll(data);
        filtereddata.removeAll(filtereddata);
    }
    private void resizeTable () {
        int size = generalTable.getColumns().size();

        for (int i = 0; i < generalTable.getColumns().size(); i++ ){
            TableColumn column = (TableColumn)generalTable.getColumns().get(i);
            if (column.getText().equalsIgnoreCase("id")) {
                column.setPrefWidth(50);
                column.setResizable(false);
            } else if ((column.getText().equalsIgnoreCase("name"))) {
                column.setPrefWidth(250);
                column.setResizable(false);
            } else if ((column.getText().contains("price"))) {
                column.setPrefWidth(100);
                column.setResizable(false);
            }else {
                column.setPrefWidth((generalTable.getWidth()-50)/generalTable.getColumns().size());
            }

            generalTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            generalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
    }
    private void updateEntriesLabel () {
        entrieslabel.setText(filtereddata.size()+" of "+data.size()+ " shown");
    }
}
