CAR RENTAL SYSTEM (Java Swing)
==============================

Files
-----
Main.java            - starts the program
LoginFrame.java      - admin login window
MainFrame.java       - main window (menu buttons + output area)
CarRentalSystem.java - all the logic + saving/loading of data
Car.java, Customer.java, Booking.java, Bill.java, Admin.java - data classes

How to run (needs JDK 8 or later)
---------------------------------
1. Put all the .java files in one folder.
2. Open a terminal / command prompt in that folder.
3. Compile :  javac *.java
4. Run     :  java Main

(In IntelliJ / Eclipse / NetBeans: create a new Java project, copy the files
into the src folder and run Main.java.)

Login
-----
Admin ID : admin
Password : admin123

Data files
----------
The program creates cars.txt, customers.txt and bookings.txt in the folder
where it is run. Sample data is added the first time. Delete these three
files to start again with the sample data.

Bill
----
Total = rent per day x days booked + fine
Fine  = Rs. 500 for every extra day the car is kept (see Bill.java)
