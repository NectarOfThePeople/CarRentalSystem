// Booking.java
// Stores the details of one booking.
// status is "Active" while the car is with the customer and "Returned" afterwards.
public class Booking {
    private String bookingId;
    private String carId;
    private String customerId;
    private int days;
    private String status;

    public Booking(String bookingId, String carId, String customerId, int days, String status) {
        this.bookingId = bookingId;
        this.carId = carId;
        this.customerId = customerId;
        this.days = days;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCarId() {
        return carId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getDays() {
        return days;
    }

    public String getStatus() {
        return status;
    }

    // Marks this booking as finished when the car is brought back
    public void returnCar() {
        status = "Returned";
    }

    // Heading line used above the list of bookings
    public static String getHeading() {
        return String.format("%-11s %-8s %-12s %-6s %s",
                "Booking ID", "Car ID", "Customer ID", "Days", "Status");
    }

    // One line of text that shows this booking in the list
    public String displayBooking() {
        return String.format("%-11s %-8s %-12s %-6d %s",
                bookingId, carId, customerId, days, status);
    }

    // One line of text that is saved in bookings.txt
    public String toFileString() {
        return bookingId + "," + carId + "," + customerId + "," + days + "," + status;
    }
}
