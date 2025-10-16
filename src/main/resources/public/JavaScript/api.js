const API_BASE = "/api/transactions";

export const Api = {
    async getAll() {
        return fetchJSON(API_BASE);
    },
    async getDeposits() {
        return fetchJSON(`${API_BASE}/deposits`);
    },
    async getPayments() {
        return fetchJSON(`${API_BASE}/payments`);
    },
    async getByVendor(vendor) {
        return fetchJSON(`${API_BASE}/vendor/${encodeURIComponent(vendor)}`);
    },
    async addTransaction(tx) {
        return fetchJSON(API_BASE, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(tx)
        });
    },
    async getReports(period) {
        return fetchJSON(`${API_BASE}/reports/${period}`);
    }
};

async function fetchJSON(url, options = {}) {
    try {
        const res = await fetch(url, options);
        if (!res.ok) throw new Error(`${res.status}: ${await res.text()}`);
        return await res.json();
    } catch (err) {
        console.error("API Error:", err);
        throw err;
    }
}