UTILITY BILL MANAGEMENT SYSTEM
Java Swing + MySQL + JDBC  (Desktop, Offline, Single User)

=========== RUN KORAR STEPS (IntelliJ IDEA) ===========
1. MySQL chalu koro (XAMPP / MySQL Workbench / MySQL Server).
2. database.sql file ta puro run koro  -> utility_db, table, view, admin user, 5 ta category toiri hobe.
3. MySQL Connector/J download koro: https://dev.mysql.com/downloads/connector/j/
   ("Platform Independent" zip) -> moddhe theke mysql-connector-j-x.x.x.jar ta "lib" folder e rakho.
4. src/DBConnection.java te PASS e tomar MySQL password dao (XAMPP hole faka rakho).
5. IntelliJ: File > Open > UtilityBillSystem folder select koro.
   - JDK na dekhale: File > Project Structure > SDK (JDK 17 ba 21 select koro).
   - jar ta lib folder e thakle auto add hoye jabe. Na hole:
     Project Structure > Libraries > + > Java > lib/mysql-connector-j.jar
6. src/LoginFrame.java open kore main() er pashe green Run button chapo.

Login:  username = admin   password = 1234

=========== PAGE LIST ===========
1. Login Page            - LoginFrame.java
2. Dashboard             - DashboardPanel.java
3. Category Management   - CategoryPanel.java
4. Add Bill Page         - AddBillPanel.java
5. View Bill Page        - ViewBillPanel.java   (Search, Update, Delete, Bill History)
6. Payment Page          - PaymentPanel.java    (Payment + Payment History)
7. Due Management Page   - DuePanel.java        (Unpaid / Partial / Overdue)
8. Monthly Report Page   - ReportPanel.java

Support file: DBConnection.java (JDBC), MainFrame.java (tab window), BasePanel.java, Item.java

=========== STATUS KIVABE ASHE ===========
Paid    = Total payment >= Bill amount
Partial = kichu payment hoyeche, kintu poro nai
Unpaid  = kono payment nai
Overdue = Due date ses, kintu bill ekhono poro nai
(Ei hisab database.sql er bill_view diye automatic hoy)

=========== PROJECT SCOPE ===========
Ei project e ache:
- Admin login
- Utility category (Electricity, Gas, Water, Internet, Other) management
- Bill add, view, search, update, delete
- Payment record (Cash, bKash, Nagad, Bank, Card) ar auto due calculation
- Paid / Unpaid / Partial / Overdue status
- Dashboard summary ar Monthly Report
- Bill ar Payment history

Ei project e nai (future work):
- Online / Web / Mobile version
- Multi-user ba family member wise login
- Password encryption (ekhane simple text password)
- Bill reminder / SMS / Email notification
- PDF ba Excel report export
- Real online payment (bKash API etc.)

Limitation: Offline single-user desktop app, shudhu nijer laptop e MySQL diye chole.

=========== TERMINAL DIYE RUN (optional) ===========
Windows:   javac -cp "lib/*" -d out src/*.java
           java -cp "out;lib/*" LoginFrame
Mac/Linux: javac -cp "lib/*" -d out src/*.java
           java -cp "out:lib/*" LoginFrame
