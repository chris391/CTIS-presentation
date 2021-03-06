package SourceCode.BusinessLogic;

import SourceCode.Controller.admin.CreateEmployeeController;
import SourceCode.Controller.admin.CreateItemController;
import SourceCode.Controller.main.MainViewController;
import SourceCode.Model.adminTableViewObjects.CategoryObj;
import SourceCode.Model.adminTableViewObjects.EmployeeObj;
import SourceCode.Model.adminTableViewObjects.ItemObj;
import SourceCode.Model.dbTablesObjects.Item;
import SourceCode.Model.insertIntoDBObjects.WriteConsumablesToDB;
import SourceCode.Model.insertIntoDBObjects.WriteReturnToDB;
import SourceCode.Model.insertIntoDBObjects.WriteTakeToDB;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

public class BusinessLogic {

    private Item item;
    private EmployeeObj employeeObj;
    ConnectDB connectDB = Factory.connectDB;


    /* METHOD FOR INSERTING EMPLOYEE INTO THE DATABASE */
    public void insertEmployee(int employeeBarcode, String employeeNo, String employeeName, String telephoneNo) throws MySQLIntegrityConstraintViolationException{
        String sql = "INSERT INTO Employee VALUES (?, ?, ?)";
        String sql2 = "INSERT INTO PhoneNumber (id, phoneNumber, employeeBarcode) VALUES (null, ?, (SELECT employeeBarcode FROM Employee WHERE employeeBarcode =?));";
        try {
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            PreparedStatement preparedStatement2 = connectDB.preparedStatement(sql2);
            preparedStatement.setInt(1, employeeBarcode);
            preparedStatement.setString(2, employeeNo);
            preparedStatement.setString(3, employeeName);
            preparedStatement2.setString(1, telephoneNo);
            preparedStatement2.setInt(2, employeeBarcode);
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
            MainViewController.updateAlertMessage("Registration successful");


        } catch (SQLException e) {
            e.printStackTrace();
            MainViewController.updateWarningMessage("Possible duplicates");
            System.out.println("Error in insertEmployee() from BusinessLogic class: " + e.getMessage());
        }
    }

    /* METHOD FOR INSERTING ITEM INTO THE DATABASE */
    public void insertItem(int itemBarcode, String itemNo, String itemName, String itemCategory) {
//        CreateItemController createItemCon = null;

        String sql = "INSERT INTO Item (itemBarcode, itemNo, itemName, itemCategory) VALUES (?, ?, ?, ?); ";
        //String sql2 = "INSERT INTO Category (id, category, itemBarcode) VALUES (null, ?, (SELECT itemBarcode FROM Item WHERE itemBarcode = ?));";
        try {
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            //PreparedStatement preparedStatement2 = connectDB.preparedStatement(sql2);
            preparedStatement.setInt(1, itemBarcode);
            preparedStatement.setString(2, itemNo);
            preparedStatement.setString(3, itemName);
            preparedStatement.setString(4, itemCategory);
            //preparedStatement2.setString(1, category);
            //preparedStatement2.setInt(2, itemBarcode);
            preparedStatement.executeUpdate();
            //preparedStatement2.executeUpdate();
            MainViewController.updateAlertMessage("Registration successful");

//            createItemCon.tfItemNo.clear();
//            createItemCon.tfItemName.clear();
//            createItemCon.tfItemBarcode.clear();
//            createItemCon.tfItemNo.requestFocus();
        } catch (SQLException e) {
            MainViewController.updateWarningMessage("Possible duplicates");
            e.printStackTrace();
            System.out.println("Error in insertItem() from BusinessLogic class: " + e.getMessage());
        }
    }
    public void insertCategory(String category) {
        String sql = "INSERT INTO Category (category) VALUES (?); ";
        try {
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setString(1, category);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            MainViewController.updateWarningMessage("Possible duplicates");
            e.printStackTrace();
            System.out.println("Error in insertCategory() from BusinessLogic class: " + e.getMessage());
        }
    }

