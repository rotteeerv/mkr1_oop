let allProducts = [];
let categoryChart = null;

async function loadProducts() {
    const response = await fetch('/api/products');

    if (!response.ok) {
        showToast('Не вдалося завантажити товари', 'error');
        return;
    }

    allProducts = await response.json();
    renderProducts(allProducts);
    renderCategories(allProducts);
    renderStats(allProducts);
}

function renderProducts(products) {
    const tbody = document.getElementById('productTableBody');
    tbody.innerHTML = '';

    if (products.length === 0) {
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="9">Товари не знайдено</td>`;
        tbody.appendChild(row);
        return;
    }

    products.forEach(product => {
        const row = document.createElement('tr');
        const stockClass = product.quantity <= product.minQuantity ? 'low-stock' : 'ok-stock';

        row.innerHTML = `
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>${product.category}</td>
            <td class="${stockClass}">${product.quantity}</td>
            <td>${product.minQuantity}</td>
            <td>${product.warehouse}</td>
            <td>${product.storageCondition}</td>
            <td>${formatDate(product.lastUpdated)}</td>
            <td class="action-buttons">
                <button onclick="changeQuantity(${product.id})">Коригувати</button>
                <button class="danger-btn" onclick="deleteProduct(${product.id})">Видалити</button>
            </td>
        `;

        tbody.appendChild(row);
    });
}

function renderCategories(products) {
    const container = document.getElementById('categoriesChart');
    if (container) {
        container.innerHTML = '';
    }

    const categoryMap = {};

    products.forEach(product => {
        categoryMap[product.category] = (categoryMap[product.category] || 0) + 1;
    });

    if (container) {
        for (const category in categoryMap) {
            const div = document.createElement('div');
            div.className = 'chart-item';
            div.textContent = `${category}: ${categoryMap[category]} товар(ів)`;
            container.appendChild(div);
        }
    }

    renderChart(categoryMap);
}

function renderChart(categoryMap) {
    const chartCanvas = document.getElementById('categoryChartCanvas');
    if (!chartCanvas || typeof Chart === 'undefined') return;

    const labels = Object.keys(categoryMap);
    const data = Object.values(categoryMap);

    if (categoryChart) {
        categoryChart.destroy();
    }

    categoryChart = new Chart(chartCanvas, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Кількість товарів за категоріями',
                    data: data,
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

function renderStats(products) {
    const totalProductsEl = document.getElementById('totalProducts');
    const lowStockCountEl = document.getElementById('lowStockCount');
    const totalQuantityEl = document.getElementById('totalQuantity');
    const totalCategoriesEl = document.getElementById('totalCategories');

    const totalProducts = products.length;
    const lowStockCount = products.filter(p => p.quantity <= p.minQuantity).length;
    const totalQuantity = products.reduce((sum, p) => sum + p.quantity, 0);
    const totalCategories = new Set(products.map(p => p.category)).size;

    totalProductsEl.textContent = totalProducts;
    lowStockCountEl.textContent = lowStockCount;
    totalQuantityEl.textContent = totalQuantity;
    totalCategoriesEl.textContent = totalCategories;
}

async function loadPurchaseList() {
    const response = await fetch('/api/products/purchase-list');

    if (!response.ok) {
        showToast('Не вдалося завантажити список на закупівлю', 'error');
        return;
    }

    const items = await response.json();
    const list = document.getElementById('purchaseList');
    list.innerHTML = '';

    if (items.length === 0) {
        const li = document.createElement('li');
        li.textContent = 'Усі товари мають достатній залишок.';
        list.appendChild(li);
        return;
    }

    items.forEach(item => {
        const li = document.createElement('li');
        li.textContent = `${item.productName}: залишок ${item.currentQuantity}, мінімум ${item.minQuantity}, рекомендовано замовити ${item.recommendedOrderQuantity}`;
        list.appendChild(li);
    });
}

async function changeQuantity(productId) {
    const value = prompt('Введіть нову кількість:');

    if (value === null) return;

    const quantity = parseInt(value);

    if (isNaN(quantity) || quantity < 0) {
        showToast('Некоректна кількість', 'error');
        return;
    }

    const response = await fetch(`/api/products/${productId}/quantity?quantity=${quantity}`, {
        method: 'PUT'
    });

    if (!response.ok) {
        showToast('Помилка під час оновлення кількості', 'error');
        return;
    }

    showToast('Кількість успішно оновлено', 'success');
    await refreshAll();
}

async function deleteProduct(productId) {
    const confirmed = confirm(`Видалити товар з ID ${productId}?`);

    if (!confirmed) return;

    const response = await fetch(`/api/products/${productId}`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        showToast('Не вдалося видалити товар', 'error');
        return;
    }

    showToast('Товар успішно видалено', 'success');
    await refreshAll();
}

document.getElementById('searchInput').addEventListener('input', function () {
    const value = this.value.toLowerCase().trim();

    const filtered = allProducts.filter(product =>
        product.name.toLowerCase().includes(value) ||
        product.category.toLowerCase().includes(value) ||
        product.warehouse.toLowerCase().includes(value)
    );

    renderProducts(filtered);
});

document.getElementById('inventoryForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const productId = document.getElementById('inventoryProductId').value;
    const quantity = parseInt(document.getElementById('inventoryQuantity').value);

    if (isNaN(quantity) || quantity < 0) {
        showToast('Фактична кількість не може бути від’ємною', 'error');
        return;
    }

    const response = await fetch(`/api/products/${productId}/quantity?quantity=${quantity}`, {
        method: 'PUT'
    });

    if (!response.ok) {
        showToast('Не вдалося зберегти результат інвентаризації', 'error');
        return;
    }

    showToast('Результат інвентаризації успішно збережено', 'success');
    this.reset();
    await refreshAll();
});

document.getElementById('invoiceForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const invoice = {
        productId: Number(document.getElementById('invoiceProductId').value),
        receivedQuantity: Number(document.getElementById('invoiceQuantity').value),
        supplier: document.getElementById('invoiceSupplier').value.trim(),
        date: document.getElementById('invoiceDate').value
    };

    if (invoice.receivedQuantity <= 0) {
        showToast('Кількість приходу має бути більшою за 0', 'error');
        return;
    }

    const response = await fetch('/api/products/invoice', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(invoice)
    });

    if (!response.ok) {
        showToast('Не вдалося обробити накладну', 'error');
        return;
    }

    showToast('Накладну успішно оброблено', 'success');
    this.reset();
    await refreshAll();
});

document.getElementById('builderDemoBtn').addEventListener('click', async function () {
    const response = await fetch('/api/orders/demo');

    if (!response.ok) {
        showToast('Не вдалося створити демонстраційне замовлення', 'error');
        return;
    }

    const data = await response.json();
    document.getElementById('builderResult').textContent = JSON.stringify(data, null, 2);
    showToast('Демонстраційне замовлення створено', 'success');
});

document.getElementById('addProductForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const newProduct = {
        name: document.getElementById('newProductName').value.trim(),
        category: document.getElementById('newProductCategory').value.trim(),
        quantity: Number(document.getElementById('newProductQuantity').value),
        minQuantity: Number(document.getElementById('newProductMinQuantity').value),
        warehouse: document.getElementById('newProductWarehouse').value.trim(),
        storageCondition: document.getElementById('newProductStorageCondition').value.trim()
    };

    if (!newProduct.name || !newProduct.category || !newProduct.warehouse || !newProduct.storageCondition) {
        showToast('Заповніть усі поля нового товару', 'error');
        return;
    }

    if (newProduct.quantity < 0 || newProduct.minQuantity < 0) {
        showToast('Кількість не може бути від’ємною', 'error');
        return;
    }

    const response = await fetch('/api/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newProduct)
    });

    if (!response.ok) {
        showToast('Не вдалося додати товар', 'error');
        return;
    }

    showToast('Товар успішно додано', 'success');
    this.reset();
    await refreshAll();
});

function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;

    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('show');
    }, 10);

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function formatDate(dateString) {
    if (!dateString) return '-';

    const date = new Date(dateString);
    if (isNaN(date.getTime())) return dateString;

    return date.toLocaleString('uk-UA');
}

async function refreshAll() {
    await loadProducts();
    await loadPurchaseList();
}

refreshAll();