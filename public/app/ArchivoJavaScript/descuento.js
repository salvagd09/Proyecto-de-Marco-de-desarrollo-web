// descuento.js

document.addEventListener("DOMContentLoaded", function () {
  const verificarBtn = document.getElementById("verificar-codigo");
  const inputCodigo = document.getElementById("input-codigo");

  verificarBtn.addEventListener("click", function () {
    const codigoIngresado = inputCodigo.value.trim().toUpperCase();

    // Lista de códigos válidos y su % de descuento
    const codigosValidos = {
      "DESC10": 0.10,
      "DESC20": 0.20,
      "ESTILOS5": 0.05
    };

    // Obtener producto guardado
    const producto = JSON.parse(localStorage.getItem("productoSeleccionado"));

    if (!producto) return;

    let cantidad = parseInt(document.getElementById("cantidad-input").value);
    let precioBase = parseFloat(producto.precio.replace("S/", "").trim());

    // Verificar si el código existe
    if (codigosValidos[codigoIngresado]) {
      const descuento = codigosValidos[codigoIngresado];
      const totalConDescuento = precioBase * cantidad * (1 - descuento);

      document.getElementById("totalProducto").innerHTML = `
        <strong>Total con descuento:</strong> S/ ${totalConDescuento.toFixed(2)}
        <br><span style="color: green;">✔ Código aplicado: ${codigoIngresado}</span>
      `;
    } else {
      document.getElementById("totalProducto").innerHTML = `
        <strong>Total:</strong> S/ ${(precioBase * cantidad).toFixed(2)}
        <br><span style="color: red;">✖ Código inválido</span>
      `;
    }
  });
});
