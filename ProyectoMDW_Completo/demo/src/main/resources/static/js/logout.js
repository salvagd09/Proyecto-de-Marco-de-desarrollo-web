/**
 * logout.js - Manejador del Cierre de Sesión
 * Versión 3.1 - Limpieza total y redirección consistente.
 */
document.addEventListener('DOMContentLoaded', function() {
    const logoutButton = document.getElementById('logout-button');

    if (logoutButton) {
        logoutButton.addEventListener('click', function() {
            // Limpia toda la información del usuario (incluido el token JWT) del sessionStorage y localStorage.
            sessionStorage.clear();
            localStorage.clear(); // Por si acaso guardaste algo en localStorage.

            console.log('Sesión local eliminada. Redirigiendo a la página de login.');

            // Redirige a la página de login principal (index.html en la raíz del static)
            window.location.href = '/';
        });
    } else {
        console.warn("El botón de logout con id 'logout-button' no fue encontrado en esta página.");
    }
});