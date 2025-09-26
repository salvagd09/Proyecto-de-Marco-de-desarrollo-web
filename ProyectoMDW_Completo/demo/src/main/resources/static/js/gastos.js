**
 * gastos.js - Lógica para el Módulo de Gastos Generales
 * Versión 3.0 - Conectado al backend Spring Boot.
 */
document.addEventListener('DOMContentLoaded', () => {
    const tablaBody = document.querySelector('#tablaGastos tbody');
    const modal = new bootstrap.Modal(document.getElementById('gastoModal'));
    const form = document.getElementById('formGasto');
    const modalTitle = document.getElementById('modalLabel');
    const btnNuevoGasto = document.getElementById('btnNuevoGasto');

    // ================== CAMBIO CLAVE ==================
    // Usamos la URL completa del backend para que el frontend sepa dónde hacer las peticiones.
    const API_URL = 'http://localhost:8080/api/gastos';

    // --- SECCIÓN 1: FUNCIONES DE AYUDA Y RENDERIZADO ---

    /**
     * Obtiene el token JWT del sessionStorage.
     * @returns {string|null} El token JWT o null si no se encuentra.
     */
    function getToken() {
        return sessionStorage.getItem('jwtToken');
    }

    /**
     * Maneja los errores de respuesta de la API, especialmente los de autenticación.
     * @param {Response} response - La respuesta del fetch.
     */
    function handleApiError(response) {
        if (response.status === 401 || response.status === 403) {
            alert('Su sesión ha expirado o no tiene permisos. Por favor, inicie sesión de nuevo.');
            window.location.href = '/html/login.html'; // Redirigir al login
        } else {
            console.error(`Error de API: ${response.status} ${response.statusText}`);
        }
    }

    /**
     * Obtiene todos los gastos desde la API y los renderiza en la tabla.
     */
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

            const gastos = await response.json();
            tablaBody.innerHTML = '';
            gastos.forEach(gasto => {
                const row = document.createElement('tr');
                const estadoClase = obtenerClaseEstado(gasto.estado);

                // IMPORTANTE: Se añaden las clases 'col-acciones', 'btn-editar' y 'btn-eliminar'
                row.innerHTML = `
                    <td>${gasto.id}</td>
                    <td>${gasto.descripcion}</td>
                    <td>${gasto.tipo}</td>
                    <td>${gasto.fecha}</td>
                    <td>S/. ${gasto.monto.toFixed(2)}</td>
                    <td><span class="badge ${estadoClase}">${gasto.estado}</span></td>
                    <td class="col-acciones">
                        <button class="btn btn-sm btn-info btn-editar" data-gasto-id="${gasto.id}" title="Cambiar Estado"><i class="bi bi-check-circle"></i></button>
                        <button class="btn btn-sm btn-danger btn-eliminar" data-gasto-id="${gasto.id}" title="Eliminar"><i class="bi bi-trash"></i></button>
                    </td>
                `;
                tablaBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error al renderizar la tabla de gastos:', error);
            alert('No se pudieron cargar los datos de gastos.');
        }
    }

    /**
     * Devuelve la clase de Bootstrap para el badge de estado.
     */
    function obtenerClaseEstado(estado) {
        switch (estado) {
            case 'Completado': return 'bg-success';
            case 'Pendiente': return 'bg-warning text-dark';
            default: return 'bg-secondary';
        }
    }

    // --- SECCIÓN 2: MANEJO DE EVENTOS ---

    // Delegación de eventos para los botones de acción en la tabla
    tablaBody.addEventListener('click', async (event) => {
        const target = event.target.closest('button');
        if (!target) return;

        const gastoId = target.dataset.gastoId;

        if (target.classList.contains('btn-editar')) {
            await cambiarEstado(gastoId);
        } else if (target.classList.contains('btn-eliminar')) {
            await eliminarGasto(gastoId);
        }
    });

    // Evento para el formulario de creación de gastos
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const token = getToken();

        const gastoData = {
            descripcion: document.getElementById('descripcion').value,
            tipo: document.getElementById('tipo').value,
            fecha: document.getElementById('fecha').value,
            monto: parseFloat(document.getElementById('monto').value),
            estado: 'Pendiente' // Los nuevos gastos siempre inician como pendientes
        };

        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(gastoData)
            });

            if (!response.ok) {
                handleApiError(response);
                throw new Error('Falló la solicitud de creación.');
            }

            alert('Nuevo gasto registrado con éxito.');
            modal.hide();
            await renderizarTabla();
        } catch (error) {
            console.error('Error al crear el gasto:', error);
            alert('No se pudo registrar el gasto.');
        }
    });

    // Evento para el botón "Nuevo Gasto" que resetea el modal
    btnNuevoGasto.addEventListener('click', () => {
        form.reset();
        modalTitle.textContent = 'Registrar Nuevo Gasto';
    });

    // --- SECCIÓN 3: FUNCIONES DE ACCIÓN ---

    async function cambiarEstado(id) {
        const token = getToken();
        if (!confirm('¿Desea cambiar el estado de este gasto?')) return;

        try {
            // Primero, obtenemos el gasto actual para saber su estado
            let response = await fetch(`${API_URL}/${id}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (!response.ok) { handleApiError(response); return; }
            const gasto = await response.json();

            // Cambiamos el estado
            gasto.estado = gasto.estado === 'Pendiente' ? 'Completado' : 'Pendiente';

            // Ahora, enviamos el objeto completo actualizado con PUT
            response = await fetch(`${API_URL}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(gasto)
            });

            if (!response.ok) {
                handleApiError(response);
                throw new Error('Falló la actualización de estado.');
            }

            alert('El estado del gasto ha sido actualizado.');
            await renderizarTabla();
        } catch (error) {
            console.error(`Error al cambiar estado del gasto #${id}:`, error);
            alert('No se pudo actualizar el estado del gasto.');
        }
    }

    async function eliminarGasto(id) {
        if (!confirm(`¿Estás seguro de que deseas eliminar el Gasto #${id}?`)) return;

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

            alert(`Gasto #${id} eliminado con éxito.`);
            await renderizarTabla();
        } catch (error) {
            console.error(`Error al eliminar el gasto #${id}:`, error);
            alert('No se pudo eliminar el gasto.');
        }
    }

    // --- INICIALIZACIÓN ---
    renderizarTabla();
});