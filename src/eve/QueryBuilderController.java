package eve;

import eve.functions.Regions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.awt.event.KeyEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by user on 15.05.14.
 */
public class QueryBuilderController {
    //------------ Trading
    @FXML private TextField minprofit;
    @FXML private TextField minbuy;
    @FXML private TextField age;
    @FXML private TextField minmargin;
    @FXML private ComboBox region_s;
    @FXML private ComboBox region_b;
    @FXML private CheckBox regiontrade_cb;
    @FXML private ComboBox tobuysell_cb;
    @FXML private ComboBox frombuysell_cb;


    @FXML private TextArea output;
    //-------------- settings
    @FXML private ComboBox accounting;
    @FXML private ComboBox brokerrelation;
    @FXML private TextField buycorpstanding;
    @FXML private TextField buyfactionstanding;
    @FXML private Label brokerfeeLabel;
    @FXML private Label salestaxLabel;
    @FXML private CheckBox directTrade;




    double brokerfee = 0;
    double salestax= 0;


    Regions regions = new Regions();
    Sql2TableController sttc;

    private ObservableList regionlist = FXCollections.observableArrayList();
    private ObservableList zero2five = FXCollections.observableArrayList();
    private ObservableList sellbuy_list = FXCollections.observableArrayList();
    private String currentQuery= "";
    public QueryBuilderController() {
    }
    public QueryBuilderController(Sql2TableController table) {
        sttc = table;

    }


