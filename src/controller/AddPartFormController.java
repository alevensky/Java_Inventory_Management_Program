package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;


public class AddPartFormController implements Initializable {

    //Form Data
    public Label radioChangeLabel;
    public RadioButton outsourcedRadio;
    public RadioButton inHouseRadio;
    public TextField name;
    public TextField inv;
    public TextField price;
    public TextField max;
    public TextField min;
    public TextField radioChangeBox;
    public StackPane errorPane;

    //Data to load
    int newID;
    String newName;
    int newInv;
    double newPrice;
    int newMax;
    int newMin;
    int newMachineId;
    String newCompanyName;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        inHouseRadio.setSelected(true);
    }


    public int createUniqueId (int q) {
        int uniquePartId = 1;
        List<Integer> partIds = new ArrayList();
        ObservableList<Part> parts = Inventory.getAllParts();
        if (q == 0) {
            uniquePartId = 1;
            for (Part p : parts) {
                if (p instanceof InHouse) {
                    partIds.add(p.getId());
                }
            }
        }
        else if (q == 1) {
            uniquePartId = 0;
            for (Part p : parts) {
                if (p instanceof Outsourced) {
                    partIds.add(p.getId());
                }
            }
        }
        for (int i : partIds) {
            if (i <= uniquePartId) {
                uniquePartId += 2;
            }
        }
        return uniquePartId;
    }

    public void onInHouse(ActionEvent actionEvent) {
        radioChangeLabel.setText("Machine ID");
    }

    public void onOutsourced(ActionEvent actionEvent) {
        radioChangeLabel.setText("Company Name");
    }

    public void onSave(ActionEvent actionEvent) throws IOException {
        errorPane.getChildren().clear();
        List<String> errorList = new ArrayList<String>();
        boolean save = true;
        if (name.getText() != "") {
            newName = name.getText();
        }
        else {
            errorList.add("Please enter string data for the Name field.\n");
            save = false;
        }
        try {
            newInv = Integer.parseInt(inv.getText());
        } catch (Exception e) {
            errorList.add("Please enter integer data for the Inv field.\n");
            save = false;
        }
        try {
            newPrice = Double.parseDouble(price.getText());
        } catch (Exception e) {
            errorList.add("Please enter double data for the Price field.\n");
            save = false;
        }
        try {
            newMax = Integer.parseInt(max.getText());
        } catch (Exception e) {
            errorList.add("Please enter integer data for the Max field.\n");
            save = false;
        }
        try {
            newMin = Integer.parseInt(min.getText());
        } catch (Exception e) {
            errorList.add("Please enter integer data for the Min field.\n");
            save = false;
        }
        if (inHouseRadio.isSelected()) {
            try {
                newMachineId = Integer.parseInt(radioChangeBox.getText());
            } catch (Exception e) {
                errorList.add("Please enter integer data for the Machine ID field.\n");
                save = false;
            }
        }
        else if (outsourcedRadio.isSelected()){
            if (radioChangeBox.getText() != "") {
                newCompanyName = radioChangeBox.getText();
            } else {
                errorList.add("Please enter string data for the Company Name field.\n");
                save = false;
            }
        }
        if (newMin >= newMax) {
            save = false;
            errorList.add("Min must be less than Max.\n");
        }
        if (newInv >= newMax || newInv <= newMin) {
            save = false;
            errorList.add("The Inv value must be between Min and Max.\n");
        }
        if (save) {
            if (inHouseRadio.isSelected()) {
                newID = createUniqueId(0);
                InHouse inh = new InHouse(newID, newName, newPrice, newInv, newMin, newMax, newMachineId);
                Inventory.addPart(inh);
            }
            else if (outsourcedRadio.isSelected()){
                newID = createUniqueId(1);
                Outsourced ous = new Outsourced(newID, newName, newPrice, newInv, newMin, newMax, newCompanyName);
                Inventory.addPart(ous);
            }
            Parent root = FXMLLoader.load(getClass().getResource("/view/OpeningScreen.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            String concatenateErrors = "";
            for (String s : errorList) {
                if (s != null) {
                    concatenateErrors += s;
                }
            }
            errorPane.getChildren().add(new Label(concatenateErrors));
        }
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/OpeningScreen.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
