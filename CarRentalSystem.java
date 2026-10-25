import java.sql.*;
import java.util.ArrayList;

// CarRentalSystem.java
// This class does the main work of the project.
// It talks to the MySQL database using JDBC. Every SQL query is written as a
// PreparedStatement; the ? marks are filled in with setString() / setInt().
public class CarRentalSystem {
    private Connection con;

    public CarRentalSystem() throws SQLException {
        con = DBConnection.getConnection();
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Could not close the connection.");
        }
    }

    // ---------- login ----------

    // true if the admin id and password are correct
    public boolean checkAdmin(String adminId, String password) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT password FROM admins WHERE admin_id = ?");
        ps.setString(1, adminId);
        ResultSet rs = ps.executeQuery();
        boolean correct = false;
        if (rs.next()) {
            // compared in Java (not in SQL) so that the password is case sensitive
            correct = rs.getString("password").equals(password);
        }
        rs.close();
        ps.close();
        return correct;
    }

    // ---------- helper methods that turn the rows of a query into objects ----------

    private ArrayList<Car> readCars(PreparedStatement ps) throws SQLException {
        ArrayList<Car> list = new ArrayList<Car>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Car(rs.getString("car_id"), rs.getString("name"), rs.getString("type"),
                    rs.getInt("rent_per_day"), rs.getBoolean("available")));
        }
        rs.close();
        ps.close();
        return list;
    }

    private ArrayList<Customer> readCustomers(PreparedStatement ps) throws SQLException {
        ArrayList<Customer> list = new ArrayList<Customer>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Customer(rs.getString("customer_id"), rs.getString("name"),
                    rs.getString("phone"), rs.getString("license_no")));
        }
        rs.close();
        ps.close();
        return list;
    }

    private ArrayList<Booking> readBookings(PreparedStatement ps) throws SQLException {
        ArrayList<Booking> list = new ArrayList<Booking>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Booking(rs.getString("booking_id"), rs.getString("car_id"),
                    rs.getString("customer_id"), rs.getInt("days"), rs.getString("status")));
        }
        rs.close();
        ps.close();
        return list;
    }

    // ---------- car operations ----------

    public String addCar(String carId, String name, String type, int rentPerDay) throws SQLException {
        if (findCar(carId) != null) {
            return "A car with this ID already exists.";
        }
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO cars (car_id, name, type, rent_per_day, available) VALUES (?, ?, ?, ?, TRUE)");
        ps.setString(1, carId);
        ps.setString(2, name);
        ps.setString(3, type);
        ps.setInt(4, rentPerDay);
        ps.executeUpdate();
        ps.close();
        return "Car added successfully.";
    }

    public String removeCar(String carId) throws SQLException {
        Car car = findCar(carId);
        if (car == null) {
            return "Car not found.";
        }
        if (!car.isAvailable()) {
            return "This car is on rent, so it cannot be removed.";
        }
        PreparedStatement ps = con.prepareStatement("DELETE FROM cars WHERE car_id = ?");
        ps.setString(1, car.getCarId());
        ps.executeUpdate();
        ps.close();
        return "Car removed successfully.";
    }

    public Car findCar(String carId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM cars WHERE car_id = ?");
        ps.setString(1, carId);
        ArrayList<Car> list = readCars(ps);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public ArrayList<Car> getAllCars() throws SQLException {
        return readCars(con.prepareStatement("SELECT * FROM cars ORDER BY car_id"));
    }

    public ArrayList<Car> getAvailableCars() throws SQLException {
        return readCars(con.prepareStatement(
                "SELECT * FROM cars WHERE available = TRUE ORDER BY car_id"));
    }

    public ArrayList<Car> searchByType(String type) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM cars WHERE type = ? ORDER BY car_id");
        ps.setString(1, type);
        return readCars(ps);
    }

    public ArrayList<Car> searchByPrice(int maxPrice) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM cars WHERE rent_per_day <= ? ORDER BY rent_per_day");
        ps.setInt(1, maxPrice);
        return readCars(ps);
    }

    // ---------- customer operations ----------

    public String registerCustomer(String id, String name, String phone, String licenseNo) throws SQLException {
        if (findCustomer(id) != null) {
            return "A customer with this ID already exists.";
        }
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO customers (customer_id, name, phone, license_no) VALUES (?, ?, ?, ?)");
        ps.setString(1, id);
        ps.setString(2, name);
        ps.setString(3, phone);
        ps.setString(4, licenseNo);
        ps.executeUpdate();
        ps.close();
        return "Customer registered successfully.";
    }

    // returns null if there is no customer with this id (used for the customer login)
    public Customer findCustomer(String customerId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM customers WHERE customer_id = ?");
        ps.setString(1, customerId);
        ArrayList<Customer> list = readCustomers(ps);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public ArrayList<Customer> getAllCustomers() throws SQLException {
        return readCustomers(con.prepareStatement("SELECT * FROM customers ORDER BY customer_id"));
    }

    // ---------- booking and return ----------

    public Booking findBooking(String bookingId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM bookings WHERE booking_id = ?");
        ps.setString(1, bookingId);
        ArrayList<Booking> list = readBookings(ps);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public ArrayList<Booking> getAllBookings() throws SQLException {
        return readBookings(con.prepareStatement(
                "SELECT * FROM bookings ORDER BY LENGTH(booking_id), booking_id"));
    }

    public ArrayList<Booking> getBookingsOfCustomer(String customerId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM bookings WHERE customer_id = ? ORDER BY LENGTH(booking_id), booking_id");
        ps.setString(1, customerId);
        return readBookings(ps);
    }

    private int countBookings() throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM bookings");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        ps.close();
        return count;
    }

    public String bookCar(String carId, String customerId, int days) throws SQLException {
        Car car = findCar(carId);
        Customer customer = findCustomer(customerId);

        if (car == null) {
            return "Car not found.";
        }
        if (customer == null) {
            return "Customer not found. Please register the customer first.";
        }

        String bookingId = "B" + (countBookings() + 1);

        // A transaction: the two changes below are saved together, or not at all.
        con.setAutoCommit(false);
        try {
            // 1. Mark the car as booked. This changes a row only if the car is still
            //    available, so the same car can never be booked twice (no double booking).
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE cars SET available = FALSE WHERE car_id = ? AND available = TRUE");
            ps.setString(1, car.getCarId());
            int changed = ps.executeUpdate();
            ps.close();

            if (changed == 0) {
                con.rollback();
                return "Sorry, this car is already booked.";
            }

            // 2. Save the booking
            ps = con.prepareStatement("INSERT INTO bookings "
                    + "(booking_id, car_id, customer_id, days, status) VALUES (?, ?, ?, ?, 'Active')");
            ps.setString(1, bookingId);
            ps.setString(2, car.getCarId());
            ps.setString(3, customer.getCustomerId());
            ps.setInt(4, days);
            ps.executeUpdate();
            ps.close();

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }

        return "Car booked successfully.\n\n"
                + "Booking ID     : " + bookingId + "\n"
                + "Car            : " + car.getName() + "\n"
                + "Customer       : " + customer.getName() + "\n"
                + "Days           : " + days + "\n"
                + "Rent (approx.) : Rs. " + (car.getRentPerDay() * days);
    }

    // daysUsed = number of days the customer really kept the car
    public String returnCar(String bookingId, int daysUsed) throws SQLException {
        Booking booking = findBooking(bookingId);
        if (booking == null) {
            return "Booking not found.";
        }
        if (!booking.getStatus().equals("Active")) {
            return "This car has already been returned.";
        }

        Car car = findCar(booking.getCarId());
        Customer customer = findCustomer(booking.getCustomerId());
        if (car == null || customer == null) {
            return "The car or the customer of this booking is missing from the database.";
        }

        // make the bill
        Bill bill = new Bill("BL" + booking.getBookingId().substring(1), booking.getBookingId());
        bill.calculateBill(car.getRentPerDay(), booking.getDays(), daysUsed);

        // update the booking and the car together (one transaction)
        con.setAutoCommit(false);
        try {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE bookings SET status = 'Returned' WHERE booking_id = ?");
            ps.setString(1, booking.getBookingId());
            ps.executeUpdate();
            ps.close();

            ps = con.prepareStatement("UPDATE cars SET available = TRUE WHERE car_id = ?");
            ps.setString(1, car.getCarId());
            ps.executeUpdate();
            ps.close();

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }

        String text = "Car returned successfully.\n\n";
        text += "Customer     : " + customer.getName() + "\n";
        text += "Car          : " + car.getName() + "\n";
        text += "Rent per day : Rs. " + car.getRentPerDay() + "\n";
        text += "Days booked  : " + booking.getDays() + "\n";
        text += "Days used    : " + daysUsed + "\n";
        text += "(Fine is Rs. " + Bill.FINE_PER_DAY + " for every extra day)\n";
        text += "------------------------------\n";
        text += bill.displayBill();
        return text;
    }
}