    /* METHOD FOR UPDATING THE EMPLOYEE TABLE */
    public void updateEmployee(String employeeBarcode, String employeeNo, String employeeName, String phoneNumber, String oldBarcode) {
        String sql = "UPDATE Employee, PhoneNumber\n" +
                "SET Employee.employeeBarcode = ?, Employee.employeeNo = ?, Employee.employeeName = ?, PhoneNumber.phoneNumber = ?, PhoneNumber.employeeBarcode = ?\n" +
                "WHERE Employee.employeeBarcode = PhoneNumber.employeeBarcode\n" +
                "AND Employee.employeeBarcode = ?;";

        try {

            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setString(1, employeeBarcode);
            preparedStatement.setString(2, employeeNo);
            preparedStatement.setString(3, employeeName);

            //preparedStatement.setString(4, oldBarcode);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, employeeBarcode);

            preparedStatement.setString(6, oldBarcode);
            preparedStatement.executeUpdate();


            MainViewController.updateAlertMessage("Registration successful");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in updateEmployee() from BusinessLogic class: " + e.getMessage());
            MainViewController.updateWarningMessage("Possible duplicates!");
        }
    }

    /* METHOD FOR UPDATING THE ITEM TABLE */
    public void updateItem(String itemBarcode, String itemNo, String itemName, String itemCategory, String oldBarcode) {
//        String sql = "UPDATE Item, Category\n" +
//                "SET Item.itemBarcode = ?, Item.itemNo = ?, Item.itemName = ?, Category.category = ?\n" +
//                "WHERE Item.itemBarcode = Category.itemBarcode\n" +
//                "AND Item.itemBarcode = ?;";
        String sql = "UPDATE Item\n" +
                "SET Item.itemBarcode = ?, Item.itemNo = ?, Item.itemName = ?, Item.itemCategory = ?\n" +
                "WHERE Item.itemBarcode = ?;";

        try {

            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setString(1, itemBarcode);
            preparedStatement.setString(2, itemNo);
            preparedStatement.setString(3, itemName);

            preparedStatement.setString(4, itemCategory);

            preparedStatement.setString(5, oldBarcode);
            preparedStatement.executeUpdate();


            MainViewController.updateAlertMessage("Registration successful");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in updateItem() from BusinessLogic class: " + e.getMessage());
            MainViewController.updateWarningMessage("Possible duplicates!");
        }
    }

    public void updateCategory(String category, String newCategory) {
//        String sql = "UPDATE Item, Category\n" +
//                "SET Item.itemBarcode = ?, Item.itemNo = ?, Item.itemName = ?, Category.category = ?\n" +
//                "WHERE Item.itemBarcode = Category.itemBarcode\n" +
//                "AND Item.itemBarcode = ?;";
        String sql = "UPDATE Category\n" +
                "SET Category.category = ?\n" +
                "WHERE Category.category = ?;";

        try {

            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setString(1, newCategory);
            preparedStatement.setString(2, category);

            System.out.println(category);
            System.out.println("new category: "+ newCategory);

            preparedStatement.executeUpdate();


            MainViewController.updateAlertMessage("Registration successful");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in updateCategory() from BusinessLogic class: " + e.getMessage());
            MainViewController.updateWarningMessage("Possible duplicates!");
        }
    }

//    /* METHOD FOR UPDATING THE BORROWED ITEM TABLE */
//    public Item updateBorrowedItemTable(String table, Timestamp timeReturned, int itemBarcode) {
//        String sql = "UPDATE " + table + " SET timeReturned =? WHERE itemBarcode =? AND timeReturned IS null;";
//        try {
//            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
//
//            preparedStatement.setTimestamp(1, timeReturned);
//            preparedStatement.setInt(2, itemBarcode);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Error in updateBorrowedItemTable() from BusinessLogic class: " + e.getMessage());
//        }
//        return item;
//    }

    /* METHOD FOR DELETING AN EMPLOYEE FROM THE DATABASE */
    public void deleteEmployee(String employeeBarcode) {

        String sql = "DELETE FROM Employee WHERE employeeBarcode=?";
        String sql2 = "DELETE FROM PhoneNumber WHERE employeeBarcode=?";
        String sql3 = "DELETE FROM BorrowedItem WHERE employeeBarcode=?";
        String sql4 = "DELETE FROM UsedProduct WHERE employeeBarcode=?";
        try {
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setString(1, employeeBarcode);
            PreparedStatement preparedStatement2 = connectDB.preparedStatement(sql2);
            preparedStatement2.setString(1, employeeBarcode);
            PreparedStatement preparedStatement3 = connectDB.preparedStatement(sql3);
            preparedStatement3.setString(1, employeeBarcode);
            PreparedStatement preparedStatement4 = connectDB.preparedStatement(sql4);
            preparedStatement4.setString(1, employeeBarcode);

            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
            preparedStatement3.executeUpdate();
            preparedStatement4.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            MainViewController.updateWarningMessage("Error while trying to delete employee");
            System.out.println("Error in deleteEmployee() from BusinessLogic class: " + e.getMessage());
        }
    }

