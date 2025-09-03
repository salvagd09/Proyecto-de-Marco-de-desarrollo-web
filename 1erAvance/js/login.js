document.getElementById("loginForm").addEventListener("submit", function(e){
    e.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // comprabacion del administrador
    if(email === "admin@estilos.com" && password === "admin"){
      window.location.href = "../../index.html";
    } else {
      alert("Credenciales incorrectas, intenta de nuevo.");
    }
  });