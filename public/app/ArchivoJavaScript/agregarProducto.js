document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault(); // Evita que el formulario se envíe normalmente
  const formData = new FormData(document.getElementById("loginForm"));
  fetch("../../router/routerAgregarProducto.php?accion=agregar", {
    method: "POST",
    body: formData
  })
  .then(response => response.json())
  .then(data => {
  if (data.success) {
    const producto = data.producto;
    localStorage.setItem("nuevoProducto", JSON.stringify(producto));
    if(data.categoria=="Ropa"){
      window.location.href = "Ventana_ProductoRopa.html";
    } else if(data.categoria=="Calzado"){
      window.location.href="Ventana_ProdcutoCalzado.html"
    } else if(data.categoria=="Accesorios"){
      window.location.href="Ventana_ProductoAccesorios.html"
    }

} else {
  alert("Error al agregar: " + data.error);
}
})
  .catch(error => {
    console.error("Error:", error);
    alert("Ocurrió un error al registrarse.");
  });
});