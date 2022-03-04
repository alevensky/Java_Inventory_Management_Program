package controller;

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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ModifyPartFormController implements Initializable {

    //Form Data
    public Label radioChangeLabel;
    public RadioButton outsourcedRadio;
    public RadioButton inHouseRadio;
    public TextField id;
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

    private static Part displayedPart = null;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (displayedPart instanceof InHouse) {
            inHouseRadio.setSelected(true);
            radioChangeLabel.setText("Machine ID");
            String firstMachId = null;
            radioChangeBox.setText(firstMachId.valueOf(((InHouse)displayedPart).getMachineId()));
        }
        else if (displayedPart instanceof Outsourced) {
            outsourcedRadio.setSelected(true);
            radioChangeLabel.setText("Company Name");
            radioChangeBox.setText(((Outsourced)displayedPart).getCompanyName());
        }
        else {
            //error handling
        }
        String firstID = null;
        id.setPromptText(firstID.valueOf(displayedPart.getId()));
        name.setText(displayedPart.getName());
        String firstInv = null;
        inv.setText(firstInv.valueOf(displayedPart.getStock()));
        String firstPrice = null;
        price.setText(firstPrice.valueOf(displayedPart.getPrice()));
        String firstMax = null;
        max.setText(firstMax.valueOf(displayedPart.getMax()));
        String firstMin = null;
        min.setText(firstMin.valueOf(displayedPart.getMin()));
    }

    public static void receivePart (Part part) {
        displayedPart = part;
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
        newID = displayedPart.getId();
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
                Part newPart = new InHouse(newID, newName, newPrice, newInv, newMin, newMax, newMachineId);
                Inventory.updatePart(newID, newPart);
            }
            else if (outsourcedRadio.isSelected()) {
                Part newPart = new Outsourced(newID, newName, newPrice, newInv, newMin, newMax, newCompanyName);
                Inventory.updatePart(newID, newPart);
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
