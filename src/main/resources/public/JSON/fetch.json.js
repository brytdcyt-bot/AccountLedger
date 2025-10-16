// fetch.json.js
export const API_CONFIG = {
    base: "/api/transactions",
    endpoints: {
        all: "/",
        deposits: "/deposits",
        payments: "/payments",
        vendor: "/vendor/:vendor",
        reports: {
            monthToDate: "/reports/month-to-date",
            previousMonth: "/reports/previous-month",
            yearToDate: "/reports/year-to-date",
            previousYear: "/reports/previous-year"
        }
    },
    payloadTemplates: {
        transaction: {
            description: "Sample description",
            vendor: "Sample Vendor",
            amount: 0
        }
    }
};