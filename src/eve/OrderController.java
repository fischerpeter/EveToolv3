package eve;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import sample.Main;
import javafx.beans.value.*;
import sample.Person;
import sun.plugin2.jvm.RemoteJVMLauncher;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.geom.Arc2D;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * Created by user on 09.05.14.
 */
public class OrderController {
    @FXML private TableView<Order> tableView;
    @FXML private TableColumn<Order, Integer> idCol;
    @FXML private TableColumn<Order, String> nameCol;
    @FXML private TableColumn<Order, Double> sellCol;
    @FXML private TableColumn<Order, Double> buyCol;
    @FXML private TableColumn<Order, Double> profitCol;
    @FXML private TableColumn<Order, Double> marginCol;
    private ObservableList<Order> data;


    Main mainApp;

    Connection sql;

    public void initialize () {

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("id"));
        idCol.setText("ID");
        nameCol.setCellValueFactory(new PropertyValueFactory<Order, String>("name"));
        nameCol.setText("Name");
        nameCol.setCellFactory(getCustomCellFactoryString("black", 0));
        sellCol.setCellValueFactory(new PropertyValueFactory<Order, Double>("sell"));
        sellCol.setText("Sell");
        sellCol.setCellFactory(getCustomCellFactoryDouble("black", 0));
        buyCol.setCellValueFactory(new PropertyValueFactory<Order, Double>("buy"));
        buyCol.setText("Buy");
        buyCol.setCellFactory(getCustomCellFactoryDouble("black", 0));
        profitCol.setCellValueFactory(new PropertyValueFactory<Order, Double>("profit"));
        profitCol.setText("Profit");
        profitCol.setCellFactory(getCustomCellFactoryDouble("black", 0));
        marginCol.setCellValueFactory(new PropertyValueFactory<Order, Double>("margin"));
        marginCol.setText("Margin");
        marginCol.setCellFactory(getCustomCellFactoryDouble("black", 2));



        try {
            sql = new DBClass().getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
            @Override
            public void changed(ObservableValue<? extends Order> observableValue, Order oldOrder, Order newOrder) {

                if (newOrder!= null)copyToClipboard(newOrder);
            }
        });

        };


    public void buildData(){
        data = FXCollections.observableArrayList();

        try{
            String SQL = "Select * from owndb.10000043emdr where name not like '%blueprint%'";
            ResultSet rs = sql.createStatement().executeQuery(SQL);
            while(rs.next()){
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setName(rs.getString("name"));
                //order.setBuy(rs.getDouble("buyprice"));
                order.setBuy(rs.getDouble("buyprice"));
                order.setSell(rs.getDouble("sellprice"));


                if (order.getProfit()>0 && order.getMargin() != Double.POSITIVE_INFINITY)data.add(order);


            }
            tableView.setItems(data);

        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //tableView.setItems(mainApp.getData());
    }

    private void copyToClipboard(Order order) {
        String name = order.getName();
        StringSelection nameselection = new StringSelection(name);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(nameselection, null);
    }


    private Callback<TableColumn<Order, String>, TableCell<Order, String>> getCustomCellFactoryString(final String color, final int digits) {
        return new Callback<TableColumn<Order, String>, TableCell<Order, String>>() {


            @Override
            public TableCell<Order, String> call(TableColumn<Order, String> param) {
                TableCell<Order, String> cell = new TableCell<Order, String>() {

                    @Override
                    public void updateItem(final String item, boolean empty) {

                        if (item != null && !color.equals("")) {
                            setText(item);
                            setStyle("-fx-text-fill: " + color + ";");
                        }

                        try {
                            if (item != null) {
                                double d = Double.parseDouble(item);
                                //String old = getItem();//.toString();
                                NumberFormat df = DecimalFormat.getInstance();
                                df.setMinimumFractionDigits(digits);
                                df.setMaximumFractionDigits(digits);
                                df.setRoundingMode(RoundingMode.DOWN);

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
    private Callback<TableColumn<Order, Double>, TableCell<Order, Double>> getCustomCellFactoryDouble(final String color, final int digits) {
        return new Callback<TableColumn<Order, Double>, TableCell<Order, Double>>() {


            @Override
            public TableCell<Order, Double> call(TableColumn<Order, Double> param) {
                TableCell<Order, Double> cell = new TableCell<Order, Double>() {

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


}
