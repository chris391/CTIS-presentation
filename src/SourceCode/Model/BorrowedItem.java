package SourceCode.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.format.DateTimeFormatter;

public class BorrowedItem {
    private final IntegerProperty id;
    private final IntegerProperty employeeBarcode;
    private final IntegerProperty itemBarcode;
    private final ObjectProperty<DateTimeFormatter> timeTaken;
    private final ObjectProperty<DateTimeFormatter> timeReturned;
    //set a place for the item - either address or car plate number

    //DEFAULT CONSTRUCTOR
    public BorrowedItem() {
        this(0, 0, 0, null, null);
    }

    //CONSTRUCTOR
    public BorrowedItem(int id, int employeeBarcode, int itemBarcode, String timeTaken, String timeReturned) {
        this.id = new SimpleIntegerProperty(id);
        this.employeeBarcode = new SimpleIntegerProperty(employeeBarcode);
        this.itemBarcode = new SimpleIntegerProperty(itemBarcode);
        this.timeTaken = new SimpleObjectProperty(timeTaken);
        this.timeReturned = new SimpleObjectProperty(timeReturned);
    }

    //GET METHODS
    public final int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty employeeBarcodeProperty() {
        return employeeBarcode;
    }

    public IntegerProperty itemBarcodeProperty() { return itemBarcode; }

    public ObjectProperty timeTakenProperty() {
        return timeTaken;
    }

    public ObjectProperty timeReturnedProperty() {
        return timeReturned;
    }
}