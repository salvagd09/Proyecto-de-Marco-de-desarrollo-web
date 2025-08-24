
document.addEventListener("DOMContentLoaded", function () {
  const btnFinalizar = document.getElementById("btn-finalizar");
  if (btnFinalizar) {
    btnFinalizar.addEventListener("click", function () {
      Swal.fire({
        title: "Compra realizada con éxito",
        text: "Gracias por confiar en EstilosPE",
        icon: "success",
        confirmButtonText: "Aceptar"
      }).then(() => {
        localStorage.removeItem("productoSeleccionado");
        // Redirigir si deseas:
       window.location.href = "../ArchivosHtml/Principal.html";
      });
    });
  }
});
