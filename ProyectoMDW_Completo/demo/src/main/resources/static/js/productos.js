/**
 * productos.js - Lógica para el Módulo de Productos
 * Versión 2.0 - Conectado al backend Spring Boot.
 */


document.addEventListener("DOMContentLoaded", function () {
    // Cargar los productos cuando el documento esté listo
    cargarProductos();

    // Cargar las estadísticas cuando el documento esté listo
    cargarEstadisticas();

});

async function cargarProductos() {
    try {
        const productos = await obtenerProductos(); // Obtener los productos desde la API
        console.log('Productos cargados:', productos); // Mostrar los productos en la consola
        mostrarProductos(productos); // Llamar a la función para mostrar los productos en la interfaz (si es necesario)
    } catch (error) {
        mostrarError(error.message); // Mostrar error si algo sale mal
    }
}

// Función para obtener los productos desde la API
async function obtenerProductos() {
    const token = sessionStorage.getItem('jwtToken'); // Obtener el token JWT

    // Hacer la solicitud fetch con el token JWT en los encabezados
    const response = await fetch('/api/productos', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : '' // Incluir el token en los encabezados
        }
    });

    if (!response.ok) {
        throw new Error('No se pudieron cargar los productos. Verifique la autenticación.');
    }

    return await response.json(); // Retornar los productos en formato JSON
}

// Mostrar los productos en la interfaz (opcional)
function mostrarProductos(productos) {
    const tableBody = document.querySelector('#productos-body');
    tableBody.innerHTML = ''; // Limpiar la tabla antes de añadir los productos

    productos.forEach(producto => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${producto.sku}</td>
            <td>
                <div class="d-flex align-items-center gap-2">
                    <div>
                        <div class="fw-semibold">${producto.nombreProducto}</div>
                        <div class="small text-muted">${producto.descripcion}</div>
                    </div>
                </div>
            </td>
            <td><span class="badge bg-secondary-subtle text-secondary">${producto.categoria.nombreCategoria}</span></td>
            <td><span class="badge bg-info-subtle text-info">${producto.talla} / ${producto.color}</span></td>
            <td class="text-end">${producto.stockActual}</td>
            <td class="text-end">S/ ${producto.precio.toFixed(2)}</td>
            <td>
                <span class="badge ${producto.estado === 'Activo' ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}">
                    <i class="bi bi-check2-circle me-1"></i>${producto.estado}
                </span>
            </td>
            <td class="d-flex gap-2">
                <button class="btn btn-sm btn-outline-info" data-bs-toggle="modal" data-bs-target="#modalVerProducto" type="button">
                    <i class="bi bi-eye"></i>
                </button>
                <button class="btn btn-sm btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#modalEditarProducto" type="button">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" type="button">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        `;

        tableBody.appendChild(row); // Añadir la fila a la tabla
    });
}

// Mostrar mensajes de error en la interfaz
function mostrarError(message) {
    const errorElement = document.getElementById('productos-error');
    errorElement.textContent = message;
    errorElement.style.display = 'block';
}


//Mostrar las estadisticas
async function cargarEstadisticas() {
    try {
        const estadisticas = await obtenerEstadisticas(); // Obtener las estadísticas desde la API
        console.log('Estadísticas de productos:', estadisticas); // Mostrar las estadísticas en la consola
        mostrarEstadisticas(estadisticas); // Llamar a la función para mostrar las estadísticas en la interfaz
    } catch (error) {
        mostrarError(error.message); // Mostrar error si algo sale mal
    }
}

// Función para obtener las estadísticas desde la API
async function obtenerEstadisticas() {
    const token = sessionStorage.getItem('jwtToken'); // Obtener el token JWT

    // Hacer la solicitud fetch con el token JWT en los encabezados
    const response = await fetch('/api/productos/estadisticas', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : '' // Incluir el token en los encabezados
        }
    });

    if (!response.ok) {
        throw new Error('No se pudieron cargar las estadísticas de productos. Verifique la autenticación.');
    }

    return await response.json(); // Retornar las estadísticas en formato JSON
}

// Mostrar las estadísticas en la interfaz
function mostrarEstadisticas(estadisticas) {
    // Aquí puedes modificar el contenido de los elementos de los KPIs
    document.getElementById('totalProductos').textContent = estadisticas.totalProductos;
    document.getElementById('productosActivos').textContent = estadisticas.productosActivos;
    document.getElementById('stockBajo').textContent = estadisticas.stockBajo;
}



