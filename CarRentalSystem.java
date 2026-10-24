import java.io.*;
import java.util.ArrayList;

// CarRentalSystem.java
// This class does the main work of the project.
// It keeps the lists of cars, customers and bookings and saves them in text files.
public class CarRentalSystem {
    private ArrayList<Car> cars = new ArrayList<Car>();
    private ArrayList<Customer> customers = new ArrayList<Customer>();
    private ArrayList<Booking> bookings = new ArrayList<Booking>();

    public CarRentalSystem() {
        File file = new File("cars.txt");
        if (file.exists()) {
            loadData();
        } else {
            loadSampleData();   // first time the program runs
        }
    }

    // ---------- getters ----------

    public ArrayList<Car> getCars() {
        return cars;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    // ---------- find methods (return null if not found) ----------

    public Car findCar(String carId) {
        for (Car c : cars) {
            if (c.getCarId().equalsIgnoreCase(carId)) {
                return c;
            }
        }
        return null;
    }

    public Customer findCustomer(String customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(customerId)) {
                return c;
            }
        }
        return null;
    }

    public Booking findBooking(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equalsIgnoreCase(bookingId)) {
                return b;
            }
        }
        return null;
    }

    // ---------- car operations ----------

    public String addCar(String carId, String name, String type, int rentPerDay) {
        carId = clean(carId);
        if (findCar(carId) != null) {
            return "A car with this ID already exists.";
        }
        cars.add(new Car(carId, clean(name), clean(type), rentPerDay, true));
        saveData();
        return "Car added successfully.";
    }

    public String removeCar(String carId) {
        Car car = findCar(carId);
        if (car == null) {
            return "Car not found.";
        }
        if (!car.isAvailable()) {
            return "This car is on rent, so it cannot be removed.";
        }
        cars.remove(car);
        saveData();
        return "Car removed successfully.";
    }

    public ArrayList<Car> searchByType(String type) {
        ArrayList<Car> result = new ArrayList<Car>();
        for (Car c : cars) {
            if (c.getType().equalsIgnoreCase(type)) {
                result.add(c);
            }
        }
        return result;
    }

    public ArrayList<Car> searchByPrice(int maxPrice) {
        ArrayList<Car> result = new ArrayList<Car>();
        for (Car c : cars) {
            if (c.getRentPerDay() <= maxPrice) {
                result.add(c);
            }
        }
        return result;
    }

    public ArrayList<Car> getAvailableCars() {
        ArrayList<Car> result = new ArrayList<Car>();
        for (Car c : cars) {
            if (c.isAvailable()) {
                result.add(c);
            }
        }
        return result;
    }

    // ---------- customer operations ----------

    public String registerCustomer(String id, String name, String phone, String licenseNo) {
        id = clean(id);
        if (findCustomer(id) != null) {
            return "A customer with this ID already exists.";
        }
        customers.add(new Customer(id, clean(name), clean(phone), clean(licenseNo)));
        saveData();
        return "Customer registered successfully.";
    }

    // ---------- booking and return ----------

    public String bookCar(String carId, String customerId, int days) {
        Car car = findCar(carId);
        Customer customer = findCustomer(customerId);

        if (car == null) {
            return "Car not found.";
        }
        if (customer == null) {
            return "Customer not found. Please register the customer first.";
        }
        if (!car.isAvailable()) {
            return "Sorry, this car is already booked.";   // this stops double booking
        }

        String bookingId = "B" + (bookings.size() + 1);
        bookings.add(new Booking(bookingId, car.getCarId(), customer.getCustomerId(), days, "Active"));
        car.setAvailable(false);
        saveData();

        return "Car booked successfully.\n\n"
                + "Booking ID     : " + bookingId + "\n"
                + "Car            : " + car.getName() + "\n"
                + "Customer       : " + customer.getName() + "\n"
                + "Days           : " + days + "\n"
                + "Rent (approx.) : Rs. " + (car.getRentPerDay() * days);
    }

    // daysUsed = number of days the customer really kept the car
    public String returnCar(String bookingId, int daysUsed) {
        Booking booking = findBooking(bookingId);
        if (booking == null) {
            return "Booking not found.";
        }
        if (!booking.getStatus().equals("Active")) {
            return "This car has already been returned.";
        }

        Car car = findCar(booking.getCarId());
        Customer customer = findCustomer(booking.getCustomerId());

        // make the bill
        Bill bill = new Bill("BL" + booking.getBookingId().substring(1), booking.getBookingId());
        bill.calculateBill(car.getRentPerDay(), booking.getDays(), daysUsed);

        // update the data
        booking.returnCar();
        car.setAvailable(true);
        saveData();

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

    // ---------- file handling ----------

    // Commas are used to separate the values in the files, so remove them from the input
    private String clean(String text) {
        return text.replace(",", " ");
    }

    private ArrayList<String> readLines(String fileName) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals("")) {
                    lines.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Could not read " + fileName);
        }
        return lines;
    }

    private void writeLines(String fileName, ArrayList<String> lines) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(fileName));
            for (String line : lines) {
                pw.println(line);
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Could not write " + fileName);
        }
    }

    private void loadData() {
        try {
            for (String line : readLines("cars.txt")) {
                String[] p = line.split(",");
                cars.add(new Car(p[0], p[1], p[2], Integer.parseInt(p[3]), Boolean.parseBoolean(p[4])));
            }
            for (String line : readLines("customers.txt")) {
                String[] p = line.split(",");
                customers.add(new Customer(p[0], p[1], p[2], p[3]));
            }
            for (String line : readLines("bookings.txt")) {
                String[] p = line.split(",");
                bookings.add(new Booking(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4]));
            }
        } catch (Exception e) {
            System.out.println("A data file is not in the correct format: " + e.getMessage());
        }
    }

    public void saveData() {
        ArrayList<String> lines = new ArrayList<String>();
        for (Car c : cars) {
            lines.add(c.toFileString());
        }
        writeLines("cars.txt", lines);

        lines = new ArrayList<String>();
        for (Customer c : customers) {
            lines.add(c.toFileString());
        }
        writeLines("customers.txt", lines);

        lines = new ArrayList<String>();
        for (Booking b : bookings) {
            lines.add(b.toFileString());
        }
        writeLines("bookings.txt", lines);
    }

    // Some data so that the program is not empty on the first run
    private void loadSampleData() {
        cars.add(new Car("C101", "Swift", "Hatchback", 1500, true));
        cars.add(new Car("C102", "Innova", "SUV", 3000, false));
        cars.add(new Car("C103", "Baleno", "Hatchback", 1800, true));
        cars.add(new Car("C104", "Verna", "Sedan", 2500, true));

        customers.add(new Customer("CU101", "Arun", "9800000001", "DL1001"));
        customers.add(new Customer("CU102", "Rahul", "9800000002", "DL1002"));

        bookings.add(new Booking("B1", "C102", "CU101", 3, "Active"));
        saveData();
    }
}
