// Car.java
// Stores the details of one car.
public class Car {
    private String carId;
    private String name;
    private String type;
    private int rentPerDay;
    private boolean available;

    public Car(String carId, String name, String type, int rentPerDay, boolean available) {
        this.carId = carId;
        this.name = name;
        this.type = type;
        this.rentPerDay = rentPerDay;
        this.available = available;
    }

    public String getCarId() {
        return carId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getRentPerDay() {
        return rentPerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Heading line used above the list of cars
    public static String getHeading() {
        return String.format("%-8s %-14s %-12s %-10s %s",
                "Car ID", "Name", "Type", "Rent/Day", "Available");
    }

    // One line of text that shows this car in the list
    public String displayCar() {
        String status;
        if (available) {
            status = "Yes";
        } else {
            status = "No";
        }
        return String.format("%-8s %-14s %-12s %-10d %s",
                carId, name, type, rentPerDay, status);
    }

    // One line of text that is saved in cars.txt
    public String toFileString() {
        return carId + "," + name + "," + type + "," + rentPerDay + "," + available;
    }
}
