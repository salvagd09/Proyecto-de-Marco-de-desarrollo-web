/**
 * login.js - Maneja el proceso de autenticación del usuario.
 * Versión 5.0 - ¡CORRECCIÓN FINAL! Rutas de redirección ajustadas a la carpeta /html.
 */
document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            event.preventDefault();

            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

            const credentials = {
                email: email,
                password: password
            };

            const apiUrl = 'http://localhost:8080/api/auth/signin';

            console.log(`Intentando conectar a la URL simple: ${apiUrl}`);

            fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(credentials)
            })
                .then(response => {
                    if (!response.ok) {
                        let errorMessage = 'Credenciales inválidas. Verifique su email y contraseña.';
                        if (response.status === 404) {
                            errorMessage = 'Error de conexión (404). La URL no se encontró. Revisa el backend.';
                        }
                        throw new Error(errorMessage);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("Login exitoso. Datos recibidos:", data);

                    sessionStorage.setItem('jwtToken', data.token);
                    sessionStorage.setItem('userName', data.email);
                    sessionStorage.setItem('userRole', data.rol);
                    sessionStorage.setItem('userEmail', data.email);

                    // --- ¡CORRECCIÓN DE RUTA! ---
                    // Añadimos "html/" al inicio para que redirija a la carpeta correcta.
                    switch (data.rol) {
                        case "Administrador":
                            window.location.href = "html/Ventana_Admin.html";
                            break;
                        case "Vendedor":
                            window.location.href = "html/ventana_vendedor.html";
                            break;
                        case "Contador":
                            window.location.href = "html/Ventana_Contador.html";
                            break;
                        default:
                            alert("Login correcto, pero no hay una vista definida para el rol: " + data.rol);
                    }
                })
                .catch(error => {
                    console.error('Error durante el login:', error.message);
                    const errorElement = document.getElementById('login-error');
                    if (errorElement) {
                        errorElement.textContent = error.message;
                        errorElement.style.display = 'block';
                    } else {
                        alert(error.message);
                    }
                });
        });
    }
});