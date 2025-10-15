const API_BASE = "http://localhost:8080/api";
const POS_COLOR = "var(--accent-color)";
const NEG_COLOR = "var(--danger-color)";

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

async function loadLedger() {
    try {
        const res = await fetch(`${API_BASE}/transactions`);
        if (!res.ok) throw new Error("Server returned status " + res.status);

        const transactions = await res.json();
        const tbody = document.querySelector("#ledgerTable tbody");
        tbody.innerHTML = "";

        if (transactions.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No transactions yet</td></tr>`;
            document.getElementById("totalAmount").textContent = "0.00";
            return;
        }

        let totalAmount = 0;

        transactions.forEach(t => {
            totalAmount += t.amount;
            const row = document.createElement("tr");
            row.style.opacity = 0;
            row.innerHTML = `
                <td>${t.date}</td>
                <td>${t.time}</td>
                <td>${t.description}</td>
                <td>${t.vendor}</td>
                <td style="color:${t.amount >= 0 ? POS_COLOR : NEG_COLOR};">${t.amount.toFixed(2)}</td>
            `;
            tbody.appendChild(row);
            requestAnimationFrame(() => row.style.transition = "opacity 0.3s ease-in-out");
            requestAnimationFrame(() => row.style.opacity = 1);
        });

        document.getElementById("totalAmount").textContent = totalAmount.toFixed(2);

    } catch (error) {
        console.error("Ledger load error:", error);
        const tbody = document.querySelector("#ledgerTable tbody");
        tbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color: ${NEG_COLOR};">Failed to load ledger</td></tr>`;
    }
}

window.addEventListener("DOMContentLoaded", loadLedger);
