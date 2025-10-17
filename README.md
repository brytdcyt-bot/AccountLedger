# Accounting Ledger

A minimalistic accounting ledger application in **Java** with a simple **HTML/JS/CSS** front-end.  
This project allows you to track deposits and payments via a REST API and view/filter them in a web-based dashboard.

---

## Features

- Add transactions (deposits or payments) via a simple form.
- View all transactions in a table.
- Filter transactions by:
    - Deposits only
    - Payments only
    - Vendor search
- Saves transactions to a CSV file (`transactions.csv` or `transactions.txt`).
- Logs activity to `ledger.log`.
- Runs a lightweight built-in HTTP server (`/api/transactions`) to serve data to the front-end.

---

## Project Structure

AccountingLedger/
├── src/main/java/com/pluralsight/AccountingLedger.java 
├── src/main/resources/static/
│ ├── index.html
│ ├── app.js
│ └── style.css
├── transactions.csv # Automatically generated CSV file
├── ledger.log # Logs created at runtime
└── README.md



---

## Getting Started

### Prerequisites

- Java 8 or higher
- Any IDE (IntelliJ, Eclipse) or command line terminal
- Web browser for the front-end

### Running the Application

1. **Compile and run the Java application:**

```bash
javac -d out src/main/java/com/pluralsight/AccountingLedger.java
java -cp out com.pluralsight.AccountingLedger
```


---


MIT License © Bryan Barie
