document.addEventListener("DOMContentLoaded", () => {
  const tarjetas = document.querySelectorAll(".Tarjeta");

  tarjetas.forEach(tarjeta => {
    let tallaSeleccionada = null;

    // Capturamos todos los botones de talla dentro de cada tarjeta
    const botonesTalla = tarjeta.querySelectorAll(".btn-talla");
    const btnComprar = tarjeta.querySelector(".btn-comprar");

    // Evento al hacer clic en cada talla
    botonesTalla.forEach(btn => {
      btn.addEventListener("click", () => {
        // Quitar selección previa
        botonesTalla.forEach(b => b.classList.remove("seleccionada"));
        // Marcar como seleccionada
        btn.classList.add("seleccionada");
        tallaSeleccionada = btn.textContent;
      });
    });

    // Evento al hacer clic en COMPRAR
    btnComprar.addEventListener("click", (e) => {
      if (!tallaSeleccionada) {
        e.preventDefault();
        alert("⚠️ Debes seleccionar una talla antes de continuar.");
        return;
      }

      // Obtener datos del producto
      const nombre = tarjeta.querySelector("h2").textContent.trim();
      const precio = tarjeta.querySelector(".Precio1").textContent.trim();
      const imagen = tarjeta.querySelector("img").src;

      const producto = {
        nombre,
        precio,
        imagen,
        talla: tallaSeleccionada,
        cantidad: 1
      };

      // Guardar en localStorage (para Compra.html)
      localStorage.setItem("productoSeleccionado", JSON.stringify(producto));

    });
  });
});