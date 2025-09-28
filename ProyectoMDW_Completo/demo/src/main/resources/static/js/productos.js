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
    const tableBody = obtenerTablaProductos();
    if (!tableBody) {
        return;
    }

    tableBody.innerHTML = '';

    productos.forEach(producto => {
        tableBody.appendChild(crearFilaProducto(producto));
    });
}

function obtenerTablaProductos() {
    return document.querySelector('#productos-body') || document.querySelector('#tablaProductos tbody');
}

function crearFilaProducto(producto) {
    const row = document.createElement('tr');
    const categoriaNombre = producto.categoria && producto.categoria.nombreCategoria ? producto.categoria.nombreCategoria : 'Sin categoría';
    const precioNumber = Number(producto.precio);
    const precioFormateado = Number.isFinite(precioNumber) ? precioNumber.toFixed(2) : producto.precio;

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
            <span class="badge ${producto.estado === 'Activo' ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}">
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
            <button class="btn btn-sm btn-outline-danger" type="button">
                <i class="bi bi-trash"></i>
            </button>
        </td>
    `;

    const editButton = row.querySelector('.btn-edit-producto');
    if (editButton) {
        editButton.addEventListener('click', () => prepararEdicionProducto(producto));
    }

    return row;
}

let productoEnEdicion = null;

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

async function prepararEdicionProducto(producto) {
    try {
        productoEnEdicion = producto;

        setInputValue('editarIdProducto', producto.idProducto);
        setInputValue('editarSku', producto.sku);
        setInputValue('editarNombreProducto', producto.nombreProducto);
        setInputValue('editarDescripcion', producto.descripcion);

        const precio = producto.precio != null ? Number(producto.precio) : '';
        setInputValue('editarPrecio', Number.isFinite(precio) ? precio : (producto.precio ?? ''));

        const stock = producto.stockActual != null ? Number(producto.stockActual) : '';
        setInputValue('editarStockActual', Number.isFinite(stock) ? stock : (producto.stockActual ?? ''));

        setSelectValue('editarTalla', producto.talla);
        setInputValue('editarColor', producto.color);
        setSelectValue('editarEstado', producto.estado || 'Activo');
        setInputValue('editarImagenProducto', producto.imagenProducto);

        const categoriaSelect = document.getElementById('editarCategoria');
        const categoriaId = producto.categoria && producto.categoria.idCategoria != null
            ? producto.categoria.idCategoria
            : null;

        await cargarCategorias(categoriaSelect, categoriaId);

        if (categoriaSelect) {
            categoriaSelect.value = categoriaId != null ? String(categoriaId) : '';
        }
    } catch (error) {
        console.error('❌ Error al preparar el modal de edición:', error);
        mostrarError('No se pudo cargar el producto para edición.');
    }
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

    const categoriaSeleccionada = document.getElementById('idCategoria').value;

    const productoData = {
        sku: document.getElementById('sku').value,
        nombreProducto: document.getElementById('nombreProducto').value,
        descripcion: document.getElementById('descripcion').value,
        stockActual: parseInt(document.getElementById('stockActual').value, 10),
        precio: parseFloat(document.getElementById('precio').value),
        talla: document.getElementById('talla').value || null,
        color: document.getElementById('color').value || null,
        estado: document.getElementById('estado').value,
        imagenProducto: document.getElementById('imagenProducto').value || null,
        categoriaId: categoriaSeleccionada ? parseInt(categoriaSeleccionada, 10) : null
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

            const modalElement = document.getElementById('modalCrearProducto');
            const modal = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            modal.hide();

            form.reset();

            const tableBody = obtenerTablaProductos();
            if (tableBody) {
                tableBody.appendChild(crearFilaProducto(nuevoProducto));
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
async function cargarCategorias(selectElement, selectedId) {
    const select = selectElement || document.getElementById('idCategoria');
    if (!select) {
        console.warn('No se encontró el elemento select para categorías');
        return;
    }

    const token = sessionStorage.getItem('jwtToken');
    const selectedValue = selectedId != null ? String(selectedId) : '';

    try {
        console.log('🔍 Solicitando categorías...');
        select.innerHTML = '<option value="">Cargando categorías...</option>';

        const response = await fetch('/api/categorias', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token ? `Bearer ${token}` : ''
            }
        });

        if (!response.ok) {
            if (response.status === 401) {
                throw new Error('No autorizado. Por favor, inicie sesión nuevamente.');
            }
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const categorias = await response.json();
        console.log('✅ Categorías recibidas:', categorias);

        if (categorias.length === 0) {
            select.innerHTML = '<option value="">No hay categorías disponibles</option>';
            return;
        }

        select.innerHTML = '<option value="">Seleccionar categoría</option>';
        categorias.forEach(categoria => {
            const option = document.createElement('option');
            option.value = categoria.idCategoria;
            option.textContent = categoria.nombreCategoria;
            select.appendChild(option);
        });

        if (selectedValue) {
            select.value = selectedValue;
        }

        console.log(`✅ ${categorias.length} categorías cargadas`);
    } catch (error) {
        console.error('❌ Error cargando categorías:', error);
        select.innerHTML = '<option value="">Error cargando categorías</option>';

        if (error.message && error.message.includes('No autorizado')) {
            alert('Sesión expirada. Por favor, inicie sesión nuevamente.');
            window.location.href = '/login.html';
        }
    }

    if (!selectedValue) {
        select.value = '';
    }
}


// Cargar categorías cuando se abre el modal (código existente)
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('modalCrearProducto');

    if (modal) {
        modal.addEventListener('show.bs.modal', function () {
            console.log('🎯 Modal abierto - cargando categorías...');
            const skuInput = document.getElementById('sku');
            if (skuInput) {
                skuInput.value = generarSKUUnico();
            }
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
