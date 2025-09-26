/**
 * auth.js - Guardia de Seguridad para Páginas Protegidas
 * Versión 5.0 - Nombres de roles unificados con el backend (sin "ROLE_").
 */
document.addEventListener('DOMContentLoaded', function() {

    // --- SECCIÓN 1: VERIFICACIÓN Y CARGA DE DATOS DE SESIÓN ---
    const token = sessionStorage.getItem('jwtToken');
    const userName = sessionStorage.getItem('userName');
    const userRole = sessionStorage.getItem('userRole'); // Esto será "Administrador", "Contador", etc.
    const userEmail = sessionStorage.getItem('userEmail');

    if (!token) {
        alert('Acceso denegado. Por favor, inicie sesión.');
        window.location.href = '/html/login.html'; // La ruta es correcta
        return;
    }

    // --- SECCIÓN 2: PERSONALIZAR LA INTERFAZ DE USUARIO (UI) ---
    const profileNameElement = document.getElementById('user-name');
    const profileRoleElement = document.getElementById('user-role');
    const profileEmailElement = document.querySelector('.user-email');

    if (userName && profileNameElement) {
        profileNameElement.textContent = userName;
    }
    if (userRole && profileRoleElement) {
        // El rol ya viene sin el prefijo, así que no necesitamos reemplazar nada.
        profileRoleElement.textContent = userRole;
    }
    if (userEmail && profileEmailElement) {
        profileEmailElement.textContent = userEmail;
        profileEmailElement.title = userEmail;
    }

    // --- SECCIÓN 3: LÓGICA DE PERMISOS POR ROL ---
    console.log(`Guardia 'auth.js' activado. Rol detectado: ${userRole}`);

    // ================== CAMBIO CLAVE ==================
    // Las claves del objeto de permisos ahora coinciden con lo que envía el backend.
    const permissions = {
        'Vendedor': {
            canCreate: true,
            canEdit: true,
            canDelete: true,
            pageExceptions: {
                'Ventas_Insumos.html': { canCreate: false, canEdit: false, canDelete: false },
                'Ventas_Productos.html': { canCreate: false, canEdit: false, canDelete: false }
            }
        },
        'Contador': {
            canCreate: false,
            canEdit: false,
            canDelete: false
        },
        'Administrador': {
            canCreate: true,
            canEdit: true,
            canDelete: true
        }
    };

    const currentUserPermissions = permissions[userRole] || {};
    let finalPermissions = { ...currentUserPermissions };

    const currentPage = window.location.pathname.split('/').pop();
    if (currentUserPermissions.pageExceptions && currentUserPermissions.pageExceptions[currentPage]) {
        finalPermissions = { ...finalPermissions, ...currentUserPermissions.pageExceptions[currentPage] };
    }

    if (finalPermissions.canCreate === false) {
        document.querySelectorAll('.btn-crear, #btnNuevaOrden, #btnNuevoGasto, #btnNuevoInsumo, #btnNuevoProducto, #btnAgregarRol, #btnNuevaVenta').forEach(btn => {
            if(btn) btn.style.display = 'none';
        });
    }
    if (finalPermissions.canEdit === false) {
        document.querySelectorAll('.btn-editar').forEach(btn => {
            if(btn) btn.style.display = 'none';
        });
    }
    if (finalPermissions.canDelete === false) {
        document.querySelectorAll('.btn-eliminar').forEach(btn => {
            if(btn) btn.style.display = 'none';
        });
    }

    if (finalPermissions.canEdit === false && finalPermissions.canDelete === false) {
        document.querySelectorAll('.col-acciones').forEach(col => col.style.display = 'none');
        const observer = new MutationObserver(() => {
            document.querySelectorAll('td.col-acciones').forEach(cell => cell.style.display = 'none');
        });
        const tableBody = document.querySelector('tbody');
        if (tableBody) {
            observer.observe(tableBody, { childList: true, subtree: true });
        }
    }
});