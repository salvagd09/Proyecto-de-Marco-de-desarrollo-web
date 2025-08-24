document.addEventListener("DOMContentLoaded", function () {
  const producto = JSON.parse(localStorage.getItem("productoSeleccionado"));

  if (producto) {
    const lista = document.querySelector(".lista-producto");
    let cantidad = 1;

    // Asegúrate de extraer el precio base correctamente aquí
    let precioTexto = producto.precio || "S/0";
    let precioBase = parseFloat(precioTexto.replace("S/", "").trim());

    lista.innerHTML = `<div class="Item-producto">
        <img src="${producto.imagen}" alt="${producto.nombre}" style="width: 100px;" />
        <div class="Detalle-Producto">
          <h3>${producto.nombre}</h3>
          <p><strong>Talla:</strong> ${producto.talla || "No especificada"}</p>
          <div class="Cantidad">
            <button class="btn-restar">-</button>
            <input type="text" value="${cantidad}" readonly id="cantidad-input" />
            <button class="btn-sumar">+</button>
          </div>
          <p>Precio unitario: S/ ${precioBase.toFixed(2)}</p>
        </div>
      </div>
    `;

    // Mostrar en resumen del pedido
    document.getElementById("nombreProducto").textContent = `Producto: ${producto.nombre}`;
    document.getElementById("precioProducto").textContent = `Precio unitario: S/ ${precioBase.toFixed(2)}`;
    const totalElemento = document.getElementById("totalProducto");

    // Función para actualizar el total
    function actualizarTotal() {
      const total = precioBase * cantidad;
      totalElemento.innerHTML = `<strong>Total:</strong> S/ ${total.toFixed(2)}`;
      document.getElementById("cantidad-input").value = cantidad;
    }

    // Eventos para + y -
    document.querySelector(".btn-sumar").addEventListener("click", () => {
      cantidad++;
      actualizarTotal();
    });

    document.querySelector(".btn-restar").addEventListener("click", () => {
      if (cantidad > 1) {
        cantidad--;
        actualizarTotal();
      }
    });

    // Inicializar total
    actualizarTotal();
  }
});


