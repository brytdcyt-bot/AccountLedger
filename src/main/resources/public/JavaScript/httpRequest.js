export async function httpRequest(url, method = 'GET', body = null) {
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' }
    };
    if (body) options.body = JSON.stringify(body);

    try {
        const res = await fetch(url, options);
        if (!res.ok) {
            const text = await res.text();
            throw new Error(`${res.status}: ${text}`);
        }
        return res.json();
    } catch (err) {
        console.error('HTTP Request failed:', err);
        throw err;
    }
}