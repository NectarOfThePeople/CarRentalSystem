CAR RENTAL SYSTEM (Java Swing + JDBC + MySQL)
=============================================

Two interfaces
--------------
ADMIN    login with Admin ID + PASSWORD   (default:  admin / admin123)
         Add / remove cars, search, register customers, book a car for a customer,
         return a car (bill), car / customer / booking lists, reports.

CUSTOMER login with Customer ID only, NO password   (sample IDs: CU101, CU102)
         View available cars, search cars, book a car, see own bookings.
         (A customer must first be registered by the admin.)

Files
-----
Main.java            starts the program (connects to MySQL, opens the login window)
LoginFrame.java      login window: choose Admin or Customer
BaseFrame.java       common part of the two windows (menu buttons, output area, helpers)
AdminFrame.java      admin window          CustomerFrame.java   customer window
CarRentalSystem.java all the JDBC code (SQL queries)
DBConnection.java    MySQL URL / user / password
Car, Customer, Booking, Bill, Admin .java   data classes
car_rental.sql       creates the database, tables and sample data

Setup (one time)
----------------
1. Install MySQL Server (and optionally MySQL Workbench). Start the MySQL service.

2. Create the database and sample data:
      mysql -u root -p < car_rental.sql
   (or in Workbench: File > Open SQL Script > car_rental.sql > run it)

3. Download the MySQL JDBC driver "Connector/J" (the Platform Independent .zip or .tar.gz)
   from https://dev.mysql.com/downloads/connector/j/ and take the file
   mysql-connector-j-x.y.z.jar from it. Put the jar in a folder named "lib"
   next to the .java files.

4. Open DBConnection.java and set USER and PASSWORD to your MySQL login.

Compile and run
---------------
Windows:
      javac -cp ".;lib/*" *.java
      java  -cp ".;lib/*" Main

Linux / macOS:
      javac -cp ".:lib/*" *.java
      java  -cp ".:lib/*" Main

IntelliJ / Eclipse / NetBeans: copy the .java files into the src folder, add the
mysql-connector-j jar to the project libraries, then run Main.java.

If the program says "Could not connect to the database" check:
  - MySQL is running and car_rental.sql was run
  - USER / PASSWORD in DBConnection.java are correct
  - the connector jar is on the classpath (the -cp part above)

Changing the admin password
---------------------------
The admin password is stored in the admins table. In MySQL run:
      USE car_rental;
      UPDATE admins SET password = 'my_new_password' WHERE admin_id = 'admin';

Bill
----
Total = rent per day x days booked + fine
Fine  = Rs. 500 for every extra day the car is kept (see Bill.java)
