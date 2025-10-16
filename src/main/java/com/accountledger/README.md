# Account Ledger Dashboard

A **full-featured Account Ledger Dashboard** for managing transactions, generating financial reports, and monitoring vendors. Designed with an **executive, professional style**.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
- [JSON & Mock Data](#json--mock-data)
- [CSS & Styling](#css--styling)
- [JavaScript Modules](#javascript-modules)
- [Contributing](#contributing)
- [License](#license)

---

## Project Overview

The Account Ledger Dashboard is a modular web application with a Java backend API and a rich JavaScript front-end. It supports:

- Transaction tracking (deposits and payments)
- Ledger summaries with totals
- Vendor-specific filtering
- Month-to-date, previous month, year-to-date, and previous year reports
- Multi-page dashboard (`ledger.html`, `reports.html`, `settings.html`, `vendors.html`)
- Enterprise-grade UI with card-based, glassy layouts
- Synthwave/Cyberpunk-inspired color palette

---

## Features

- **Add / Edit Transactions**: Positive = deposits, Negative = payments
- **Real-time Ledger**: Auto-calculated totals and dynamic table updates
- **Reports**: Month-to-date, previous month, year-to-date, previous year
- **Vendor Management**: Filter transactions per vendor
- **Customizable UI**: Light/dark themes, animations, responsive layout
- **Offline-ready**: Mock transactions for demos
- **Fully Modular JS & CSS**: Easily extendable

---

## Technologies

- **Backend:** Java (Spring-style REST API)
- **Frontend:** HTML5, CSS3, JS ES6 modules
- **CSS:** Variables, components, layout, theme, utilities
- **JSON:** Config, mock data, fetch setup
- **Build Tool:** Maven for backend, plain static resources for frontend
- **Browsers:** Chrome, Edge, Firefox (modern)

---

## Project Structure

resources/

└── public/

├── CSS/
│ ├── animations.css
│ ├── components.css
│ ├── layout.css
│ ├── style.css
│ ├── theme-dark.css
│ ├── utilities.css
│ └── variables.css

├── HTML/
│ ├── index.html
│ ├── ledger.html
│ ├── reports.html
│ ├── settings.html
│ ├── vendors.html
│ ├── filters.html
│ └── charts.html

├── JavaScript/
│ ├── api.js
│ ├── app.js
│ ├── auth.js
│ ├── charts.js
│ ├── filters.js
│ ├── httpRequest.js
│ ├── init.js
│ ├── loadTransaction.js
│ ├── notifications.js
│ ├── reports.js
│ ├── script.js
│ ├── server.js
│ ├── settings.js
│ ├── storage.js
│ ├── theme.js
│ ├── ui.js
│ ├── utils.js
│ ├── validator.js
│ └── vendors.js

├── JSON/
│ ├── config.json
│ ├── data.json
│ ├── fetch.json
│ ├── mockTransactions.json
│ └── package.json

└── requests/
└── ledger.http


---

## Installation

1. Clone the repository:

```bash
git clone https://github.com/brytdcyt-bot/AccountLedger.git