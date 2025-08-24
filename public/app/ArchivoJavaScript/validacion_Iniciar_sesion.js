function validarLogin() {
  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();
  
  if (!username || !password) {
    alert("Por favor, completa todos los campos.");
    return false;
  }
  return true;
}
  document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault(); // Evita que el formulario se envíe normalmente

  if (!validarLogin()) {
    return;
  }
  const formData = new FormData(this);
  fetch("../../router/routerIniciarSesion.php", {
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
    alert("Ocurrió un error al iniciar sesión.");
  });
});
