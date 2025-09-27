/**
 * productos.js - Lógica para el Módulo de Productos
 * Versión 2.0 - Conectado al backend Spring Boot.
 */
document.addEventListener('DOMContentLoaded', () => {
    const tablaBody = document.querySelector('#tablaProductos tbody');
    const modal = new bootstrap.Modal(document.getElementById('productoModal'));
    const form = document.getElementById('formProducto');
    const modalTitle = document.getElementById('modalLabel');
    const productoIdInput = document.getElementById('productoId');
    const btnNuevoProducto = document.getElementById('btnNuevoProducto');

    // ================== CAMBIO CLAVE ==================
    // Usamos la URL completa del backend para que el frontend sepa dónde hacer las peticiones.
    const API_URL = 'http://localhost:8080/api/productos';

    // --- SECCIÓN 1: FUNCIONES DE AYUDA Y RENDERIZADO ---

    function getToken() {
        return sessionStorage.getItem('jwtToken');
    }

    function handleApiError(response) {
        if (response.status === 401 || response.status === 403) {
            alert('Su sesión ha expirado o no tiene permisos. Por favor, inicie sesión de nuevo.');
            window.location.href = '/html/login.html';
        } else {
            console.error(`Error de API: ${response.status} ${response.statusText}`);
        }
    }

    async function renderizarTabla() {
        const token = getToken();
        if (!token) return;

        try {
            const response = await fetch(API_URL, {
                headers: { 'Authorization': `Bearer ${token}` }
            });

            if (!response.ok) {
                handleApiError(response);
                return;
            }

            const productos = await response.json();
            tablaBody.innerHTML = '';
            productos.forEach(producto => {
                const row = document.createElement('tr');
                // Añadimos las clases para que auth.js gestione los permisos
                row.innerHTML = `
                    <td>${producto.id}</td>
                    <td>${producto.nombre}</td>
                    <td>${producto.categoria}</td>
                    <td>S/. ${parseFloat(producto.precio).toFixed(2)}</td>
                    <td>${producto.stock}</td>
                    <td class="col-acciones">
                        <button class="btn btn-sm btn-info btn-editar" data-producto-id="${producto.id}" title="Editar"><i class="bi bi-pencil-square"></i></button>
                        <button class="btn btn-sm btn-danger btn-eliminar" data-producto-id="${producto.id}" title="Eliminar"><i class="bi bi-trash"></i></button>
                    </td>
                `;
                tablaBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error al renderizar la tabla de productos:', error);
            alert('No se pudieron cargar los datos de productos.');
        }
    }

    // --- SECCIÓN 2: MANEJO DE EVENTOS ---

    tablaBody.addEventListener('click', async (event) => {
        const target = event.target.closest('button');
        if (!target) return;

        const productoId = target.dataset.productoId;

        if (target.classList.contains('btn-editar')) {
            await editarProducto(productoId);
        } else if (target.classList.contains('btn-eliminar')) {
            await eliminarProducto(productoId);
        }
    });

    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const token = getToken();

        const productoData = {
            nombre: document.getElementById('nombre').value,
            categoria: document.getElementById('categoria').value,
            precio: parseFloat(document.getElementById('precio').value),
            stock: parseInt(document.getElementById('stock').value, 10),
        };

        const productoId = productoIdInput.value;
        const method = productoId ? 'PUT' : 'POST';
        const url = productoId ? `${API_URL}/${productoId}` : API_URL;

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(productoData)
            });

            if (!response.ok) {
                handleApiError(response);
                throw new Error('Falló la solicitud de guardar.');
            }

            alert(`Producto ${productoId ? 'actualizado' : 'registrado'} con éxito.`);
            modal.hide();
            await renderizarTabla();
        } catch (error) {
            console.error('Error al guardar el producto:', error);
            alert('No se pudo guardar el producto.');
        }
    });

    btnNuevoProducto.addEventListener('click', () => {
        form.reset();
        productoIdInput.value = '';
        modalTitle.textContent = 'Registrar Nuevo Producto';
        modal.show();
    });

    // --- SECCIÓN 3: FUNCIONES DE ACCIÓN ---

    async function editarProducto(id) {
        const token = getToken();
        try {
            const response = await fetch(`${API_URL}/${id}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (!response.ok) {
                handleApiError(response);
                return;
            }
            const producto = await response.json();

            modalTitle.textContent = 'Actualizar Producto';
            productoIdInput.value = producto.id;
            document.getElementById('nombre').value = producto.nombre;
            document.getElementById('categoria').value = producto.categoria;
            document.getElementById('precio').value = producto.precio;
            document.getElementById('stock').value = producto.stock;
            modal.show();
        } catch (error) {
            console.error(`Error al obtener datos para editar el producto #${id}:`, error);
        }
    }

    async function eliminarProducto(id) {
        if (!confirm(`¿Estás seguro de que deseas eliminar el Producto #${id}?`)) return;

        const token = getToken();
        try {
            const response = await fetch(`${API_URL}/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });

            if (!response.ok) {
                handleApiError(response);
                throw new Error('Falló la solicitud de eliminar.');
            }

            alert(`Producto #${id} eliminado con éxito.`);
            await renderizarTabla();
        } catch (error) {
            console.error(`Error al eliminar el producto #${id}:`, error);
            alert('No se pudo eliminar el producto.');
        }
    }

    // --- INICIALIZACIÓN ---
    renderizarTabla();
});