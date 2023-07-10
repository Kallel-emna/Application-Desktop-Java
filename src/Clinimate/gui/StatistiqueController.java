/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Clinimate.gui;

import Clinimate.utils.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author medam
 */
public class StatistiqueController implements Initializable {

    Connection cnx = DataSource.getInstance().getCnx();
    @FXML
    private AnchorPane mainform;
    @FXML
    private BarChart<?, ?> barchart;
    private Object XYChart;
    @FXML
    private Button btnretour;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chart();
    }

    public void chart() {
    String selectData = "SELECT date_rendezvous, COUNT(*) AS nb_rendezvous FROM Rendez_vous GROUP BY date_rendezvous";
    try {
        XYChart.Series chartData = new XYChart.Series();
        PreparedStatement pst = cnx.prepareStatement(selectData);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            XYChart.Data data = new XYChart.Data(rs.getString(1), rs.getInt(2));
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                    if (newValue != null) {
                        newValue.setStyle("-fx-bar-fill:   #2C5364 ");
                        // Modifier la couleur du nombre de rendez-vous en navy
                    }
                }
            });
            chartData.getData().add(data);
        }
        barchart.getData().add(chartData);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
    private void retour(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Rendez_vous.fxml"));
        Parent root = loader.load();
        btnretour.getScene().setRoot(root);
    }

}
