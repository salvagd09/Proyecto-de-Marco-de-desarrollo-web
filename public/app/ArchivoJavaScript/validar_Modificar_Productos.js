document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("loginForm");
  const selectProducto = document.getElementById("productos");
  const inputNombre = document.getElementById("nombre");
  const inputDescripcion = document.getElementById("descripcion");
  const inputPrecio = document.getElementById("precio");
  const inputImagen = document.getElementById("ruta_imagen");
  const mensajeError = document.getElementById("error-msg");
  form.addEventListener("submit", function (e) {
    e.preventDefault();

    // 1. Validación de campos
    const campos = [selectProducto, inputNombre, inputDescripcion, inputPrecio, inputImagen];
    let hayVacios = false;

    campos.forEach((campo) => {
      if (campo.value.trim() === "") {
        campo.style.border = "2px solid red";
        hayVacios = true;
      } else {
        campo.style.border = "";
      }
    });

    if (hayVacios) {
      if (mensajeError) {
        mensajeError.style.display = "block";
        mensajeError.textContent = "⚠️ Por favor, complete todos los campos.";
      } else {
        alert("⚠️ Por favor, complete todos los campos.");
      }
      return;
    } else {
      if (mensajeError) mensajeError.style.display = "none";
    }

    // 2. Enviar los datos si todo está correcto
    const formData = new FormData(form);

    fetch("../../router/routerModificarProducto.php?accion=modificar", {
      method: "POST",
      body: formData
    })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          alert("✅ Producto modificado correctamente.");
          form.reset();
          campos.forEach((campo) => campo.style.border = "");
        } else {
          alert("❌ Error: " + (data.error || data.message));
        }
      })
      .catch(error => {
        console.error("Error:", error);
        alert("❌ Ocurrió un error al registrar el producto.");
      });
  });
});