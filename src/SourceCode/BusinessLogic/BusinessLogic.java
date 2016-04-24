
package SourceCode.BusinessLogic;

import SourceCode.Model.Employee;
import SourceCode.Model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class BusinessLogic {
    private Item item;
    private Employee employee;
    Factory factory = Factory.getInstance();


    /* METHOD FOR INSERTING EMPLOYEE INTO THE DATABASE */
    public void insertEmployee(int employeeBarcode, String employeeNo, String employeeName, int telephoneNo) {
        String sql = "INSERT INTO Employee VALUES (?, ?, ?)";
        String sql2 = "INSERT INTO PhoneNumber (id, phoneNumber, employeeBarcode) VALUES (null, ?, (SELECT employeeBarcode FROM Employee WHERE employeeBarcode =?));";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);
            PreparedStatement preparedStatement2 = factory.preparedStatement(sql2);
            preparedStatement.setInt(1, employeeBarcode);
            preparedStatement.setString(2, employeeNo);
            preparedStatement.setString(3, employeeName);
            preparedStatement2.setInt(1, telephoneNo);
            preparedStatement2.setInt(2, employeeBarcode);
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in insertEmployee() from BusinessLogic class");
        }
    }

    /* METHOD FOR INSERTING ITEM INTO THE DATABASE */
    public void insertItem(int itemBarcode, String itemNo, String itemName, String category) {
        String sql = "INSERT INTO Item (itemBarcode, itemNo, itemName) VALUES (?, ?, ?); ";
        String sql2 = "INSERT INTO Category (id, category, itemBarcode) VALUES (null, ?, (SELECT itemBarcode FROM Item WHERE itemBarcode = ?));";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);
            PreparedStatement preparedStatement2 = factory.preparedStatement(sql2);
            preparedStatement.setInt(1, itemBarcode);
            preparedStatement.setString(2, itemNo);
            preparedStatement.setString(3, itemName);
            preparedStatement2.setString(1, category);
            preparedStatement2.setInt(2, itemBarcode);
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in insertItem() from BusinessLogic class");
        }
    }

    /* METHOD FOR UPDATING THE EMPLOYEE TABLE */
    public Employee updateEmployeeTable(String table, int employeeBarcode, String employeeNo, String employeeName, int telephoneNo, int oldBarcode) {
        String sql = "UPDATE " + table + " SET employeeBarcode=?, employeeNo=?, employeeName=?, telephoneNo=? WHERE employeeBarcode=?";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);
            preparedStatement.setInt(1, employeeBarcode);
            preparedStatement.setString(2, employeeNo);
            preparedStatement.setString(3, employeeName);
            preparedStatement.setInt(4, telephoneNo);
            preparedStatement.setInt(5, oldBarcode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in updateEmployeeTable() from BusinessLogic class");
        }
        return employee;
    }

    /* METHOD FOR UPDATING THE ITEM TABLE */
    public Item updateItemTable(String table, int itemBarcode, String itemNo, String itemName, String category, int oldBarcode) {
        String sql = "UPDATE " + table + " SET itemBarcode=?, itemNo=?, description=?, category=? WHERE itemBarcode=?;";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);
            preparedStatement.setInt(1, itemBarcode);
            preparedStatement.setString(2, itemNo);
            preparedStatement.setString(3, itemName);
            preparedStatement.setString(4, category);
            preparedStatement.setInt(5, oldBarcode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in updateItemTable() from BusinessLogic class");
        }
        return item;
    }

    /* METHOD FOR UPDATING THE BORROWED ITEM TABLE */
    public Item updateBorrowedItemTable(String table, Timestamp timeReturned, int itemBarcode) {
        String sql = "UPDATE " + table + " SET timeReturned =? WHERE itemBarcode =? AND timeReturned IS null;";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);

            preparedStatement.setTimestamp(1, timeReturned);
            preparedStatement.setInt(2, itemBarcode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in updateBorrowedItemTable() from BusinessLogic class");
        }
        return item;
    }

    /* METHOD FOR DELETING AN EMPLOYEE FROM THE DATABASE */
    public void deleteEmployee(int employeeBarcode) {
        String sql = "DELETE FROM Employee WHERE employeeBarcode=?";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);
            preparedStatement.setInt(1, employeeBarcode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in deleteEmployee() from BusinessLogic class");
        }
    }

    /* METHOD FOR DELETING AN ITEM FROM THE DATABASE */
    public void deleteItem(int itemBarcode) {
        String sql = "DELETE FROM Item WHERE itemBarcode=?";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);
            preparedStatement.setInt(1, itemBarcode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in deleteItem() from BusinessLogic class");
        }
    }

    /* METHOD FOR INSERTING INTO BORROWED ITEM TABLE */
    public void takeItem(int employeeBarcode, int itemBarcode, Timestamp timeTaken, String place) {
        String sql = "INSERT INTO BorrowedItem VALUES (null, ?, ?, ?, null);";
        String sql2 = "INSERT INTO Place VALUES (null, ?, (SELECT id FROM BorrowedItem WHERE itemBarcode = ? AND timeReturned IS NULL));";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);
            PreparedStatement preparedStatement2 = factory.preparedStatement(sql2);
            preparedStatement.setInt(1, employeeBarcode);
            preparedStatement.setInt(2, itemBarcode);
            preparedStatement.setTimestamp(3, timeTaken);
            preparedStatement2.setString(1, place);
            preparedStatement2.setInt(2, itemBarcode);
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
            preparedStatement.close();
            preparedStatement2.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in takeItem() from BusinessLogic class");
        }
    }

    /* METHOD FOR RETURNING THE TAKEN ITEM TO THE DATABASE */
    public void returnItem(int itemBarcode) {
        try {
            String query = "SELECT itemName, timeTaken, employeeName FROM BorrowedItem INNER JOIN Item ON BorrowedItem.itemBarcode = Item.itemBarcode INNER JOIN Employee ON BorrowedItem.employeeBarcode = Employee.employeeBarcode WHERE BorrowedItem.itemBarcode = ? AND BorrowedItem.timeReturned IS NULL;";

            PreparedStatement preparedStatement = factory.preparedStatement(query);
            preparedStatement.setInt(1, itemBarcode);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in returnItem() from BusinessLogic class");
        }
    }

    /* METHOD FOR CHECKING IF THE ITEM IS ALREADY REGISTERED TAKEN */
    public boolean searchItem(int itemBarcode) {
        try {
            String query = "SELECT itemBarcode FROM BorrowedItem WHERE itemBarcode =? AND timeReturned is null;";

            PreparedStatement preparedStatement = factory.preparedStatement(query);

            preparedStatement.setInt(1, itemBarcode);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in searchItem() from BusinessLogic class");
        }
        return false;
    }

    /* METHOD FOR CHECKING THE EMPLOYEE BARCODE STORED INTO THE DATABASE */
    public boolean checkEmployeeBarcode(int employeeBarcode) {

        try {
            String query = "SELECT employeeBarcode FROM Employee WHERE employeeBarcode=?";

            PreparedStatement preparedStatement = factory.preparedStatement(query);

            preparedStatement.setInt(1, employeeBarcode);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkEmployeeBarcode() from BusinessLogic class");
        }
        return false;
    }

    /* METHOD FOR CHECKING THE ITEM BARCODE STORED INTO THE DATABASE */
    public boolean checkItemBarcode(int itemBarcode) {

        try {

            String query = "SELECT itemBarcode FROM Item WHERE itemBarcode = ?";
            PreparedStatement preparedStatement = factory.preparedStatement(query);

            preparedStatement.setInt(1, itemBarcode);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkItemBarcode() from BusinessLogic class");
        }
        return false;
    }

    /* METHOD FOR CHECKING THE LOGIN CREDENTIALS */
    public boolean checkLogInCredentials(String username, String password) {
        try {
            String query = "SELECT * FROM Admin WHERE username=? AND password=?";

            PreparedStatement preparedStatement = factory.preparedStatement(query);

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
            System.out.println("Error in checkLoginCredentials() from BusinessLogic class");
        }
        return false;
    }

    /* RETURN THE LOGIN CREDENTIALS */
    public String getLogin(String name, String pass) {
        String out = "";
        try {
            String nameAndPass = "SELECT * FROM Admin where username = ? AND password= ?";

            PreparedStatement preparedStatement = factory.preparedStatement(nameAndPass);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pass);
            ResultSet results = preparedStatement.executeQuery();

            if (results.next()) {
                out = results.getString(1) + "\n" + results.getString(2);
            } else {
                out = " no data ";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getLogin() from BusinessLogic class");
        }
        return out;
    }

    /* METHOD FOR RETURNING THE CATEGORY */
    public ObservableList<String> getCategory() {
        ObservableList<String> observableList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT category FROM Category";
            PreparedStatement preparedStatement = factory.preparedStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {


                String category = (resultSet.getString(1));


                observableList.add(category);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getCategory() from BusinessLogic class");
        }
        return observableList;
    }

    /* METHOD FOR DELETING A ROW FROM BORROWED ITEM TABLE IN THE DATABASE */
    public void deleteBorrowedItem(int id) {
        String sql = "DELETE FROM UsedItem WHERE id=?";
        try {
            PreparedStatement preparedStatement = factory.preparedStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in deleteUsedItem() from BusinessLogic class");
        }
    }
}