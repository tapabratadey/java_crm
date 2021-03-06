## OVERVIEW
- Application Title: Java CRM
- Purpose: Appointment Scheduler built in Java.
- Author: Tapa
- Version: 1.0
- Date: July 7th, 2021


## APPLICATION SPECIFICATIONS
1. IDE 
   * IntelliJ IDEA 2021.1 (Community Edition)
   * Build #IC-211.6693.111, built on April 6, 2021
   * Runtime version: 11.0.10+9-b1341.35 amd64
   * VM: Dynamic Code Evolution 64-Bit Server VM by JetBrains s.r.o.
   * Windows 10 10.0
   * GC: G1 Young Generation, G1 Old Generation
   * Memory: 1024M
   * Cores: 4
   * Kotlin: 211-1.4.32-release-IJ6693.72
2. JDK
   * jdk-16
3. JavaFX                     
   * javafx-sdk-11.0.2
4. MySQL Connector Driver     
   * mysql-connector-java-8.0.22


## DIRECTION TO RUN THE PROGRAM
1. Download the .zip file and import into IntelliJ.
2. Config IntelliJ (Specify the JDK, SDK, and the MySQL connector driver)
   * Go to Edit Configurations in the top right corner.
   * In the VM options field, add "--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml,javafx
   .graphics".
   * PATH_TO_FX is the path to the JavaFX sdk
   * Go to File->Setting->Appearance->Path Variables. Add the PATH_TO_FX variable.
3. Run the Main.java file using the green run button in the top right corner of IntelliJ.
4. Once the project has finished the build process, it will load a login screen, enter "test" for both the username and
the password.
5. Once validated, you'll see the dashboard screen. You will see the following options:
   * Customers Panel
      - You can add/edit/delete customers.
   * Appointments Panel
      - You can add/edit/delete/toggle between weekly and monthly appointments.
   * Reports Panel
      - Displays total number of customer appointments by month and type.
      - Displays appointment schedules of each contact.
      - Displays the operational countries.
   * Appointment Reminders
      - Displays appointment reminders if they are within 15 minutes of the appointment.
   * Logout Button
      - Logs out a user.