export function saveToLocal(key, data) {
    localStorage.setItem(key, JSON.stringify(data));
}

export function loadFromLocal(key) {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : null;
}
