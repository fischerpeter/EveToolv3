package eve;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by user on 09.05.14.
 */
public class Order {
    private double tax = 1.0145;

    private SimpleIntegerProperty id = new SimpleIntegerProperty();

    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleDoubleProperty buy = new SimpleDoubleProperty();
    private SimpleDoubleProperty sell = new SimpleDoubleProperty();
    //private SimpleStringProperty profit = new SimpleStringProperty();
    private SimpleDoubleProperty profit = new SimpleDoubleProperty();
    private SimpleDoubleProperty margin = new SimpleDoubleProperty();
    //private double buy_d;
    //private double sell_d;
    //private double profit_d;
    //private double margin_d;





    public Double getSell() {
        return sell.get();
    }

    public SimpleDoubleProperty sellProperty() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell.set(sell);
        //sell_d=Double.parseDouble(sell);
        setProfit ();
    }

    public Double getBuy() {
        return buy.get();
    }

    public SimpleDoubleProperty buyProperty() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy.set(buy);
        //buy_d=Double.parseDouble(buy);
        setProfit ();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }
    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public Double getProfit() {
        return profit.get();
    }

    public SimpleDoubleProperty profitProperty() {
        return profit;
    }

    //public void setProfit(String profit) {
    public void setProfit(Double profit) {
        //this.profit.set(profit);
        //profit_d=Double.parseDouble(profit);
        this.profit.set(profit);
        //profit_d=profit;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
    public Double getMargin() {
        return margin.get();
    }

    public SimpleDoubleProperty marginProperty() {
        return margin;
    }
    private void setProfit () {
        //profit_d=(sell_d-buy_d)*tax;
        //profit.set(String.valueOf(profit_d));
        profit.set((sell.get()-buy.get())/tax);
        setMargin();
    }
    private void setMargin() {
        margin.set((profit.get()*100)/buy.get());
       //margin.set(String.valueOf(margin_d));

    }
    /*
    public double getProfit_d () {
        return profit_d;
    }
    public double getMargin_d () {
        return margin_d;
    }
    */



}
