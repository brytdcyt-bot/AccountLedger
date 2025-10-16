import { Api } from './api.js';

export async function getVendors() {
    const txs = await Api.getAll();
    return [...new Set(txs.map(t => t.vendor))];
}
