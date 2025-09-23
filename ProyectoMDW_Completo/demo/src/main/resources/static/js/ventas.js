document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formVenta');
    const tablaBody = document.querySelector('#tablaVentas tbody');
    const modal = new bootstrap.Modal(document.getElementById('ventaModal'));
    const modalTitle = document.getElementById('modalLabel');
    const ventaIdInput = document.getElementById('ventaId');
    const btnNuevaVenta = document.getElementById('btnNuevaVenta');

    // Datos de ejemplo para simular una base de datos
    let ventas = [
        { id: 1, cliente: 'Juan Pérez', fecha: '2025-08-25', total: 250.00, estado: 'Completada' },
        { id: 2, cliente: 'Ana García', fecha: '2025-08-26', total: 120.50, estado: 'Completada' },
        { id: 3, cliente: 'Luis Torres', fecha: '2025-08-27', total: 55.75, estado: 'Pendiente' },
    ];

    function renderizarTabla() {
        tablaBody.innerHTML = '';
        ventas.forEach(venta => {
            const row = document.createElement('tr');
            const estadoClase = obtenerClaseEstado(venta.estado);
            row.innerHTML = `
                <td>${venta.id}</td>
                <td>${venta.cliente}</td>
                <td>${venta.fecha}</td>
                <td>S/. ${venta.total.toFixed(2)}</td>
                <td><span class="badge ${estadoClase}">${venta.estado}</span></td>
                <td>
                    <button class="btn btn-sm btn-success boleta-btn" data-venta-id="${venta.id}"><i class="bi bi-receipt-cutoff"></i></button>
                    <button class="btn btn-sm btn-info editar-btn" data-venta-id="${venta.id}"><i class="bi bi-pencil-square"></i></button>
                    <button class="btn btn-sm btn-danger eliminar-btn" data-venta-id="${venta.id}"><i class="bi bi-trash"></i></button>
                </td>
            `;
            tablaBody.appendChild(row);

            // Agregar eventos a los botones de la nueva fila
            row.querySelector('.boleta-btn').addEventListener('click', () => generarBoleta(venta.id));
            row.querySelector('.editar-btn').addEventListener('click', () => editarVenta(venta.id));
            row.querySelector('.eliminar-btn').addEventListener('click', () => eliminarVenta(venta.id));
        });
    }

    function obtenerClaseEstado(estado) {
        switch (estado) {
            case 'Completada': return 'bg-success';
            case 'Pendiente': return 'bg-warning text-dark';
            case 'Cancelada': return 'bg-danger';
            default: return 'bg-secondary';
        }
    }

    function editarVenta(id) {
        const venta = ventas.find(v => v.id === id);
        if (venta) {
            modalTitle.textContent = 'Actualizar Venta';
            ventaIdInput.value = venta.id;
            document.getElementById('cliente').value = venta.cliente;
            document.getElementById('fecha').value = venta.fecha;
            document.getElementById('total').value = venta.total;
            document.getElementById('estado').value = venta.estado;
            modal.show();
        }
    }

    function eliminarVenta(id) {
        if (confirm(`¿Estás seguro de que deseas eliminar la Venta #${id}?`)) {
            ventas = ventas.filter(v => v.id !== id);
            renderizarTabla();
        }
    }

    function generarBoleta(id) {
        const venta = ventas.find(v => v.id === id);
        if (venta) {
            alert(`Generando boleta para la Venta #${venta.id}:\nCliente: ${venta.cliente}\nTotal: S/. ${venta.total.toFixed(2)}`);
        }
    }

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const ventaId = ventaIdInput.value;
        const cliente = document.getElementById('cliente').value;
        const fecha = document.getElementById('fecha').value;
        const total = parseFloat(document.getElementById('total').value);
        const estado = document.getElementById('estado').value;

        if (ventaId) {
            const index = ventas.findIndex(v => v.id == ventaId);
            if (index !== -1) {
                ventas[index].cliente = cliente;
                ventas[index].fecha = fecha;
                ventas[index].total = total;
                ventas[index].estado = estado;
                alert(`Venta #${ventaId} actualizada con éxito.`);
            }
        } else {
            const nuevaVenta = {
                id: Date.now(),
                cliente: cliente,
                fecha: fecha,
                total: total,
                estado: estado,
            };
            ventas.push(nuevaVenta);
            alert('Nueva venta registrada con éxito.');
        }

        renderizarTabla();
        form.reset();
        modal.hide();
    });

    btnNuevaVenta.addEventListener('click', () => {
        form.reset();
        ventaIdInput.value = '';
        modalTitle.textContent = 'Registrar Nueva Venta';
    });

    renderizarTabla();
});