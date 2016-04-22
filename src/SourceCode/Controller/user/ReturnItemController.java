package SourceCode.Controller.user;

import SourceCode.BusinessLogic.BusinessLogic;
import SourceCode.BusinessLogic.Factory;
import SourceCode.Controller.RunView;
import SourceCode.Model.BorrowedItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

//import static SourceCode.BusinessLogic.Factory.conn;

public class ReturnItemController {
    // Observable list
    private ObservableList borrowedItemData;
    Connection conn;

    Factory factory = Factory.getInstance();
    BusinessLogic businessLogic;
    @FXML
    private TextField tfItemBarcode;
    @FXML
    private TableView tableView;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnSubmit;

    private RunView runView;



    @FXML
    private void btnBackAction() throws IOException {
        runView.showMainView();
    }
    @FXML
    private void btnDeleteAction() throws IOException {
        // YOUR CODE HERE

    }

    @FXML
    private void btnSubmitAction() {
//        try {
//            if (tfItemBarcode.getLength() > 0) {
//                if (businessLogic.checkIfItemIsTaken(Integer.parseInt(tfItemBarcode.getText()))) {
//
//                   java.util.Date today = new java.util.Date();
//                    Timestamp timeReturned = new Timestamp(today.getTime());
//                    businessLogic.returnItem(Integer.parseInt(tfItemBarcode.getText()));
//                    businessLogic.updateBorrowedItemTable("BorrowedItem", timeReturned, Integer.parseInt(tfItemBarcode.getText()));
//
//                    updateAlertMessage("Item returned");
//                    runView.showMainView();
//                } else if (!businessLogic.checkIfItemIsTaken(Integer.parseInt(tfItemBarcode.getText()))) {
//                    updateAlertMessage("Item has not been taken by employee");
//                    tfItemBarcode.setText(null);
//                }
//            } else {
//                updateAlertMessage("Missing item barcode");
//            }
//        } catch (Exception e) {
//            System.out.println("Exception: " + e.getMessage());
//        }
    }

    @FXML
    private void checkItemBarcode() {
        int barcode = Integer.parseInt(tfItemBarcode.getText());

        try {

            if (businessLogic.checkItemBarcode(barcode)) {


                initComponents();

            } else {
                updateAlertMessage("Please scan the barcode again");
                tfItemBarcode.setText(null);
            }
        } catch (Exception e) {
            System.out.println("Exception in checkItemBarcode() from ReturnItemController class: " + e.getMessage());
        }
    }

    public void initComponents() {

        //CALLING BUILD DATA METHOD
        buildData();


//        //SETTING COLUMNS FOR ITEM TABLE VIEW
//        TableColumn columnItem = new TableColumn<Item, Integer>("Barcode: ");
//        columnItem.setCellValueFactory(new PropertyValueFactory<Item, Integer>("itemBarcode"));
//        columnItem.setMinWidth(200);
//        TableColumn columnItemNo = new TableColumn<Item, Integer>("Item number: ");
//        columnItemNo.setCellValueFactory(new PropertyValueFactory<Item, Integer>("itemNo"));
//        columnItemNo.setMinWidth(200);
//        TableColumn colProperty = new TableColumn<Item, String>("Description: ");
//        colProperty.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
//        colProperty.setMinWidth(300);

        //SETTING COLUMNS FOR USED ITEM TABLE VIEW
        TableColumn columnId = new TableColumn<BorrowedItem, Integer>("Id: ");
        columnId.setCellValueFactory(new PropertyValueFactory<BorrowedItem, Integer>("id"));
        columnId.setMinWidth(50);
        TableColumn columnBarcodeEmployee = new TableColumn<BorrowedItem, Integer>("Employee barcode: ");
        columnBarcodeEmployee.setCellValueFactory(new PropertyValueFactory<BorrowedItem, Integer>("employeeBarcode"));
        columnBarcodeEmployee.setMinWidth(100);
        TableColumn columnBarcodeItem = new TableColumn<BorrowedItem, Integer>("Item barcode: ");
        columnBarcodeItem.setCellValueFactory(new PropertyValueFactory<BorrowedItem, Integer>("itemBarcode"));
        columnBarcodeItem.setMinWidth(100);
        TableColumn columnTimeTaken = new TableColumn<BorrowedItem, DateTimeFormatter>("Time taken: ");
        columnTimeTaken.setCellValueFactory(new PropertyValueFactory<BorrowedItem, DateTimeFormatter>("timeTaken"));
        columnTimeTaken.setMinWidth(170);
        TableColumn columnTimeReturned = new TableColumn<BorrowedItem, DateTimeFormatter>("Time returned: ");
        columnTimeReturned.setCellValueFactory(new PropertyValueFactory<BorrowedItem, DateTimeFormatter>("timeReturned"));
        columnTimeReturned.setMinWidth(170);

        //ASSIGNING THE COLUMNS TO THE TABLE VIEW

        tableView.getColumns().setAll(columnId, columnBarcodeEmployee, columnBarcodeItem, columnTimeTaken, columnTimeReturned);
    }


    public void buildData(){
        //THE CONNECTION
        //Connection conn;

        //THE OBSERVABLE LIST
        borrowedItemData = FXCollections.observableArrayList();
        try {
            //conn = factory.conn;

            //SQL QUERIES
            String sql = "SELECT * FROM BorrowedItem;";

            //EXECUTE QUERIES
            ResultSet result = conn.prepareStatement(sql).executeQuery();



            while ((result.next())){
                int rowBarcode = result.getInt("itemBarcode");
                int itemBarcodeInt = Integer.parseInt(tfItemBarcode.getText());

                if (rowBarcode == itemBarcodeInt) {

                    BorrowedItem borrowedItem = new BorrowedItem();
                    borrowedItem.idProperty().set(result.getInt("id"));
                    borrowedItem.employeeBarcodeProperty().set(result.getInt("employeeBarcode"));
                    borrowedItem.itemBarcodeProperty().set(result.getInt("itemBarcode"));
                    borrowedItem.timeTakenProperty().set(result.getString("timeTaken"));
                    borrowedItem.timeReturnedProperty().set(result.getString("timeReturned"));

                    borrowedItemData.add(borrowedItem);

                }


            }
            //OBSERVABLE LIST ADDED TO TABLE VIEW
            //tableView.setItems(borrowedItemData);
            tableView.getItems().addAll(borrowedItemData);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building BorrowedItem Data");
        }

    }

    //METHOD FOR THE ALERT MESSAGES SHOWN TO THE USER
    public void updateAlertMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
