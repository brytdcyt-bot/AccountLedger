# Account Ledger Application

## Overview
The Account Ledger project is a full-stack, professional-grade dashboard to manage, track, and report financial transactions. Built with Java (Spring Boot) backend and modern HTML/CSS/JS frontend.

The dashboard supports:
- Add, view, filter, and report transactions.
- Multi-page interface: Dashboard, Ledger, Settings.
- Professional, subtle cyberpunk UI with glassy components.
- API-driven architecture for future integration.

---

## Features
- **CRUD Transactions**: Add deposits or payments.
- **Reports**: Month-to-date, previous month, year-to-date, previous year.
- **Vendor Filtering**: Quickly see transactions per vendor.
- **Responsive Design**: Works across desktop and mobile.
- **Dark Theme & Glass UI**: Enterprise, modern style.
- **Persistence**: Transactions stored in `transactions.csv`.

---

## Backend
- **Language**: Java
- **Framework**: Spring Boot
- **Entry Point**: `LedgerApi.java` (monolithic API + CSV persistence + static file serving)
- **Data Storage**: CSV (`transactions.csv`)
- **API Endpoints**:
    - `GET /api/transactions` – all transactions
    - `POST /api/transactions` – add transaction
    - `GET /api/transactions/deposits` – deposits only
    - `GET /api/transactions/payments` – payments only
    - `GET /api/transactions/vendor/{vendor}` – filter by vendor
    - `GET /api/transactions/reports/*` – reporting endpoints

---

## Frontend
- **Technologies**: HTML5, CSS3, JavaScript (ES6+)
- **Structure**:
    ```
    public/
    ├─ HTML/
    ├─ CSS/
    ├─ JavaScript/
    └─ JSON/
    ```
- **CSS Modules**:
    - `variables.css` – theme and colors
    - `style.css` – main layout
    - `components.css` – cards, buttons, tables
    - `layout.css` – header, sidebar, content
    - `utilities.css` – helper classes
    - `animations.css` – fade, hover, button presses
    - `theme-dark.css` – optional dark mode

- **JS Modules**:
    - `app.js` – main dashboard logic
    - `api.js` – fetch API requests
    - `loadTransaction.js` – load transactions into table
    - `reports.js` – report calculations
    - `settings.js` – configuration settings
    - `utils.js` – helper functions
    - `validator.js` – form validation
    - `vendors.js` – vendor-related utilities

---

## Installation & Run
1. **Clone repository**:
```bash
 https://github.com/brytdcyt-bot/AccountLedger.git
