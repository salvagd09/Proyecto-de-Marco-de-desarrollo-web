document.addEventListener('DOMContentLoaded', () => {
    // Referencias a los elementos del DOM
    const tablaBody = document.querySelector('#tablaRoles tbody');
    const infoRegistros = document.getElementById('infoRegistros');
    const rolModal = new bootstrap.Modal(document.getElementById('rolModal'));
    const modalTitle = document.getElementById('modalLabel');
    const formRol = document.getElementById('formRol');
    const rolIdInput = document.getElementById('rolId');
    const nombreRolInput = document.getElementById('nombreRol');
    const descripcionRolInput = document.getElementById('descripcionRol');
    const comisionRolInput = document.getElementById('comisionRol');

    // Datos de ejemplo para simular una base de datos de roles
    let roles = [
        { id: 3, nombre: 'VENDEDOR', descripcion: 'VENDEDOR', comision: 0.00 },
        { id: 12, nombre: 'SUPERVISOR', descripcion: 'SUPERVISA LAS VENTAS CREA PROMOCIONES REALIZA', comision: 0.00 },
        { id: 7, nombre: 'SOPORTE', descripcion: 'SOPORTE', comision: 0.00 },
        { id: 5, nombre: 'INVENTARIADOR', descripcion: 'REALIZA INVENTARIO', comision: 0.00 },
        { id: 11, nombre: 'CONTADOR', descripcion: 'CONTADOR', comision: 0.00 },
        { id: 2, nombre: 'CLIENTES', descripcion: 'TODOS LOS CLIENTE', comision: 0.00 },
        { id: 6, nombre: 'CAJERO VENDEDOR', descripcion: 'CAJERO VENDEDOR', comision: 10.00 },
        { id: 10, nombre: 'CAJERO 2', descripcion: 'CAJERO 2', comision: 0.00 },
        { id: 4, nombre: 'CAJERO', descripcion: 'CAJERO', comision: 0.00 },
        { id: 1, nombre: 'ADMINISTRADOR', descripcion: 'TODOS LOS PERMISOS', comision: 0.00 },
    ];

    // Función para renderizar la tabla con los datos del array 'roles'
    function renderizarTabla() {
        tablaBody.innerHTML = ''; // Limpia la tabla
        roles.forEach(rol => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${rol.id}</td>
                <td>${rol.nombre}</td>
                <td>${rol.descripcion}</td>
                <td>S/. ${rol.comision.toFixed(2)}</td>
                <td>
                    <button class="btn btn-sm btn-info editar-btn" data-rol-id="${rol.id}"><i class="bi bi-pencil-square"></i></button>
                    <button class="btn btn-sm btn-danger eliminar-btn" data-rol-id="${rol.id}"><i class="bi bi-trash"></i></button>
                </td>
            `;
            tablaBody.appendChild(row);

            // Agregar eventos a los botones de la nueva fila
            row.querySelector('.editar-btn').addEventListener('click', () => editarRol(rol.id));
            row.querySelector('.eliminar-btn').addEventListener('click', () => eliminarRol(rol.id));
        });

        // Actualiza el texto de información de registros
        infoRegistros.textContent = `Mostrando registros del 1 al ${roles.length} de un total de ${roles.length} registros`;
    }

    // Funcionalidad para agregar o editar un rol
    formRol.addEventListener('submit', (e) => {
        e.preventDefault();

        const rolId = rolIdInput.value;
        const nuevoRol = {
            nombre: nombreRolInput.value,
            descripcion: descripcionRolInput.value,
            comision: parseFloat(comisionRolInput.value)
        };

        if (rolId) {
            // Lógica para editar
            const index = roles.findIndex(r => r.id == rolId);
            if (index !== -1) {
                roles[index].nombre = nuevoRol.nombre;
                roles[index].descripcion = nuevoRol.descripcion;
                roles[index].comision = nuevoRol.comision;
                alert('Rol actualizado con éxito.');
            }
        } else {
            // Lógica para agregar
            nuevoRol.id = Date.now(); // Genera un ID único temporal
            roles.push(nuevoRol);
            alert('Rol agregado con éxito.');
        }

        renderizarTabla();
        rolModal.hide();
        formRol.reset();
    });

    // Función para llenar el formulario y abrir el modal para editar
    function editarRol(id) {
        const rol = roles.find(r => r.id === id);
        if (rol) {
            modalTitle.textContent = 'Editar Rol';
            rolIdInput.value = rol.id;
            nombreRolInput.value = rol.nombre;
            descripcionRolInput.value = rol.descripcion;
            comisionRolInput.value = rol.comision;
            rolModal.show();
        }
    }

    // Función para eliminar un rol
    function eliminarRol(id) {
        if (confirm(`¿Estás seguro de que deseas eliminar el rol #${id}?`)) {
            roles = roles.filter(r => r.id !== id);
            renderizarTabla();
            alert('Rol eliminado con éxito.');
        }
    }

    // Evento para el botón "Agregar Rol" para limpiar el formulario y abrir el modal
    document.getElementById('btnAgregarRol').addEventListener('click', () => {
        formRol.reset();
        rolIdInput.value = '';
        modalTitle.textContent = 'Agregar Nuevo Rol';
    });

    // Otros eventos de la página (Exportar, Mostrar registros)
    document.getElementById('btnExportar').addEventListener('click', () => {
        alert('Exportando la tabla...');
    });

    document.getElementById('selectRegistros').addEventListener('change', (e) => {
        console.log(`Cambiado para mostrar ${e.target.value} registros.`);
        // Lógica para filtrar registros si tienes muchos
    });

    // Llama a la función para renderizar la tabla al cargar la página
    renderizarTabla();
});