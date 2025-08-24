  document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault(); // Evita que el formulario se envíe normalmente
  const formData = new FormData(this);
  fetch("../../router/routerRegistro.php?accion=registrar", {
    method: "POST",
    body: formData
  })
  .then(response => response.json())
  .then(data => {
    alert(data.message);
    if (data.success) {
      window.location.href = "Principal.html";
    }
  })
  .catch(error => {
    console.error("Error:", error);
    alert("Ocurrió un error al registrarse.");
  });
});