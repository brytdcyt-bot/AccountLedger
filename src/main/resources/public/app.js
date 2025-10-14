const API_BASE = "http://localhost:8080/api";

// Submit transaction form
document.getElementById("transactionForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const description = document.getElementById("description").value.trim();
    const vendor = document.getElementById("vendor").value.trim();
    const amount = parseFloat(document.getElementById("amount").value);

    if (!description || !vendor || isNaN(amount)) {
        alert("Please fill all fields correctly.");
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/transactions`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ description, vendor, amount })
        });

        if (!res.ok) {
            const err = await res.json();
            throw new Error(err.error || "Unknown server error");
        }

        e.target.reset();
        await loadLedger();
    } catch (error) {
        console.error("Transaction error:", error);
        alert("Failed to add transaction: " + error.message);
    }
});

// Load ledger table
async function loadLedger() {
    try {
        const res = await fetch(`${API_BASE}/transactions`);
        if (!res.ok) throw new Error("Server returned status " + res.status);

        const transactions = await res.json();
        const tbody = document.querySelector("#ledgerTable tbody");
        tbody.innerHTML = "";

        if (transactions.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No transactions yet</td></tr>`;
            return;
        }

        transactions.forEach(t => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${t.date}</td>
                <td>${t.time}</td>
                <td>${t.description}</td>
                <td>${t.vendor}</td>
                <td style="color:${t.amount >= 0 ? '#00ff99' : '#ff4b2b'};">${t.amount.toFixed(2)}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error("Ledger load error:", error);
        const tbody = document.querySelector("#ledgerTable tbody");
        tbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color: #ff4b2b;">Failed to load ledger</td></tr>`;
    }
}

// Auto-load on page ready
window.addEventListener("DOMContentLoaded", loadLedger);
