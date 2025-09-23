document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formOrdenCompra');
    const tablaBody = document.querySelector('#tablaCompras tbody');
    const modal = new bootstrap.Modal(document.getElementById('ordenModal'));
    const modalTitle = document.getElementById('modalLabel');
    const ordenIdInput = document.getElementById('ordenId');
    const btnNuevaOrden = document.getElementById('btnNuevaOrden');

    // Datos de ejemplo para simular una base de datos
    let compras = [
        { id: 1, proveedor: 'Proveedores Industriales SAC', fecha: '2025-08-28', total: 1500.50, estado: 'Completada' },
        { id: 2, proveedor: 'Suministros Textiles del Norte', fecha: '2025-08-29', total: 750.00, estado: 'Pendiente' },
        { id: 3, proveedor: 'Distribuidora de Insumos PE', fecha: '2025-08-30', total: 230.75, estado: 'Cancelada' },
    ];

    function renderizarTabla() {
        tablaBody.innerHTML = '';
        compras.forEach(compra => {
            const row = document.createElement('tr');
            const estadoClase = obtenerClaseEstado(compra.estado);
            row.innerHTML = `
                <td>${compra.id}</td>
                <td>${compra.proveedor}</td>
                <td>${compra.fecha}</td>
                <td>S/. ${compra.total.toFixed(2)}</td>
                <td><span class="badge ${estadoClase}">${compra.estado}</span></td>
                <td>
                    <button class="btn btn-sm btn-success boleta-btn" data-compra-id="${compra.id}"><i class="bi bi-receipt-cutoff"></i></button>
                    <button class="btn btn-sm btn-info editar-btn" data-compra-id="${compra.id}"><i class="bi bi-pencil-square"></i></button>
                    <button class="btn btn-sm btn-danger eliminar-btn" data-compra-id="${compra.id}"><i class="bi bi-trash"></i></button>
                </td>
            `;
            tablaBody.appendChild(row);

            // Agregar eventos a los botones de la nueva fila
            row.querySelector('.boleta-btn').addEventListener('click', () => generarBoleta(compra.id));
            row.querySelector('.editar-btn').addEventListener('click', () => editarCompra(compra.id));
            row.querySelector('.eliminar-btn').addEventListener('click', () => eliminarCompra(compra.id));
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

    function editarCompra(id) {
        const compra = compras.find(c => c.id === id);
        if (compra) {
            modalTitle.textContent = 'Actualizar Orden de Compra';
            ordenIdInput.value = compra.id;
            document.getElementById('proveedor').value = compra.proveedor;
            document.getElementById('fecha').value = compra.fecha;
            document.getElementById('total').value = compra.total;
            document.getElementById('estado').value = compra.estado;
            modal.show();
        }
    }

    function eliminarCompra(id) {
        if (confirm(`¿Estás seguro de que deseas eliminar la Orden de Compra #${id}?`)) {
            compras = compras.filter(c => c.id !== id);
            renderizarTabla();
        }
    }

    function generarBoleta(id) {
        const compra = compras.find(c => c.id === id);
        if (compra) {
            alert(`Generando boleta para la Orden #${compra.id}:\nProveedor: ${compra.proveedor}\nTotal: S/. ${compra.total.toFixed(2)}`);
        }
    }

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const ordenId = ordenIdInput.value;
        const proveedor = document.getElementById('proveedor').value;
        const fecha = document.getElementById('fecha').value;
        const total = parseFloat(document.getElementById('total').value);
        const estado = document.getElementById('estado').value;

        if (ordenId) {
            const index = compras.findIndex(c => c.id == ordenId);
            if (index !== -1) {
                compras[index].proveedor = proveedor;
                compras[index].fecha = fecha;
                compras[index].total = total;
                compras[index].estado = estado;
                alert(`Orden #${ordenId} actualizada con éxito.`);
            }
        } else {
            const nuevaCompra = {
                id: Date.now(),
                proveedor: proveedor,
                fecha: fecha,
                total: total,
                estado: estado,
            };
            compras.push(nuevaCompra);
            alert('Nueva orden registrada con éxito.');
        }

        renderizarTabla();
        form.reset();
        modal.hide();
    });

    btnNuevaOrden.addEventListener('click', () => {
        form.reset();
        ordenIdInput.value = '';
        modalTitle.textContent = 'Registrar Nueva Orden de Compra';
    });

    renderizarTabla();
});