    public void initialize () {
        region_s.setItems(regionlist);
        region_b.setItems(regionlist);

        sellbuy_list.add("sellprice");
        sellbuy_list.add("buyprice");
        tobuysell_cb.setItems(sellbuy_list);
        frombuysell_cb.setItems(sellbuy_list);

        setRegions(regions.getRegionNames());

        addZero2Five();
        accounting.setItems(zero2five);
        accounting.getSelectionModel().select("5");
        brokerrelation.setItems(zero2five);
        brokerrelation.getSelectionModel().select("5");
        calcTaxs();
        enableRegionTrading(false);

        buildDatacollection(new ActionEvent());



        //buildDatacollection(new ActionEvent());
    }
    public void setRegions (ObservableList regions) {
        regionlist.addAll(regions);
        region_s.getSelectionModel().select(0);
        region_b.getSelectionModel().select(1);
    }
    public void buildDatacollection (ActionEvent actionEvent) {

        if (frombuysell_cb.getValue() == null) {
            frombuysell_cb.getSelectionModel().select(0);
            tobuysell_cb.getSelectionModel().select(1);
        }
        //System.out.println(frombuysell_cb.getValue());


        String selectedRegionBuy= region_s.getSelectionModel().getSelectedItem().toString();
        String selectedRegionSell = "Metropolis";
        if (region_b.getSelectionModel().getSelectedItem()!= null) {
            selectedRegionSell= region_b.getSelectionModel().getSelectedItem().toString();
        }
        System.out.println(selectedRegionSell);
        output.setText("DataCollection built: "+selectedRegionBuy+"--> "+selectedRegionBuy);
        if (sttc != null) {
            if (regiontrade_cb.isSelected()) {
                enableRegionTrading(true);
                if (directTrade.isSelected()) {
                    sttc.updateView(
                            regions.IDbyName(selectedRegionBuy),
                            frombuysell_cb.getSelectionModel().getSelectedItem().toString(),
                            regions.IDbyName(selectedRegionSell),
                            tobuysell_cb.getSelectionModel().getSelectedItem().toString(),
                            0, 0);
                } else {
                    sttc.updateView(
                            regions.IDbyName(selectedRegionBuy),
                            frombuysell_cb.getSelectionModel().getSelectedItem().toString(),
                            regions.IDbyName(selectedRegionSell),
                            tobuysell_cb.getSelectionModel().getSelectedItem().toString(),
                            brokerfee, salestax);
                }
            } else if (!actionEvent.getSource().equals(region_b)) {
                System.out.println("build view for "+selectedRegionBuy);
                enableRegionTrading(false);
                if (directTrade.isSelected()) {
                    sttc.updateView(
                            regions.IDbyName(selectedRegionBuy),
                            "buyprice",
                            regions.IDbyName(selectedRegionBuy),
                            "sellprice",
                            0, 0);
                } else {
                    sttc.updateView(
                            regions.IDbyName(selectedRegionBuy),
                            "buyprice",
                            regions.IDbyName(selectedRegionBuy),
                            "sellprice",
                            brokerfee, salestax);
                }

            }

        }   else {
            output.setText("No Table configured !");
        }

    }
    public void showFromRegion (ActionEvent actionEvent) {
        String selectedRegion= region_s.getSelectionModel().getSelectedItem().toString();
        currentQuery = "SELECT * FROM owndb."+regions.IDbyName(selectedRegion)+"emdr";
        sttc.showQuery(currentQuery);
    }
    public void showFromDataselection (ActionEvent actionEvent){
        String selectedRegion= region_s.getSelectionModel().getSelectedItem().toString();
        currentQuery = "SELECT * FROM datacollection";
        sttc.showQuery(currentQuery);
    }
    public void setSql2Table(Sql2TableController table) {
        sttc=table;
    }
    private void addZero2Five() {

        zero2five.add(0);
        zero2five.add(1);
        zero2five.add(2);
        zero2five.add(3);
        zero2five.add(4);
        zero2five.add(5);

    }
    private void calcTaxs() {
        //---Get Values

        try {
            int brokerrelationskill = Integer.parseInt(brokerrelation.getSelectionModel().getSelectedItem().toString());
            int accountingskill = Integer.parseInt(accounting.getSelectionModel().getSelectedItem().toString());
            double factionstanding = Double.parseDouble(buyfactionstanding.getText());
            double corporationstanding = Double.parseDouble(buycorpstanding.getText());

            //---Broker Fees
            double below = Math.pow(2,(0.14*factionstanding+0.06*corporationstanding) );
            brokerfee = (1-0.05*brokerrelationskill)/below;
            brokerfeeLabel.setText(double2String(brokerfee));

            //--- Sales Tax
            salestax= 1.5 - (0.15 * accountingskill);
            salestaxLabel.setText(double2String(salestax));
        } catch (NullPointerException npe) {
            System.out.println("No entries in at least 1 tax field");
        }
    }
    private String double2String (Double value) {
        int digits = 2;
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(digits);
        df.setMaximumFractionDigits(digits);
        df.setRoundingMode(RoundingMode.DOWN);

        return (df.format(value));
    }
    public void calcTax (ActionEvent actionEvent) {
        calcTaxs();
    }
    public void setCurrentQuery () {
        String selectedRegion= region_s.getSelectionModel().getSelectedItem().toString();
        double tax = (brokerfee+salestax);

        //String profit = " (sellprice - buyprice*tax)";
        //*
        currentQuery =
                "SELECT * " +
                "FROM datacollection " +
                "WHERE profit > " + minprofit.getText() +
                " AND buyprice > " + minbuy.getText() +
                " AND name NOT LIKE '%blueprint%' " +
                " AND name NOT LIKE '%II%'" +
                " AND TIMEDIFF (now(),lastUpdate) < TIME('"+age.getText()+"')" +
                " AND margin > "+ minmargin.getText();
        //+" AND volume > "+sg.getMinVolumeField().getText()
        output.setText(currentQuery);
        System.out.println(currentQuery);
        if (sttc != null) {
            sttc.showQuery(currentQuery);
        }   else {
            output.setText("No Table configured !");
        }

        //sttc.showQuery(currentQuery);
        //*/
    }
    public void clearData (ActionEvent actionEvent) {
        sttc.clearData();
    }
    public void sendQuery (ActionEvent actionEvent) {
        //setCurrentQuery();
        if(actionEvent.getEventType().equals(KeyEvent.VK_ENTER)) {
            System.out.println("EnterPresed");
            //setCurrentQuery(actionEvent);
        }
    }
    private void enableRegionTrading(Boolean visible) {
        frombuysell_cb.setDisable(!visible);
        tobuysell_cb.setDisable(!visible);
        region_b.setDisable(!visible);
    }
}