// Función para guardar el producto
document.getElementById('btnGuardarProducto').addEventListener('click', async function () {
    const form = document.getElementById('formCrearProducto');

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const productoData = {
        sku: document.getElementById('sku').value,
        nombreProducto: document.getElementById('nombreProducto').value,
        descripcion: document.getElementById('descripcion').value,
        stockActual: parseInt(document.getElementById('stockActual').value),
        precio: parseFloat(document.getElementById('precio').value),
        talla: document.getElementById('talla').value || null,
        color: document.getElementById('color').value || null,
        estado: document.getElementById('estado').value,
        imagenProducto: document.getElementById('imagenProducto').value || null,
        categoria: {
            idCategoria: parseInt(document.getElementById('idCategoria').value)
        }
    };

    try {
        const token = sessionStorage.getItem('jwtToken'); // Obtener el token

        const response = await fetch('/api/productos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token ? `Bearer ${token}` : '' // Añadir el token
            },
            body: JSON.stringify(productoData)
        });

        if (response.ok) {
            const nuevoProducto = await response.json();
            alert('Producto creado exitosamente!');

            // Cerrar modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('modalCrearProducto'));
            modal.hide();

            // Limpiar formulario
            form.reset();

            // Recargar la lista de productos (si tienes una función para esto)
            if (typeof cargarProductos === 'function') {
                cargarProductos();
            }

        } else {
            const error = await response.json();
            alert('Error al crear producto: ' + error.error);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error de conexión: ' + error.message);
    }
});


// Función mejorada para cargar categorías CON TOKEN
async function cargarCategorias() {
    const select = document.getElementById('idCategoria');
    const token = sessionStorage.getItem('jwtToken'); // Obtener el token JWT

    try {
        console.log('🔍 Solicitando categorías...');
        select.innerHTML = '<option value="">Cargando categorías...</option>';

        const response = await fetch('/api/categorias', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token ? `Bearer ${token}` : '' // Incluir el token
            }
        });

        if (response.ok) {
            const categorias = await response.json();
            console.log('✅ Categorías recibidas:', categorias);

            if (categorias.length > 0) {
                select.innerHTML = '<option value="">Seleccionar categoría</option>';

                categorias.forEach(categoria => {
                    const option = document.createElement('option');
                    option.value = categoria.idCategoria;
                    option.textContent = categoria.nombreCategoria;
                    select.appendChild(option);
                });

                console.log(`✅ ${categorias.length} categorías cargadas`);
            } else {
                select.innerHTML = '<option value="">No hay categorías disponibles</option>';
                console.warn('⚠️ No hay categorías disponibles');
            }
        } else if (response.status === 401) {
            throw new Error('No autorizado. Por favor, inicie sesión nuevamente.');
        } else {
            throw new Error(`Error HTTP: ${response.status}`);
        }
    } catch (error) {
        console.error('❌ Error cargando categorías:', error);
        select.innerHTML = `
            <option value="">Error cargando categorías</option>
            <option value="1">Polos</option>
            <option value="2">Pantalones</option>
            <option value="3">Casacas</option>
            <option value="4">Accesorios</option>
        `;

        // Mostrar error si es de autenticación
        if (error.message.includes('No autorizado')) {
            alert('Sesión expirada. Por favor, inicie sesión nuevamente.');
            window.location.href = '/login.html'; // Redirigir al login
        }
    }
}


// Cargar categorías cuando se abre el modal (código existente)
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('modalCrearProducto');

    if (modal) {
        modal.addEventListener('show.bs.modal', function () {
            console.log('🎯 Modal abierto - cargando categorías...');
            cargarCategorias();
        });
    }

    const btnNuevoProducto = document.querySelector('[data-bs-target="#modalCrearProducto"]');
    if (btnNuevoProducto) {
        btnNuevoProducto.addEventListener('click', function () {
            console.log('🖱️ Click en Nuevo Producto - precargando categorías...');
            setTimeout(() => cargarCategorias(), 100);
        });
    }
});
// En tu formulario, usa un SKU diferente
function generarSKUUnico() {
    const random = Math.floor(Math.random() * 10000);
    return `SKU-${Date.now().toString().slice(-6)}-${random}`;
}

// Al abrir el modal, genera SKU automáticamente
document.getElementById('modalCrearProducto').addEventListener('show.bs.modal', function() {
    document.getElementById('sku').value = generarSKUUnico();
    cargarCategorias();
});

//Antiguo javascript
/*
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
*/