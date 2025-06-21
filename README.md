# 📚 Library Management System (Desktop App - Java Swing)

## 📌 Description
A desktop-based application for managing library operations such as user management, book catalog, and borrowing functionality. This application is built using Java Swing (JFrame UI components) and connects to an Oracle database using JDBC. Designed and developed using NetBeans IDE.

---

## 🚀 How to Run the Project

### 🛠 Prerequisites
- Java JDK 8 or later
- Oracle Database (XE or higher)
- NetBeans IDE
- ojdbc17.jar (Oracle JDBC driver) – already included in the `lib/` folder

### 🧩 Steps to Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/library-management-system-desktop.git
   ```

2. **Open the project in NetBeans**:
   - Open NetBeans
   - File → Open Project → Select `Library_Management_System` folder

3. **Add the JDBC driver**:
   - Right-click on the project → Properties → Libraries → Add JAR/Folder
   - Select `lib/ojdbc17.jar`

4. **Set up the Oracle database**:
   - Open SQL*Plus or Oracle SQL Developer
   - Login:
     ```sql
     CONNECT system/YOUR_PASSWORD;
     ```
   - Run the setup script:
     ```sql
     @sql/setup.sql
     ```
   - Commit the data:
     ```sql
     COMMIT;
     ```

5. **Update DB credentials in `src/db/DBConnection.java`**:
   ```java
   private static final String USER = "your_oracle_username";
   private static final String PASSWORD = "your_oracle_password";
   ```

6. **Run the project** in NetBeans.

---

## 📁 Folder Structure

```
Library_Management_System/
├── src/                    # Java source code (UI, DB, logic)
├── nbproject/              # NetBeans project settings (no private folder)
├── lib/
│   └── ojdbc17.jar         # Oracle JDBC driver
├── sql/
│   └── setup.sql           # DB schema and sample data
├── .gitignore              # Git ignore rules
├── README.md               # Project documentation
```

---

## 🧾 Database

- **DB**: Oracle
- **Connection**: JDBC (`oracle.jdbc.driver.OracleDriver`)
- **Port**: 1522 (default can be changed in `DBConnection.java`)
- **Setup Script**: `sql/setup.sql`
- **Tables**: Users, Books, Transactions, etc.
- **Sample Data**: 5 rows per table inserted by script

---

## 💻 Technologies Used

- **Java** – Application logic
- **Java Swing** – Desktop GUI
- **JDBC** – Database connectivity
- **Oracle Database** – Backend storage
- **NetBeans IDE** – Project development

---

## 📝 Notes

- Make sure Oracle DB is running before launching the app
- Don’t forget to run `COMMIT;` after executing the setup SQL script
- Credentials in `DBConnection.java` should be replaced before running
- You can modify `setup.sql` to add more users, books, etc.

---

## 🙌 Acknowledgements

- Oracle Database XE
- NetBeans IDE
- Java Swing and the open-source developer community

---

## 📬 Contact

For queries or contributions, feel free to open issues or submit a pull request!
