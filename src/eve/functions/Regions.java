package eve.functions;

import eve.DBClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by user on 15.05.14.
 */
public class Regions {

    private ObservableList regionIDs= FXCollections.observableArrayList();
    private ObservableList regionNames= FXCollections.observableArrayList();


    public Regions() {
        getRegionsFromDb();
    }
    private void getRegionsFromDb() {
        DBClass db = new DBClass();


        try {
            Connection sql = db.getConnection();
            //Get IDs from ownDB, which are populated and created by EMDR.jar
            String SQL = "SHOW TABLES IN owndb LIKE '%emdr'";
            ResultSet rs = sql.createStatement().executeQuery(SQL);
            while (rs.next()) regionIDs.add(rs.getString(1).split("e")[0]);

            //Get Names from own DB
            for (Object id : regionIDs) {
                SQL = "SELECT regionName from eve_phoebe.mapRegions WHERE regionID="+id.toString();

                rs = sql.createStatement().executeQuery(SQL);
                while (rs.next()) regionNames.add(rs.getString(1));
            }



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public ObservableList getRegionIDs (){
        return regionIDs;
    }
    public ObservableList getRegionNames () {
        return regionNames;
    }
    public String IDbyName (String name) {
        return (String) regionIDs.get(regionNames.indexOf(name));
    }
    public String NamebyID (String id) {
        return (String) regionNames.get(regionIDs.indexOf(id));
    }
}
