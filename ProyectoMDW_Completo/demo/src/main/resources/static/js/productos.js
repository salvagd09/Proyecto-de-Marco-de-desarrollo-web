/**
 * productos.js - Lógica para el módulo de Productos
 * Gestiona listados, filtros, creación, edición y cambios de estado
 * contra el backend de Spring Boot mediante fetch + JSON.
 */

const API_PRODUCTOS = '/api/productos';
const API_CATEGORIAS = '/api/categorias';
const TOKEN_KEY = 'jwtToken';
const DEBOUNCE_DELAY = 300;
const PAGE_SIZE = 8;

let productosActuales = [];
let productoEnEdicion = null;
let debounceTimer = null;
let estadoTabSeleccionado = 'Activo';
let currentPage = 1;

document.addEventListener('DOMContentLoaded', () => {
    inicializarEventos();
    refrescarProductos();
    cargarEstadisticas();
});

function inicializarEventos() {
    const modalCrear = document.getElementById('modalCrearProducto');
    if (modalCrear) {
        modalCrear.addEventListener('show.bs.modal', () => {
            const skuInput = document.getElementById('sku');
            if (skuInput) {
                skuInput.value = generarSKUUnico();
            }
            cargarCategorias();
        });
    }

    const btnNuevoProducto = document.querySelector('[data-bs-target="#modalCrearProducto"]');
    if (btnNuevoProducto) {
        btnNuevoProducto.addEventListener('click', () => setTimeout(() => cargarCategorias(), 100));
    }

    const filtroCategoria = document.getElementById('filtroCategoria');
    if (filtroCategoria) {
        cargarCategorias(filtroCategoria, null, 'Todas las categorías');
        filtroCategoria.addEventListener('change', refrescarProductos);
    }

    ['filtroStock', 'filtroPrecio'].forEach(id => {
        const select = document.getElementById(id);
        if (select) {
            select.addEventListener('change', refrescarProductos);
        }
    });

    const filtroEstado = document.getElementById('filtroEstado');
    if (filtroEstado) {
        filtroEstado.addEventListener('change', () => {
            if (filtroEstado.value) {
                estadoTabSeleccionado = filtroEstado.value;
            } else {
                filtroEstado.value = estadoTabSeleccionado;
            }
            actualizarTabsEstadoUI();
            refrescarProductos();
        });
    }

    const clearFilters = document.getElementById('clearFilters');
    if (clearFilters) {
        clearFilters.addEventListener('click', limpiarFiltros);
    }

    const inputBusqueda = document.getElementById('buscarProducto');
    if (inputBusqueda) {
        inputBusqueda.addEventListener('input', () => debounce(refrescarProductos, DEBOUNCE_DELAY));
    }

    const btnGuardarProducto = document.getElementById('btnGuardarProducto');
    if (btnGuardarProducto) {
        btnGuardarProducto.addEventListener('click', crearProducto);
    }

    const btnActualizarProducto = document.getElementById('btnActualizarProducto');
    if (btnActualizarProducto) {
        btnActualizarProducto.addEventListener('click', actualizarProductoDesdeModal);
    }

    configurarTabsEstado();
}

async function refrescarProductos() {
    try {
        limpiarError();
        const query = construirQueryFiltros();
        const url = query ? `${API_PRODUCTOS}?${query}` : API_PRODUCTOS;
        const response = await fetchConToken(url, { method: 'GET' });

        if (!response.ok) {
            throw new Error('No se pudieron cargar los productos.');
        }

        const productos = await response.json();
        productosActuales = Array.isArray(productos) ? productos : [];
        currentPage = 1;
        renderProductos();
    } catch (error) {
        console.error('❌ Error al refrescar productos:', error);
        mostrarError(error.message || 'No se pudo cargar la lista de productos.');
    }
}

function construirQueryFiltros() {
    const params = new URLSearchParams();

    const estado = obtenerValorSelect('filtroEstado');
    if (estado) {
        params.append('estado', estado);
    }

    const stock = obtenerValorSelect('filtroStock');
    if (stock) {
        params.append('stock', stock);
    }

    const precio = obtenerValorSelect('filtroPrecio');
    if (precio) {
        params.append('precio', precio);
    }

    const categoriaId = obtenerValorSelect('filtroCategoria');
    if (categoriaId) {
        params.append('categoriaId', categoriaId);
    }

    const termino = document.getElementById('buscarProducto');
    if (termino && termino.value.trim() !== '') {
        params.append('search', termino.value.trim());
    }

    return params.toString();
}

