function validacionContactanos() {
  const nombre = document.getElementById("nombre").value.trim();
  const apellido = document.getElementById("apellido").value.trim();
  const correo = document.getElementById("correo").value.trim();
  const telefono = document.getElementById("telefono").value.trim();
  const mensaje = document.getElementById("mensaje").value.trim();

  const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const regexTelefono = /^[0-9]{6,15}$/;

  if (!nombre || !apellido || !correo || !telefono || !mensaje) {
    alert("Por favor, completa todos los campos.");
    return false;
  }

  if (!regexCorreo.test(correo)) {
    alert("Ingresa un correo válido.");
    return false;
  }

  if (!regexTelefono.test(telefono)) {
    alert("Ingresa un número de teléfono válido.");
    return false;
  }

  return true; // ✅ permite el envío automático
}
document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault(); // Evita que el formulario se envíe normalmente
  if (!validacionContactanos()) {
    return;
  }
  const formData = new FormData(this);
  fetch("../../router/routerContactoC.php", {
    method: "POST",
    body: formData
  })
    .then(response => response.json())
    .then(data => {
      alert(data.message);
      if (data.success) {
          window.location.href="Principal.html";
      }
    })
    .catch(error => {
      console.error("Error:", error);
      alert("Ocurrió un error al enviar mensaje");
    });
});