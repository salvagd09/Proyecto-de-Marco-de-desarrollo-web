/*
 * ventas.js - Lógica para el Módulo de Ventas
 * Versión 3.0 - Conectado al backend Spring Boot.
 */
document.addEventListener('DOMContentLoaded', () => {
    const tablaBody = document.querySelector('#tablaVentas tbody');
    const modal = new bootstrap.Modal(document.getElementById('ventaModal'));
    const form = document.getElementById('formVenta');
    const modalTitle = document.getElementById('modalLabel');
    const ventaIdInput = document.getElementById('ventaId');
    const btnNuevaVenta = document.getElementById('btnNuevaVenta');

    // ================== CAMBIO CLAVE ==================
    // Usamos la URL completa del backend para que el frontend sepa dónde hacer las peticiones.
    const API_URL = 'http://localhost:8080/api/ventas';

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

            const ventas = await response.json();
            tablaBody.innerHTML = '';
            ventas.forEach(venta => {
                const row = document.createElement('tr');
                const estadoClase = obtenerClaseEstado(venta.estado);

                // Añadimos las clases para que auth.js gestione los permisos
                row.innerHTML = `
                    <td>${venta.id}</td>
                    <td>${venta.cliente}</td>
                    <td>${venta.fecha}</td>
                    <td>S/. ${parseFloat(venta.total).toFixed(2)}</td>
                    <td><span class="badge ${estadoClase}">${venta.estado}</span></td>
                    <td class="col-acciones">
                        <button class="btn btn-sm btn-success boleta-btn" data-venta-id="${venta.id}" title="Generar Boleta"><i class="bi bi-receipt-cutoff"></i></button>
                        <button class="btn btn-sm btn-info btn-editar" data-venta-id="${venta.id}" title="Editar"><i class="bi bi-pencil-square"></i></button>
                        <button class="btn btn-sm btn-danger btn-eliminar" data-venta-id="${venta.id}" title="Eliminar"><i class="bi bi-trash"></i></button>
                    </td>
                `;
                tablaBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error al renderizar la tabla de ventas:', error);
            alert('No se pudieron cargar los datos de ventas.');
        }
    }

    function obtenerClaseEstado(estado) {
        switch (estado) {
            case 'Completada': return 'bg-success';
            case 'Pendiente': return 'bg-warning text-dark';
            case 'Cancelada': return 'bg-danger';
            default: return 'bg-secondary';
        }
    }

    // --- SECCIÓN 2: MANEJO DE EVENTOS ---

    tablaBody.addEventListener('click', async (event) => {
        const target = event.target.closest('button');
        if (!target) return;

        const ventaId = target.dataset.ventaId;

        if (target.classList.contains('boleta-btn')) {
            generarBoleta(ventaId);
        } else if (target.classList.contains('btn-editar')) {
            await editarVenta(ventaId);
        } else if (target.classList.contains('btn-eliminar')) {
            await eliminarVenta(ventaId);
        }
    });

    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const token = getToken();

        const ventaData = {
            cliente: document.getElementById('cliente').value,
            fecha: document.getElementById('fecha').value,
            total: parseFloat(document.getElementById('total').value),
            estado: document.getElementById('estado').value,
        };

        const ventaId = ventaIdInput.value;
        const method = ventaId ? 'PUT' : 'POST';
        const url = ventaId ? `${API_URL}/${ventaId}` : API_URL;

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(ventaData)
            });

            if (!response.ok) {
                handleApiError(response);
                throw new Error('Falló la solicitud de guardar.');
            }

            alert(`Venta ${ventaId ? 'actualizada' : 'registrada'} con éxito.`);
            modal.hide();
            await renderizarTabla();
        } catch (error) {
            console.error('Error al guardar la venta:', error);
            alert('No se pudo guardar la venta.');
        }
    });

    btnNuevaVenta.addEventListener('click', () => {
        form.reset();
        ventaIdInput.value = '';
        modalTitle.textContent = 'Registrar Nueva Venta';
        modal.show();
    });

    // --- SECCIÓN 3: FUNCIONES DE ACCIÓN ---

    async function editarVenta(id) {
        const token = getToken();
        try {
            const response = await fetch(`${API_URL}/${id}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (!response.ok) {
                handleApiError(response);
                return;
            }
            const venta = await response.json();

            modalTitle.textContent = 'Actualizar Venta';
            ventaIdInput.value = venta.id;
            document.getElementById('cliente').value = venta.cliente;
            document.getElementById('fecha').value = venta.fecha;
            document.getElementById('total').value = venta.total;
            document.getElementById('estado').value = venta.estado;
            modal.show();
        } catch (error) {
            console.error(`Error al obtener datos para editar la venta #${id}:`, error);
        }
    }

    async function eliminarVenta(id) {
        if (!confirm(`¿Estás seguro de que deseas eliminar la Venta #${id}?`)) return;

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

            alert(`Venta #${id} eliminada con éxito.`);
            await renderizarTabla();
        } catch (error) {
            console.error(`Error al eliminar la venta #${id}:`, error);
            alert('No se pudo eliminar la venta.');
        }
    }

    function generarBoleta(id) {
        alert(`FUNCIONALIDAD PENDIENTE: Generar boleta para la Venta #${id}.`);
    }

    // --- INICIALIZACIÓN ---
    renderizarTabla();
});