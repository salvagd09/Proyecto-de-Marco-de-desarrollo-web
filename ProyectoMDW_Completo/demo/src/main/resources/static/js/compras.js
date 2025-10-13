d/**
  * compras.js - Lógica para el Módulo de Órdenes de Compra
  * Versión 3.0 - Conectado al backend Spring Boot.
  */
 document.addEventListener('DOMContentLoaded', () => {
     const tablaBody = document.querySelector('#tablaCompras tbody');
     const modal = new bootstrap.Modal(document.getElementById('ordenModal'));
     const form = document.getElementById('formOrdenCompra');
     const modalTitle = document.getElementById('modalLabel');
     const ordenIdInput = document.getElementById('ordenId');
     const btnNuevaOrden = document.getElementById('btnNuevaOrden');

     // ================== CAMBIO CLAVE ==================
     // Usamos la URL completa del backend para que el frontend sepa dónde hacer las peticiones.
     const API_URL = 'http://localhost:8080/api/compras';

     // --- SECCIÓN 1: FUNCIONES DE RENDERIZADO Y OBTENCIÓN DE DATOS ---

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
      * Obtiene todas las órdenes de compra desde la API y las renderiza en la tabla.
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

             const compras = await response.json();
             tablaBody.innerHTML = '';
             compras.forEach(compra => {
                 const row = document.createElement('tr');
                 const estadoClase = obtenerClaseEstado(compra.estado);

                 // IMPORTANTE: Se añaden las clases 'col-acciones', 'btn-editar' y 'btn-eliminar'
                 // para que auth.js pueda gestionar los permisos.
                 row.innerHTML = `
                     <td>${compra.id}</td>
                     <td>${compra.proveedor}</td>
                     <td>${compra.fecha}</td>
                     <td>S/. ${compra.total.toFixed(2)}</td>
                     <td><span class="badge ${estadoClase}">${compra.estado}</span></td>
                     <td class="col-acciones">
                         <button class="btn btn-sm btn-success boleta-btn" data-compra-id="${compra.id}" title="Generar Boleta"><i class="bi bi-receipt-cutoff"></i></button>
                         <button class="btn btn-sm btn-info btn-editar" data-compra-id="${compra.id}" title="Editar"><i class="bi bi-pencil-square"></i></button>
                         <button class="btn btn-sm btn-danger btn-eliminar" data-compra-id="${compra.id}" title="Eliminar"><i class="bi bi-trash"></i></button>
                     </td>
                 `;
                 tablaBody.appendChild(row);
             });
         } catch (error) {
             console.error('Error al renderizar la tabla:', error);
             alert('No se pudieron cargar los datos de compras.');
         }
     }

     /**
      * Devuelve la clase de Bootstrap para el badge de estado.
      * @param {string} estado - El estado de la compra.
      * @returns {string} La clase CSS.
      */
     function obtenerClaseEstado(estado) {
         switch (estado) {
             case 'Completada': return 'bg-success';
             case 'Pendiente': return 'bg-warning text-dark';
             case 'Cancelada': return 'bg-danger';
             default: return 'bg-secondary';
         }
     }

     // --- SECCIÓN 2: MANEJO DE EVENTOS ---

     // Evento para los botones de la tabla (usando delegación de eventos)
     tablaBody.addEventListener('click', async (event) => {
         const target = event.target.closest('button');
         if (!target) return;

         const compraId = target.dataset.compraId;

         if (target.classList.contains('boleta-btn')) {
             generarBoleta(compraId);
         } else if (target.classList.contains('btn-editar')) {
             await editarCompra(compraId);
         } else if (target.classList.contains('btn-eliminar')) {
             await eliminarCompra(compraId);
         }
     });

     // Evento para el formulario (Crear o Actualizar)
     form.addEventListener('submit', async (event) => {
         event.preventDefault();
         const token = getToken();

         const compraData = {
             proveedor: document.getElementById('proveedor').value,
             fecha: document.getElementById('fecha').value,
             total: parseFloat(document.getElementById('total').value),
             estado: document.getElementById('estado').value,
         };

         const ordenId = ordenIdInput.value;
         const method = ordenId ? 'PUT' : 'POST';
         const url = ordenId ? `${API_URL}/${ordenId}` : API_URL;

         try {
             const response = await fetch(url, {
                 method: method,
                 headers: {
                     'Content-Type': 'application/json',
                     'Authorization': `Bearer ${token}`
                 },
                 body: JSON.stringify(compraData)
             });

             if (!response.ok) {
                 handleApiError(response);
                 throw new Error('Falló la solicitud de guardar.');
             }

             alert(`Orden ${ordenId ? 'actualizada' : 'registrada'} con éxito.`);
             modal.hide();
             await renderizarTabla();
         } catch (error) {
             console.error('Error al guardar la orden:', error);
             alert('No se pudo guardar la orden.');
         }
     });

     // Evento para el botón "Nueva Orden" que resetea el modal
     btnNuevaOrden.addEventListener('click', () => {
         form.reset();
         ordenIdInput.value = '';
         modalTitle.textContent = 'Registrar Nueva Orden de Compra';
     });

     // --- SECCIÓN 3: FUNCIONES DE ACCIÓN (Editar, Eliminar, etc.) ---

     async function editarCompra(id) {
         const token = getToken();
         try {
             const response = await fetch(`${API_URL}/${id}`, {
                 headers: { 'Authorization': `Bearer ${token}` }
             });
             if (!response.ok) {
                 handleApiError(response);
                 return;
             }
             const compra = await response.json();

             modalTitle.textContent = 'Actualizar Orden de Compra';
             ordenIdInput.value = compra.id;
             document.getElementById('proveedor').value = compra.proveedor;
             document.getElementById('fecha').value = compra.fecha;
             document.getElementById('total').value = compra.total;
             document.getElementById('estado').value = compra.estado;
             modal.show();
         } catch (error) {
             console.error(`Error al obtener datos para editar la compra #${id}:`, error);
         }
     }

     async function eliminarCompra(id) {
         if (!confirm(`¿Estás seguro de que deseas eliminar la Orden de Compra #${id}?`)) return;

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

             alert(`Orden #${id} eliminada con éxito.`);
             await renderizarTabla();
         } catch (error) {
             console.error(`Error al eliminar la compra #${id}:`, error);
             alert('No se pudo eliminar la orden.');
         }
     }

     function generarBoleta(id) {
         // Esta función puede mantenerse como está o modificarse para llamar a un endpoint que genere un PDF.
         alert(`FUNCIONALIDAD PENDIENTE: Generar boleta para la Orden #${id}.`);
     }

     // --- INICIALIZACIÓN ---
     renderizarTabla();
 });