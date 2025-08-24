function validarLogin(){
    const usuario = document.getElementById("usuario").value;
    const contraseña = document.getElementById("contraseña").value;
    if (!usuario || !contraseña) {
        return false;
    }
    return true;
};
document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault(); // Evita que el formulario se envíe normalmente
  if (!validarLogin()) {
    return;
  }
  const formData = new FormData(this);
  fetch("../../router/routerAccederAdminC.php", {
    method: "POST",
    body: formData
  })
  .then(response => response.json())
  .then(data => {
    alert(data.message);
    if (data.success) {
        window.location.href = "Admin.html"; // Cambia a la página que corresponda después del login
    }
  })
  .catch(error => {
    console.error("Error:", error);
    alert("Ocurrió un error al iniciar sesión.");
  });
});
