package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    public static void addPart (Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct (Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart (int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return  part;
            }
        }
        return null;
    }

    public static Product lookupProduct (int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return  product;
            }
        }
        return null;
    }

    public static ObservableList<Part> lookupPart (String partName) {
        ObservableList<Part> partNames = FXCollections.observableArrayList();
        for(Part part : allParts) {
            if (part.getName().contains(partName)) {
                partNames.add(part);
            }
        }
        return partNames;
    }

    public static ObservableList<Product> lookupProduct (String productName) {
        ObservableList<Product> productNames = FXCollections.observableArrayList();
        for(Product product : allProducts) {
            if (product.getName().contains(productName)) {
                productNames.add(product);
            }
        }
        return productNames;
    }

    public static void updatePart (int index, Part selectedPart) {
        int replaceIndex = 0;
        boolean replace = false;
        for (Part part : allParts) {
            if (part.getId() == index) {
                replaceIndex = allParts.indexOf(part);
                replace = true;
            }
        }
        if (replace) {
            allParts.set(replaceIndex, selectedPart);
        }
    }

    public static void updateProduct (int index, Product newProduct) {
        int replaceIndex = 0;
        boolean replace = false;
        for (Product product : allProducts) {
            if (product.getId() == index) {
                replaceIndex = allProducts.indexOf(product);
                replace = true;
            }
        }
        if (replace) {
            allProducts.set(replaceIndex, newProduct);
        }
    }

    public static boolean deletePart (Part selectedPart) {
        if (selectedPart == null) {
            return false;
        }
        else {
            allParts.remove(selectedPart);
            return true;
        }
    }

    public static boolean deleteProduct (Product selectedProduct) {
        if (selectedProduct == null) {
            return false;
        }
        else {
            allProducts.remove(selectedProduct);
            return true;
        }
    }

    public static ObservableList<Part> getAllParts () {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts () {
        return allProducts;
    }
}