function limpiarFiltros() {
    estadoTabSeleccionado = 'Activo';
    currentPage = 1;
    setSelectValue('filtroEstado', estadoTabSeleccionado);
    setSelectValue('filtroStock', '');
    setSelectValue('filtroPrecio', '');
    setSelectValue('filtroCategoria', '');

    const buscador = document.getElementById('buscarProducto');
    if (buscador) {
        buscador.value = '';
    }

    actualizarTabsEstadoUI();
    refrescarProductos();
}

function mostrarProductos(productos) {
    const tableBody = obtenerTablaProductos();
    if (!tableBody) {
        return;
    }

    tableBody.innerHTML = '';

    if (!Array.isArray(productos) || productos.length === 0) {
        const row = document.createElement('tr');
        const cell = document.createElement('td');
        cell.colSpan = 8;
        cell.className = 'text-center text-muted';
        cell.textContent = 'No se encontraron productos para los filtros seleccionados.';
        row.appendChild(cell);
        tableBody.appendChild(row);
        return;
    }

    productos.forEach(producto => {
        tableBody.appendChild(crearFilaProducto(producto));
    });
}

function renderProductos() {
    const total = productosActuales.length;
    const totalPages = Math.max(1, Math.ceil(total / PAGE_SIZE));

    if (total === 0) {
        currentPage = 1;
        mostrarProductos([]);
        actualizarPaginacionUI(total, totalPages, 0, 0);
        return;
    }

    if (currentPage > totalPages) {
        currentPage = totalPages;
    }

    const startIndex = (currentPage - 1) * PAGE_SIZE;
    const endIndex = Math.min(startIndex + PAGE_SIZE, total);
    const paginaProductos = productosActuales.slice(startIndex, endIndex);

    mostrarProductos(paginaProductos);
    actualizarPaginacionUI(total, totalPages, startIndex, endIndex);
}

function actualizarPaginacionUI(total, totalPages, startIndex, endIndex) {
    const container = document.getElementById('paginacionContainer');
    const resumen = document.getElementById('paginacionResumen');
    const lista = document.getElementById('paginacionLista');

    if (!container || !resumen || !lista) {
        return;
    }

    lista.innerHTML = '';

    if (total === 0) {
        resumen.textContent = 'Sin productos para mostrar';
        return;
    }

    resumen.textContent = `Mostrando ${startIndex + 1}–${endIndex} de ${total}`;

    const prevLi = document.createElement('li');
    prevLi.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    const prevBtn = document.createElement('button');
    prevBtn.className = 'page-link';
    prevBtn.type = 'button';
    prevBtn.textContent = 'Anterior';
    if (currentPage > 1) {
        prevBtn.addEventListener('click', () => cambiarPagina(currentPage - 1));
    }
    prevLi.appendChild(prevBtn);
    lista.appendChild(prevLi);

    for (let page = 1; page <= totalPages; page++) {
        const li = document.createElement('li');
        li.className = `page-item ${page === currentPage ? 'active' : ''}`;
        const btn = document.createElement('button');
        btn.className = 'page-link';
        btn.type = 'button';
        btn.textContent = page;
        if (page !== currentPage) {
            btn.addEventListener('click', () => cambiarPagina(page));
        }
        li.appendChild(btn);
        lista.appendChild(li);
    }

    const nextLi = document.createElement('li');
    nextLi.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
    const nextBtn = document.createElement('button');
    nextBtn.className = 'page-link';
    nextBtn.type = 'button';
    nextBtn.textContent = 'Siguiente';
    if (currentPage < totalPages) {
        nextBtn.addEventListener('click', () => cambiarPagina(currentPage + 1));
    }
    nextLi.appendChild(nextBtn);
    lista.appendChild(nextLi);
}

function cambiarPagina(page) {
    const totalPages = Math.max(1, Math.ceil(productosActuales.length / PAGE_SIZE));
    const paginaNormalizada = Math.min(Math.max(page, 1), totalPages);

    if (paginaNormalizada === currentPage) {
        return;
    }

    currentPage = paginaNormalizada;
    renderProductos();
}

function obtenerTablaProductos() {
    return document.querySelector('#productos-body') || document.querySelector('#tablaProductos tbody');
}

