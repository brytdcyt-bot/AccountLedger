// fetch.json.js
import mockData from "../JSON/mockData.json" assert { type: "json" };

export const API_CONFIG = {
    base: "/api/transactions",
    endpoints: {
        all: "/",
        deposits: "/deposits",
        payments: "/payments",
        vendor: (vendor) => `/vendor/${encodeURIComponent(vendor)}`,
        reports: {
            monthToDate: "/reports/month-to-date",
            previousMonth: "/reports/previous-month",
            yearToDate: "/reports/year-to-date",
            previousYear: "/reports/previous-year"
        }
    },
    payloadTemplates: {
        transaction: {
            date: new Date().toISOString().split("T")[0],
            time: new Date().toISOString().split("T")[1].split(".")[0],
            description: "Sample description",
            vendor: "Sample Vendor",
            amount: 0
        }
    }
};

// -----------------------------------------
// Runtime Mode Detection
// -----------------------------------------
const USE_MOCK = window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1";

console.info(`[Ledger API] Mode: ${USE_MOCK ? "Mock Data" : "Live API"}`);

// -----------------------------------------
// Fetch Wrapper
// -----------------------------------------
async function safeFetch(url, options = {}) {
    try {
        const res = await fetch(url, options);
        if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`);
        return await res.json();
    } catch (err) {
        console.warn(`[Ledger API] Fetch failed for ${url}:`, err);
        throw err;
    }
}

// -----------------------------------------
// API Service Functions
// -----------------------------------------
export const LedgerAPI = {
    async getAll() {
        if (USE_MOCK) return mockData.transactions;
        return await safeFetch(`${API_CONFIG.base}${API_CONFIG.endpoints.all}`);
    },

    async getDeposits() {
        if (USE_MOCK) return mockData.transactions.filter(tx => tx.amount > 0);
        return await safeFetch(`${API_CONFIG.base}${API_CONFIG.endpoints.deposits}`);
    },

    async getPayments() {
        if (USE_MOCK) return mockData.transactions.filter(tx => tx.amount < 0);
        return await safeFetch(`${API_CONFIG.base}${API_CONFIG.endpoints.payments}`);
    },

    async getVendor(vendor) {
        if (USE_MOCK) return mockData.transactions.filter(tx => tx.vendor.toLowerCase() === vendor.toLowerCase());
        return await safeFetch(`${API_CONFIG.base}${API_CONFIG.endpoints.vendor(vendor)}`);
    },

    async getReport(type = "monthToDate") {
        if (USE_MOCK) return mockData.summary;
        const endpoint = API_CONFIG.endpoints.reports[type];
        if (!endpoint) throw new Error(`Unknown report type: ${type}`);
        return await safeFetch(`${API_CONFIG.base}${endpoint}`);
    },

    async addTransaction(transaction) {
        const payload = { ...API_CONFIG.payloadTemplates.transaction, ...transaction };
        if (USE_MOCK) {
            console.log("[Ledger API] Mock add:", payload);
            mockData.transactions.push(payload);
            return payload;
        }

        return await safeFetch(`${API_CONFIG.base}${API_CONFIG.endpoints.all}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
    }
};