document.addEventListener("DOMContentLoaded", () => {
  const tarjetas = document.querySelectorAll(".Tarjeta");

  tarjetas.forEach(tarjeta => {
    const btnComprar = tarjeta.querySelector(".btn-comprar");

    // Evento al hacer clic en COMPRAR
    btnComprar.addEventListener("click", (e) => {

      // Obtener datos del producto
      const nombre = tarjeta.querySelector("h2").textContent.trim();
      const precio = tarjeta.querySelector(".Precio1").textContent.trim();
      const imagen = tarjeta.querySelector("img").src;

      const producto = {
        nombre,
        precio,
        imagen,
        cantidad: 1
      };
      // Guardar en localStorage (para Compra.html)
      localStorage.setItem("productoSeleccionado", JSON.stringify(producto));

    });
  });
});