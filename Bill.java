// Bill.java
// Calculates the bill when a car is returned.
// Total = rentPerDay x days + fine
public class Bill {
    public static final int FINE_PER_DAY = 500;   // fine for every extra day

    private String billId;
    private String bookingId;
    private int amount;
    private int fine;

    public Bill(String billId, String bookingId) {
        this.billId = billId;
        this.bookingId = bookingId;
    }

    // bookedDays = days in the booking, daysUsed = days the customer really kept the car
    public void calculateBill(int rentPerDay, int bookedDays, int daysUsed) {
        fine = 0;
        if (daysUsed > bookedDays) {
            fine = (daysUsed - bookedDays) * FINE_PER_DAY;
        }
        amount = rentPerDay * bookedDays + fine;
    }

    public String getBillId() {
        return billId;
    }

    public int getAmount() {
        return amount;
    }

    public int getFine() {
        return fine;
    }

    // Text of the bill to show on the screen
    public String displayBill() {
        String text = "Bill ID      : " + billId + "\n";
        text += "Booking ID   : " + bookingId + "\n";
        text += "Fine         : Rs. " + fine + "\n";
        text += "Total Amount : Rs. " + amount + "\n";
        return text;
    }
}
