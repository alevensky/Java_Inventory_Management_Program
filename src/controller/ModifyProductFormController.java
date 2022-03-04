package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProductFormController implements Initializable {

    //Parts Table
    public TextField search;
    public TableView partsTable;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;

    //Built-Products Table
    public TableView productsTable;
    public TableColumn prodIDCol;
    public TableColumn prodNameCol;
    public TableColumn prodInvCol;
    public TableColumn prodPriceCol;
    public ObservableList<Part> products = FXCollections.observableArrayList();
    public StackPane errorPane;

    //User Inputs
    public TextField id;
    public TextField name;
    public TextField inv;
    public TextField price;
    public TextField max;
    public TextField min;

    //Data to load
    String newName;
    int newInv;
    double newPrice;
    int newMax;
    int newMin;

    private static Product displayedProduct = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(Inventory.getAllParts());
        for (Part p : displayedProduct.getAllAssociatedParts()) {
            products.add(p);
        }
        productsTable.setItems(products);

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        String firstId = null;
        id.setPromptText(firstId.valueOf(displayedProduct.getId()));
        name.setText(displayedProduct.getName());
        String firstInv = null;
        inv.setText(firstInv.valueOf(displayedProduct.getStock()));
        String firstPrice = null;
        price.setText(firstPrice.valueOf(displayedProduct.getPrice()));
        String firstMax = null;
        max.setText(firstMax.valueOf(displayedProduct.getMax()));
        String firstMin = null;
        min.setText(firstMin.valueOf(displayedProduct.getMin()));
    }

    public static void receiveProduct (Product product) { displayedProduct = product; }

    public void onAdd(ActionEvent actionEvent) {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        products.add(selectedPart);
    }

    public void onRemove(ActionEvent actionEvent) {
        boolean deleted = false;
        Part selectedPart = (Part) productsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Removal");
        alert.setHeaderText("");
        alert.setContentText("Are you sure you want to remove this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            products.remove(selectedPart);
            deleted = true;
        }
        else if (result.get() == ButtonType.CANCEL) {
            Alert abortedAlert = new Alert(Alert.AlertType.WARNING);
            abortedAlert.setTitle("Operation Aborted");
            abortedAlert.setHeaderText("");
            abortedAlert.setContentText("The part was not removed.");
            abortedAlert.showAndWait();
        }
        else {
            Alert failAlert = new Alert(Alert.AlertType.WARNING);
            failAlert.setTitle("Operation Failure");
            failAlert.setHeaderText("");
            failAlert.setContentText("Operation could not be completed!");
            failAlert.showAndWait();
        }
        if (deleted) {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Operation Complete");
            successAlert.setHeaderText("");
            successAlert.setContentText("The part was successfully removed from the product.");
            successAlert.showAndWait();
        }
    }

    public void onSearch(ActionEvent actionEvent) {
        String q = search.getText();
        ObservableList<Part> parts = Inventory.lookupPart(q);
        if (parts.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Part part = Inventory.lookupPart(id);
                if (part != null) {
                    partsTable.getSelectionModel().select(part);
                }
            } catch (NumberFormatException e) {
                //ignore - search by name instead
            }
        }
        else if (parts.size() == 1) {
            partsTable.setItems(parts);
            partsTable.getSelectionModel().selectFirst();
        }
        else {
            partsTable.setItems(parts);
            partsTable.getSelectionModel().clearSelection();
        }
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
            newMin = Integer.parseInt(min.getText());
        } catch (Exception e) {
            errorList.add("Please enter integer data for the Min field.\n");
            save = false;
        }
        try {
            newMax = Integer.parseInt(max.getText());
        } catch (Exception e) {
            errorList.add("Please enter integer data for the Max field.\n");
            save = false;
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
            Product newProduct = new Product(displayedProduct.getId(), name.getText(), newPrice, newInv, newMin, newMax);
            for (Part p : products) {
                newProduct.addAssociatedPart(p);
            }
            Inventory.updateProduct(newProduct.getId(), newProduct);
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