function crearFilaProducto(producto) {
    const row = document.createElement('tr');
    row.dataset.productId = producto.idProducto;

    const categoriaNombre = producto.categoria && producto.categoria.nombreCategoria ? producto.categoria.nombreCategoria : 'Sin categoría';
    const precioNumber = Number(producto.precio);
    const precioFormateado = Number.isFinite(precioNumber) ? precioNumber.toFixed(2) : (producto.precio ?? 0);

    const esActivo = producto.estado === 'Activo';
    const estadoBadge = esActivo ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger';
    const iconoAccion = esActivo ? 'bi-trash' : 'bi-check2';
    const claseAccion = esActivo ? 'btn-outline-danger' : 'btn-outline-success';
    const tituloAccion = esActivo ? 'Desactivar producto' : 'Activar producto';

    row.innerHTML = `
        <td>${producto.sku ?? ''}</td>
        <td>
            <div class="d-flex align-items-center gap-2">
                <div>
                    <div class="fw-semibold">${producto.nombreProducto ?? ''}</div>
                    <div class="small text-muted">${producto.descripcion ?? ''}</div>
                </div>
            </div>
        </td>
        <td><span class="badge bg-secondary-subtle text-secondary">${categoriaNombre}</span></td>
        <td><span class="badge bg-info-subtle text-info">${producto.talla ?? ''} / ${producto.color ?? ''}</span></td>
        <td class="text-end">${producto.stockActual ?? 0}</td>
        <td class="text-end">S/ ${precioFormateado}</td>
        <td>
            <span class="badge ${estadoBadge}">
                <i class="bi bi-check2-circle me-1"></i>${producto.estado ?? ''}
            </span>
        </td>
        <td class="d-flex gap-2">
            <button class="btn btn-sm btn-outline-info" data-bs-toggle="modal" data-bs-target="#modalVerProducto" type="button">
                <i class="bi bi-eye"></i>
            </button>
            <button class="btn btn-sm btn-outline-secondary btn-edit-producto" data-bs-toggle="modal" data-bs-target="#modalEditarProducto" type="button">
                <i class="bi bi-pencil"></i>
            </button>
            <button class="btn btn-sm ${claseAccion} btn-toggle-estado" type="button" title="${tituloAccion}">
                <i class="bi ${iconoAccion}"></i>
            </button>
        </td>
    `;

    const editButton = row.querySelector('.btn-edit-producto');
    if (editButton) {
        editButton.addEventListener('click', () => prepararEdicionProducto(producto.idProducto));
    }

    const toggleButton = row.querySelector('.btn-toggle-estado');
    if (toggleButton) {
        toggleButton.addEventListener('click', () => cambiarEstadoProducto(producto.idProducto, producto.estado));
    }

    return row;
}

