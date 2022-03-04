package controller;

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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class OpeningScreenController implements Initializable {

    //Parts Table
    public TableView partsTable;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;
    public Button addA;
    public Button modifyA;
    public Button deleteA;

    //Products Table
    public TableView productsTable;
    public TableColumn prodIDCol;
    public TableColumn prodNameCol;
    public TableColumn prodInvCol;
    public TableColumn prodPriceCol;
    public Button addB;
    public Button modifyB;
    public Button deleteB;
    public StackPane errorPane;

    //Application Exit
    public Button exitA;
    public TextField searchA;
    public TextField searchB;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProducts());

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    public void onAddA(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPartForm.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyA(ActionEvent actionEvent) throws IOException {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        ModifyPartFormController.receivePart(selectedPart);
        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPartForm.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onDeleteA(ActionEvent actionEvent) {
        boolean deleted = false;
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("");
        alert.setContentText("Are you sure you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deleted = Inventory.deletePart(selectedPart);
        }
        else if (result.get() == ButtonType.CANCEL) {
            Alert abortedAlert = new Alert(Alert.AlertType.WARNING);
            abortedAlert.setTitle("Operation Aborted");
            abortedAlert.setHeaderText("");
            abortedAlert.setContentText("The part was not deleted.");
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
            successAlert.setContentText("The part was successfully deleted.");
            successAlert.showAndWait();
        }
    }

    public void onAddB(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProductForm.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyB(ActionEvent actionEvent) throws IOException {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        ModifyProductFormController.receiveProduct(selectedProduct);
        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProductForm.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onDeleteB(ActionEvent actionEvent) {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (selectedProduct == null)
            return;
        if (selectedProduct.getAllAssociatedParts().isEmpty()) {
            deleted = Inventory.deleteProduct(selectedProduct);
        }
        else {
            String errorMessage = "The selected product has parts associated with it.";
            errorPane.getChildren().add(new Label(errorMessage));
        }
    }

    public void onExitA(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void onSearchA(ActionEvent actionEvent) {
        String q = searchA.getText();
        ObservableList<Part> parts = Inventory.lookupPart(q);
        if (parts.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Part part = Inventory.lookupPart(id);
                if (part != null) {
                    partsTable.getSelectionModel().select(part);
                }
            } catch (NumberFormatException e) {
                //ignore - kick to search by name
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

    public void onSearchB(ActionEvent actionEvent) {
        String q = searchB.getText();
        ObservableList<Product> products = Inventory.lookupProduct(q);
        if (products.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Product product = Inventory.lookupProduct(id);
                if (product != null) {
                    productsTable.getSelectionModel().select(product);
                }
            } catch (NumberFormatException e) {
                //ignore - kick to search by name
            }
        }
        else if (products.size() == 1) {
            productsTable.setItems(products);
            productsTable.getSelectionModel().selectFirst();
        }
        else {
            productsTable.setItems(products);
            productsTable.getSelectionModel().clearSelection();
        }
    }
}
