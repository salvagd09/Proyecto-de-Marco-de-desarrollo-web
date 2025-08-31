document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formGasto');
    const tablaBody = document.querySelector('#tablaGastos tbody');
    const modal = new bootstrap.Modal(document.getElementById('gastoModal'));
    const modalTitle = document.getElementById('modalLabel');
    const btnNuevoGasto = document.getElementById('btnNuevoGasto');

    // Datos de ejemplo para simular una base de datos de gastos
    let gastos = [
        { id: 1, descripcion: 'Alquiler del local', tipo: 'Alquiler', fecha: '2025-08-01', monto: 1200.00, estado: 'Pendiente' },
        { id: 2, descripcion: 'Recibo de electricidad', tipo: 'Electricidad', fecha: '2025-08-15', monto: 150.50, estado: 'Completado' },
        { id: 3, descripcion: 'Recibo de agua', tipo: 'Agua', fecha: '2025-08-18', monto: 45.00, estado: 'Pendiente' },
    ];

    function renderizarTabla() {
        tablaBody.innerHTML = '';
        gastos.forEach(gasto => {
            const row = document.createElement('tr');
            const estadoClase = obtenerClaseEstado(gasto.estado);
            row.innerHTML = `
                <td>${gasto.id}</td>
                <td>${gasto.descripcion}</td>
                <td>${gasto.tipo}</td>
                <td>${gasto.fecha}</td>
                <td>S/. ${gasto.monto.toFixed(2)}</td>
                <td><span class="badge ${estadoClase}">${gasto.estado}</span></td>
                <td>
                    <button class="btn btn-sm btn-info estado-btn" data-gasto-id="${gasto.id}"><i class="bi bi-check-circle"></i></button>
                    <button class="btn btn-sm btn-danger eliminar-btn" data-gasto-id="${gasto.id}"><i class="bi bi-trash"></i></button>
                </td>
            `;
            tablaBody.appendChild(row);

            // Agregar eventos a los botones de la nueva fila
            row.querySelector('.estado-btn').addEventListener('click', () => cambiarEstado(gasto.id));
            row.querySelector('.eliminar-btn').addEventListener('click', () => eliminarGasto(gasto.id));
        });
    }

    function obtenerClaseEstado(estado) {
        switch (estado) {
            case 'Completado': return 'bg-success';
            case 'Pendiente': return 'bg-warning text-dark';
            default: return 'bg-secondary';
        }
    }

    function cambiarEstado(id) {
        const gasto = gastos.find(g => g.id === id);
        if (gasto) {
            // Cambiar el estado entre 'Pendiente' y 'Completado'
            gasto.estado = gasto.estado === 'Pendiente' ? 'Completado' : 'Pendiente';
            renderizarTabla();
        }
    }

    function eliminarGasto(id) {
        if (confirm(`¿Estás seguro de que deseas eliminar el Gasto #${id}?`)) {
            gastos = gastos.filter(g => g.id !== id);
            renderizarTabla();
        }
    }

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const descripcion = document.getElementById('descripcion').value;
        const tipo = document.getElementById('tipo').value;
        const fecha = document.getElementById('fecha').value;
        const monto = parseFloat(document.getElementById('monto').value);

        const nuevoGasto = {
            id: Date.now(),
            descripcion: descripcion,
            tipo: tipo,
            fecha: fecha,
            monto: monto,
            estado: 'Pendiente' // El estado inicial de un nuevo gasto siempre es 'Pendiente'
        };
        gastos.push(nuevoGasto);
        alert('Nuevo gasto registrado con éxito.');

        renderizarTabla();
        form.reset();
        modal.hide();
    });

    btnNuevoGasto.addEventListener('click', () => {
        form.reset();
        modalTitle.textContent = 'Registrar Nuevo Gasto';
    });

    renderizarTabla();
});