    /* METHOD FOR DELETING AN ITEM FROM THE DATABASE */
    public void deleteItem(String itemBarcode) {
        String sql = "DELETE FROM Item WHERE itemBarcode=?";
        String sql2 = "DELETE FROM BorrowedItem WHERE itemBarcode=?";
        String sql3 = "DELETE FROM UsedProduct WHERE itemBarcode=?";

        try {
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setString(1, itemBarcode);
            PreparedStatement preparedStatement2 = connectDB.preparedStatement(sql2);
            preparedStatement2.setString(1, itemBarcode);
            PreparedStatement preparedStatement3 = connectDB.preparedStatement(sql3);
            preparedStatement3.setString(1, itemBarcode);

            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
            preparedStatement3.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            MainViewController.updateWarningMessage("Error while trying to delete item");
            System.out.println("Error in deleteItem() from BusinessLogic class: " + e.getMessage());
        }
    }

    public void deleteCategory(String category) {
        String sql = "DELETE FROM Category WHERE category=?";

        try {
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setString(1, category);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            MainViewController.updateWarningMessage("Error while trying to delete category");
            System.out.println("Error in deleteCategory() from BusinessLogic class: " + e.getMessage());
        }
    }

    /* METHOD FOR INSERTING INTO BORROWED ITEM TABLE */
    public void takeItem(ArrayList<WriteTakeToDB> arrayList) {
        String sql = "INSERT INTO BorrowedItem VALUES (null, ?, ?, ?, null, null);";
        String sql2 = "INSERT INTO Place VALUES (null, ?, ?, (SELECT id FROM BorrowedItem WHERE itemBarcode = ? AND timeReturned IS NULL));";
        Iterator<WriteTakeToDB> it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                WriteTakeToDB writeTakeToDB = it.next();
                PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
                PreparedStatement preparedStatement2 = connectDB.preparedStatement(sql2);
                preparedStatement.setInt(1, Integer.valueOf(writeTakeToDB.getEmployeeBarcode()));
                preparedStatement.setString(2, writeTakeToDB.getItemBarcode());
                preparedStatement.setString(3, writeTakeToDB.getTimeTaken());
                preparedStatement2.setString(1, writeTakeToDB.getPlace());
                preparedStatement2.setString(2, writeTakeToDB.getPlaceReference());
                preparedStatement2.setString(3, writeTakeToDB.getItemBarcode());
                preparedStatement.executeUpdate();
                preparedStatement2.executeUpdate();
                //preparedStatement.close();
                //preparedStatement2.close();

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error in takeItem() from BusinessLogic class: " + e.getMessage());
            }
        }
    }

    public void takeConsumables(ArrayList<WriteConsumablesToDB> arrayList) {
        String sql = "INSERT INTO UsedProduct VALUES (null, ?, ?, ?, ?)";

        Iterator<WriteConsumablesToDB> it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                WriteConsumablesToDB writeConsumablesToDB = it.next();

                PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
                preparedStatement.setString(1, writeConsumablesToDB.getEmployeeBarcode());
                preparedStatement.setString(2, writeConsumablesToDB.getItemBarcode());
                preparedStatement.setInt(3, writeConsumablesToDB.getQuantity());
                preparedStatement.setString(4, writeConsumablesToDB.getTimeTaken());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error in takeConsumables() from BusinessLogic class: " + e.getMessage());
            }
        }
    }
    public void returnItem(ArrayList<WriteReturnToDB> arrayList) {
        String sql = "UPDATE BorrowedItem SET timeReturned = ?, functional = ? WHERE itemBarcode = ? AND timeReturned IS null;";

        Iterator<WriteReturnToDB> it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                WriteReturnToDB writeReturnToDB = it.next();

                PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
                preparedStatement.setString(1, writeReturnToDB.getTimeReturned());
                preparedStatement.setString(2, writeReturnToDB.getFunctional());
                preparedStatement.setString(3, writeReturnToDB.getItembarcode());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error in returnItem() from BusinessLogic class: " + e.getMessage());
            }
        }
    }

    /* METHOD FOR CHECKING IF THE ITEM IS ALREADY REGISTERED TAKEN */
    public boolean searchItemBarcode(String itemBarcode) {
        try {
            String query = "SELECT itemBarcode FROM BorrowedItem " +
                    "WHERE itemBarcode = ? AND timeReturned is null;";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            preparedStatement.setString(1, itemBarcode);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in searchItemBarcode() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }
    public boolean seachItemBarcodeForSearchScreen(String itemBarcode) {
        try {
//            String query = "SELECT itemBarcode FROM BorrowedItem " +
//                    "WHERE itemBarcode = ? AND timeReturned is null;";
            String query = "SELECT Item.itemBarcode FROM Item" +
                    " INNER JOIN BorrowedItem ON" +
                    " Item.itemBarcode = BorrowedItem.itemBarcode" +
                    " WHERE Item.itemBarcode =? AND timeReturned is null;";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            preparedStatement.setString(1, itemBarcode);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in searchItemBarcode() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }
    /* METHOD FOR CHECKING IF THE ITEM IS ALREADY REGISTERED TAKEN */
    public boolean searchItemByNumber(String itemNumber) {
        try {
            String query = "SELECT itemNo FROM Item" +
                    " INNER JOIN BorrowedItem ON" +
                    " Item.itemBarcode = BorrowedItem.itemBarcode" +
                    " WHERE itemNo =? AND timeReturned is null;";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            preparedStatement.setString(1, itemNumber);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in searchItemByNumber() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }

    /* METHOD FOR CHECKING IF THE ITEM IS ALREADY REGISTERED TAKEN */
    public boolean searchEmployeeByName(String employeeName) {
        try {
            String query = "SELECT employeeName FROM Employee"+
                    " INNER JOIN BorrowedItem ON" +
                    " Employee.employeeBarcode = BorrowedItem.employeeBarcode" +
                    " WHERE employeeName =? AND timeReturned is null;";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            preparedStatement.setString(1, employeeName);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in searchEmployeeByName() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }
    /* METHOD FOR CHECKING IF THE ITEM IS ALREADY REGISTERED TAKEN */
    public boolean searchEmployeeByBarcode(String employeeBarcode) {
        try {
            String query = "SELECT Employee.employeeBarcode FROM Employee\n" +
                    "INNER JOIN BorrowedItem ON\n" +
                    "Employee.employeeBarcode = BorrowedItem.employeeBarcode\n" +
                    "WHERE Employee.employeeBarcode = ?\n" +
                    "AND timeReturned is null;";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            preparedStatement.setString(1, employeeBarcode);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in searchEmployeeByBarcode() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }

    /* METHOD FOR CHECKING THE EMPLOYEE BARCODE STORED INTO THE DATABASE */
    public boolean checkEmployeeBarcode(String employeeBarcode) {

        try {
            String query = "SELECT employeeBarcode FROM Employee WHERE employeeBarcode=?";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);

            preparedStatement.setString(1, employeeBarcode);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkEmployeeBarcode() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }
    public boolean checkEmployeeNumber(String employeeNumber) {

        try {
            String query = "SELECT employeeNo FROM Employee WHERE employeeNo=?";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);

            preparedStatement.setString(1, employeeNumber);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkEmployeeName() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }
    public boolean checkEmployeeName(String employeeName) {

        try {
            String query = "SELECT employeeName FROM Employee WHERE employeeName=?";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);

            preparedStatement.setString(1, employeeName);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkEmployeeName() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }


    /* METHOD FOR CHECKING THE ITEM BARCODE STORED INTO THE DATABASE */
    public boolean checkItemBarcode(String itemBarcode) {

        try {

            String query = "SELECT itemBarcode FROM Item WHERE itemBarcode = ?";
            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            //int code = Integer.valueOf(itemBarcode);
            preparedStatement.setString(1, itemBarcode);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Error in checkItemBarcode() from BusinessLogic class: " + e.getMessage());
        } catch (Exception e){
//            e.printStackTrace();

            System.out.println("Error in checkItemBarcode() from BusinessLogic class(usually-just a parse error): " + e.getMessage());
        }
        return false;
    }
    public boolean checkItemNo(String itemNo) {

        try {
            String query = "SELECT itemNo FROM Item WHERE itemNo = ?";
            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            preparedStatement.setString(1, itemNo);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkItemNo() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }
    public boolean checkItemCategory(String itemCategory) {

        try {
            String query = "SELECT itemCategory FROM Item WHERE itemCategory = ?";
            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            preparedStatement.setString(1, itemCategory);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkItemCategory() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }

    public boolean checkItemName(String name) {

        try {
            String query = "SELECT itemName FROM Item WHERE itemName = ?";
            PreparedStatement preparedStatement = connectDB.preparedStatement(query);
            preparedStatement.setString(1, name);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkItemName() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }

    /* METHOD FOR CHECKING THE LOGIN CREDENTIALS */
    public boolean checkLoginCredentials(String username, String password) {
        try {
            String query = "SELECT * FROM Admin WHERE username=? AND password=?";

            PreparedStatement preparedStatement = connectDB.preparedStatement(query);

            preparedStatement.setString(1, username);
            preparedStatement.setString(1, password);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkLoginCredentials() from BusinessLogic class: " + e.getMessage());
        }
        return false;
    }

    public boolean checkUsername(String username){
        try {
            String sql = "SELECT username FROM Admin WHERE username = ?";

            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error in checkUsername in BusinessLogic");
        }
        return false;
    }
    public boolean checkPassword(String password){
        try {
            String sql = "SELECT password FROM Admin WHERE password = ?";

            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            preparedStatement.setString(1, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error in checkPassword in BusinessLogic");
        }
        return false;
    }

    /* METHOD FOR RETURNING THE CATEGORY */
    public ObservableList<String> getCategory() {
        ObservableList<String> observableList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT category FROM Category";
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {


                String category = (resultSet.getString(1));


                observableList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getItemCategory() from BusinessLogic class: "  + e.getMessage());
        }
        return observableList;
    }

    public ObservableList<String> getPlace() {
        ObservableList<String> observableList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT place FROM Place";
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {


                String place = (resultSet.getString(1));


                observableList.add(place);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getPlace() from BusinessLogic class: "  + e.getMessage());
        }
        return observableList;
    }
    public String getEmployeeName(String employeeBarcode) {
        String name = "";
        //ArrayList<String> arrayList = new ArrayList<>();

        try {
            String sql = "SELECT employeeName FROM Employee WHERE employeeBarcode = ?";
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            preparedStatement.setString(1, employeeBarcode);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {


                name = resultSet.getString(1);
                //arrayList.add(employeeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getEmployeeName() from BusinessLogic class: "  + e.getMessage());
        }
        return name;
    }

    public EmployeeObj getEmployee(String employeeBarcode) {
        //ObservableList<EmployeeObj> observableList = FXCollections.observableArrayList();
        EmployeeObj employeeObj = new EmployeeObj();

        try {
            String sql = "SELECT Employee.employeeBarcode, employeeNo, employeeName, phoneNumber FROM Employee\n" +
                    "INNER JOIN PhoneNumber ON\n" +
                    "Employee.employeeBarcode = PhoneNumber.employeeBarcode\n"+
                    "WHERE Employee.employeeBarcode = ?";
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            preparedStatement.setString(1, employeeBarcode);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String barcode = resultSet.getString("employeeBarcode");
                String number = resultSet.getString("employeeNo");
                String name = resultSet.getString("employeeName");
                String phone = resultSet.getString("phoneNumber");

                employeeObj.setEmployeeBarcode(barcode);
                employeeObj.setEmployeeNo(number);
                employeeObj.setEmployeeName(name);
                employeeObj.setPhoneNumber(phone);

                //observableList.add(employeeObj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getEmployee() from BusinessLogic class: "  + e.getMessage());
        }
        return employeeObj;
    }
    public ItemObj getItem(String itemBarcode) {
        //ObservableList<EmployeeObj> observableList = FXCollections.observableArrayList();
        ItemObj itemObj = new ItemObj();

        try {
            String sql = "SELECT Item.itemBarcode, itemNo, itemName, itemCategory FROM Item\n" +
                    "WHERE Item.itemBarcode = ?";
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            preparedStatement.setString(1, itemBarcode);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String barcode = resultSet.getString("itemBarcode");
                String number = resultSet.getString("itemNo");
                String name = resultSet.getString("itemName");
                String category = resultSet.getString("itemCategory");

                itemObj.setItemBarcode(barcode);
                itemObj.setItemNo(number);
                itemObj.setItemName(name);
                itemObj.setItemCategory(category);

                //observableList.add(employeeObj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getItem() from BusinessLogic class: "  + e.getMessage());
        }
        return itemObj;
    }
    public CategoryObj getCategory(String category) {
        CategoryObj categoryObj = new CategoryObj();

        try {
            String sql = "SELECT category FROM Category\n" +
                    "WHERE category = ?";
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            preparedStatement.setString(1, category);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String categoryFound = resultSet.getString("category");

                categoryObj.setCategory(categoryFound);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getCategory() from BusinessLogic class: "  + e.getMessage());
        }
        return categoryObj;
    }
    /* METHOD FOR DELETING A ROW FROM BORROWED ITEM TABLE IN THE DATABASE */
    public void deleteBorrowedItem(int id) {
        String sql = "DELETE FROM UsedItem WHERE id=?";
        try {
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in deleteUsedItem() from BusinessLogic class: " + e.getMessage());
        }
    }

    public long getNewItemBarcode() {
        long max = 0;
        try {
            String sql = "SELECT MAX(itemBarcode) AS max FROM Item";
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                max = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getNewItemBarcode() from BusinessLogic class: "  + e.getMessage());
        }
        return max+1;
    }
//    public long getNewItemBarcode2(){
//        System.out.println(1);
//        CreateItemController createItemCon = new CreateItemController();
//        long max = 0;
//        //without key and cleaning department categories
//        ObservableList<String> filteredList = FXCollections.observableArrayList();
//        filteredList.addAll(getCategory());
//        filteredList.remove("Key");
//        filteredList.remove("Cleaning department");
//        System.out.println(2);
////        if (checkItemPrimaryKey()){
////            System.out.println(3);
////            createItemCon.tfItemBarcode.setText("10000");
////        }
//         if(createItemCon.categoryCombo.getSelectionModel().getSelectedItem().toString().equals("Key")){
//            System.out.println(4);
//            try {
//                String sql = "SELECT MAX(itemBarcode) AS max FROM Item WHERE itemCategory = 'Key'";
//                PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
//
//                ResultSet resultSet = preparedStatement.executeQuery();
//
//                while (resultSet.next()) {
//                    max = resultSet.getLong(1);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                System.out.println("Error in getNewItemBarcode2() from BusinessLogic class: "  + e.getMessage());
//            }
//
//        }
//        else if(createItemCon.categoryCombo.getSelectionModel().getSelectedItem().toString().equals("Cleaning department")){
//            System.out.println(5);
//            try {
//                String sql = "SELECT MAX(itemBarcode) AS max FROM Item WHERE itemCategory = 'Cleaning department'";
//                PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
//
//                ResultSet resultSet = preparedStatement.executeQuery();
//
//                while (resultSet.next()) {
//                    max = resultSet.getLong(1);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                System.out.println("Error in getNewItemBarcode2() from BusinessLogic class: "  + e.getMessage());
//            }
//
//        }
//        else if(createItemCon.categoryCombo.getSelectionModel().getSelectedItem().equals(filteredList)){
//            System.out.println(6);
//            try {
//                String sql = "SELECT MAX(itemBarcode) AS max FROM Item WHERE itemCategory != 'Cleaning department' AND itemCategory != 'Key'";
//                PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
//
//                ResultSet resultSet = preparedStatement.executeQuery();
//
//                while (resultSet.next()) {
//                    max = resultSet.getLong(1);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                System.out.println("Error in getNewItemBarcode2() from BusinessLogic class: "  + e.getMessage());
//            }
//        }
//        return max+1;
//    }
    public int getNewEmployeeBarcode() {
        int max = 0;
        try {
            String sql = "SELECT MAX(employeeBarcode) AS max FROM Employee";
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                max = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getNewEmployeeBarcode() from BusinessLogic class: "  + e.getMessage());
        }
        return max+1;
    }

    //Used to determine if there are existing primary keys or not
    public boolean checkItemPrimaryKey(){
        try {
            String sql = "SELECT itemBarcode FROM Item";

            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return false;
            }else {
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error in checkItemPrimaryKey() in BusinessLogic");
        }
        return false;
    }
//    public String checkItemCategory() {
//        try {
//            String sql = "SELECT itemCategory FROM Item";
//
//            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                return false;
//            } else {
//                return true;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Error in checkItemPrimaryKey() in BusinessLogic");
//        }
//        return false;
//    }

    //Used to determine if there are existing primary keys or not
    public boolean checkEmployeePrimaryKey(){
        try {
            String sql = "SELECT employeeBarcode FROM Item";

            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error in checkEmployeePrimaryKey() in BusinessLogic");
        }
        return false;
    }
    public void insertFunctional(String itemBarcode, String functional) {
        String sql = "INSERT INTO Functional (itemBarcode, functional) VALUES (?, ?); ";
        try {
            PreparedStatement preparedStatement = connectDB.preparedStatement(sql);
            preparedStatement.setString(1, itemBarcode);
            preparedStatement.setString(2, functional);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            MainViewController.updateWarningMessage("Error");
            e.printStackTrace();
            System.out.println("Error in insertFunctional() from BusinessLogic class: " + e.getMessage());
        }
    }


}