async function crearProducto() {
    const form = document.getElementById('formCrearProducto');
    if (!form) {
        return;
    }

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const categoriaSeleccionada = document.getElementById('idCategoria').value;

    const productoData = {
        sku: obtenerValorInput('sku'),
        nombreProducto: obtenerValorInput('nombreProducto'),
        descripcion: obtenerValorInput('descripcion'),
        stockActual: parseEntero(obtenerValorInput('stockActual')),
        precio: parseDecimal(obtenerValorInput('precio')),
        talla: normalizarTexto(obtenerValorInput('talla')),
        color: normalizarTexto(obtenerValorInput('color')),
        estado: obtenerValorInput('estado'),
        imagenProducto: normalizarTexto(obtenerValorInput('imagenProducto')),
        categoriaId: categoriaSeleccionada ? parseInt(categoriaSeleccionada, 10) : null
    };

    try {
        const response = await fetchConToken(API_PRODUCTOS, {
            method: 'POST',
            body: JSON.stringify(productoData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => ({ error: 'Error al crear producto.' }));
            throw new Error(error.error || 'No se pudo crear el producto.');
        }

        await response.json();
        alert('Producto creado exitosamente');

        const modalElement = document.getElementById('modalCrearProducto');
        const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
        modal.hide();

        form.reset();

        await refrescarProductos();
        await cargarEstadisticas();
    } catch (error) {
        console.error('❌ Error al crear producto:', error);
        alert(error.message || 'Error al crear producto.');
    }
}

async function actualizarProductoDesdeModal() {
    if (!productoEnEdicion) {
        alert('Selecciona un producto para editar.');
        return;
    }

    const form = document.getElementById('formEditarProducto');
    if (!form) {
        return;
    }

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const productoData = {
        sku: obtenerValorInput('editarSku'),
        nombreProducto: obtenerValorInput('editarNombreProducto'),
        descripcion: obtenerValorInput('editarDescripcion'),
        stockActual: parseEntero(obtenerValorInput('editarStockActual')),
        precio: parseDecimal(obtenerValorInput('editarPrecio')),
        talla: normalizarTexto(obtenerValorInput('editarTalla')),
        color: normalizarTexto(obtenerValorInput('editarColor')),
        estado: obtenerValorInput('editarEstado'),
        imagenProducto: normalizarTexto(obtenerValorInput('editarImagenProducto')),
        categoriaId: parseEntero(obtenerValorInput('editarCategoria'))
    };

    const idProducto = productoEnEdicion.idProducto;

    try {
        const response = await fetchConToken(`${API_PRODUCTOS}/${idProducto}`, {
            method: 'PUT',
            body: JSON.stringify(productoData)
        });

        if (!response.ok) {
            const error = await response.json().catch(() => ({ error: 'Error al actualizar producto.' }));
            throw new Error(error.error || 'No se pudo actualizar el producto.');
        }

        await response.json();
        alert('Producto actualizado correctamente');

        await refrescarProductos();
        await cargarEstadisticas();

        const modalElement = document.getElementById('modalEditarProducto');
        const modal = bootstrap.Modal.getInstance(modalElement);
        if (modal) {
            modal.hide();
        }

        productoEnEdicion = null;
    } catch (error) {
        console.error('❌ Error al actualizar producto:', error);
        alert(error.message || 'Error al actualizar producto.');
    }
}

async function cambiarEstadoProducto(idProducto, estadoActual) {
    const nuevoEstado = estadoActual === 'Activo' ? 'Inactivo' : 'Activo';

    try {
        const response = await fetchConToken(`${API_PRODUCTOS}/${idProducto}/estado`, {
            method: 'PATCH',
            body: JSON.stringify({ estado: nuevoEstado })
        });

        if (!response.ok) {
            const error = await response.json().catch(() => ({ error: 'No se pudo cambiar el estado.' }));
            throw new Error(error.error || 'No se pudo cambiar el estado del producto.');
        }

        await response.json();
        await refrescarProductos();
        await cargarEstadisticas();

        const mensaje = nuevoEstado === 'Activo' ? 'Producto activado' : 'Producto desactivado';
        alert(mensaje);
    } catch (error) {
        console.error('❌ Error al cambiar estado:', error);
        alert(error.message || 'Error al cambiar el estado del producto.');
    }
}

function prepararEdicionProducto(idProducto) {
    const producto = productosActuales.find(p => p.idProducto === idProducto);
    if (!producto) {
        alert('No se pudo cargar la información del producto seleccionado.');
        return;
    }

    productoEnEdicion = producto;

    setInputValue('editarIdProducto', producto.idProducto);
    setInputValue('editarSku', producto.sku);
    setInputValue('editarNombreProducto', producto.nombreProducto);
    setInputValue('editarDescripcion', producto.descripcion);
    setInputValue('editarPrecio', producto.precio);
    setInputValue('editarStockActual', producto.stockActual);
    setSelectValue('editarTalla', producto.talla);
    setInputValue('editarColor', producto.color);
    setSelectValue('editarEstado', producto.estado || 'Activo');
    setInputValue('editarImagenProducto', producto.imagenProducto);

    const categoriaSelect = document.getElementById('editarCategoria');
    const categoriaId = producto.categoria && producto.categoria.idCategoria != null ? producto.categoria.idCategoria : null;

    cargarCategorias(categoriaSelect, categoriaId, 'Seleccionar categoría');
}

// Mostrar mensajes de error en la interfaz
function mostrarError(message) {
    const errorElement = document.getElementById('productos-error');
    if (!errorElement) {
        alert(message);
        return;
    }
    errorElement.textContent = message;
    errorElement.style.display = 'block';
}

function limpiarError() {
    const errorElement = document.getElementById('productos-error');
    if (errorElement) {
        errorElement.textContent = '';
        errorElement.style.display = 'none';
    }
}

async function cargarEstadisticas() {
    try {
        const response = await fetchConToken(`${API_PRODUCTOS}/estadisticas`, { method: 'GET' });
        if (!response.ok) {
            throw new Error('No se pudieron cargar las estadísticas de productos. Verifique la autenticación.');
        }
        const estadisticas = await response.json();
        mostrarEstadisticas(estadisticas);
    } catch (error) {
        console.error('❌ Error al cargar estadísticas:', error);
        mostrarError(error.message);
    }
}

function mostrarEstadisticas(estadisticas) {
    document.getElementById('totalProductos').textContent = estadisticas.totalProductos;
    document.getElementById('productosActivos').textContent = estadisticas.productosActivos;
    document.getElementById('stockBajo').textContent = estadisticas.stockBajo;
    const inactivos = document.getElementById('productosInactivos');
    if (inactivos) {
        inactivos.textContent = estadisticas.productosInactivos;
    }
}

async function cargarCategorias(selectElement, selectedId, placeholderText) {
    const select = selectElement || document.getElementById('idCategoria');
    if (!select) {
        console.warn('No se encontró el elemento select para categorías');
        return;
    }

    const selectedValue = selectedId != null ? String(selectedId) : '';
    const placeholder = placeholderText || 'Seleccionar categoría';

    try {
        select.innerHTML = '<option value="">Cargando categorías...</option>';

        const response = await fetchConToken(API_CATEGORIAS, { method: 'GET' });

        if (!response.ok) {
            throw new Error('No autorizado. Por favor, inicie sesión nuevamente.');
        }

        const categorias = await response.json();

        if (!Array.isArray(categorias) || categorias.length === 0) {
            select.innerHTML = '<option value="">No hay categorías disponibles</option>';
            return;
        }

        select.innerHTML = `<option value="">${placeholder}</option>`;
        categorias.forEach(categoria => {
            const option = document.createElement('option');
            option.value = categoria.idCategoria;
            option.textContent = categoria.nombreCategoria;
            select.appendChild(option);
        });

        if (selectedValue) {
            select.value = selectedValue;
        }
    } catch (error) {
        console.error('❌ Error cargando categorías:', error);
        select.innerHTML = `<option value="">${placeholder}</option>`;
        if (error.message && error.message.includes('No autorizado')) {
            alert('Sesión expirada. Por favor, inicie sesión nuevamente.');
            window.location.href = '/login.html';
        }
    }

    if (!selectedValue) {
        select.value = '';
    }
}

function configurarTabsEstado() {
    const tabs = document.querySelectorAll('#estadoTabs .nav-link');
    if (!tabs.length) {
        setSelectValue('filtroEstado', estadoTabSeleccionado);
        return;
    }

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const estado = tab.dataset.estadoTab;
            if (!estado || estado === estadoTabSeleccionado) {
                return;
            }
            estadoTabSeleccionado = estado;
            actualizarTabsEstadoUI();
            refrescarProductos();
        });
    });

    actualizarTabsEstadoUI();
}

