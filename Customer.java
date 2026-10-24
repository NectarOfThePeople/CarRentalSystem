// Customer.java
// Stores the details of one customer.
public class Customer {
    private String customerId;
    private String name;
    private String phone;
    private String licenseNo;

    public Customer(String customerId, String name, String phone, String licenseNo) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.licenseNo = licenseNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    // Heading line used above the list of customers
    public static String getHeading() {
        return String.format("%-8s %-14s %-12s %s",
                "Cust ID", "Name", "Phone", "License No");
    }

    // One line of text that shows this customer in the list
    public String displayCustomer() {
        return String.format("%-8s %-14s %-12s %s",
                customerId, name, phone, licenseNo);
    }

    // One line of text that is saved in customers.txt
    public String toFileString() {
        return customerId + "," + name + "," + phone + "," + licenseNo;
    }
}