function actualizarTabsEstadoUI() {
    const tabs = document.querySelectorAll('#estadoTabs .nav-link');
    if (tabs.length) {
        tabs.forEach(tab => {
            const estado = tab.dataset.estadoTab;
            if (estado === estadoTabSeleccionado) {
                tab.classList.add('active');
            } else {
                tab.classList.remove('active');
            }
        });
    }

    setSelectValue('filtroEstado', estadoTabSeleccionado);
}

function obtenerValorSelect(id) {
    const element = document.getElementById(id);
    if (!element) {
        return '';
    }
    const value = element.value;
    return value && value.trim() !== '' ? value : '';
}

function obtenerValorInput(id) {
    const element = document.getElementById(id);
    return element ? element.value : '';
}

function setInputValue(id, value) {
    const element = document.getElementById(id);
    if (element) {
        element.value = value ?? '';
    }
}

function setSelectValue(id, value) {
    const element = document.getElementById(id);
    if (element) {
        element.value = value ?? '';
    }
}

function parseEntero(valor) {
    const numero = parseInt(valor, 10);
    return Number.isFinite(numero) ? numero : null;
}

function parseDecimal(valor) {
    const numero = parseFloat(valor);
    return Number.isFinite(numero) ? numero : null;
}

function normalizarTexto(valor) {
    if (valor === undefined || valor === null) {
        return null;
    }
    const texto = valor.trim();
    return texto === '' ? null : texto;
}

function generarSKUUnico() {
    const random = Math.floor(Math.random() * 10000);
    return `SKU-${Date.now().toString().slice(-6)}-${random}`;
}

function debounce(callback, delay) {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(callback, delay);
}

async function fetchConToken(url, options = {}) {
    const token = sessionStorage.getItem(TOKEN_KEY);
    const headers = Object.assign({}, options.headers || {});

    const metodo = (options.method || 'GET').toUpperCase();
    if (metodo !== 'GET' && !headers['Content-Type']) {
        headers['Content-Type'] = 'application/json';
    }

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    return fetch(url, { ...options, headers });
}