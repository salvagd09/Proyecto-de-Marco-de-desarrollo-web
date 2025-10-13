document.addEventListener('DOMContentLoaded', () => {
    const sidebar = document.getElementById('sidebar');
    if (!sidebar) {
        return;
    }

    const navLinks = sidebar.querySelectorAll('.nav-link');
    if (!navLinks.length) {
        return;
    }

    const currentPath = window.location.pathname.replace(/\/$/, '');

    let mejorCoincidencia = null;
    let longitudCoincidencia = -1;

    navLinks.forEach(link => {
        link.classList.remove('active');

        const href = link.getAttribute('href');
        if (!href) {
            return;
        }

        const normalizedHref = href.replace(/\/$/, '');
        if (!normalizedHref) {
            return;
        }

        const esCoincidenciaExacta = currentPath === normalizedHref;
        const esCoincidenciaConPrefijo = currentPath.startsWith(`${normalizedHref}/`);

        if (esCoincidenciaExacta || esCoincidenciaConPrefijo) {
            if (normalizedHref.length > longitudCoincidencia) {
                mejorCoincidencia = link;
                longitudCoincidencia = normalizedHref.length;
            }
        }
    });

    if (mejorCoincidencia) {
        mejorCoincidencia.classList.add('active');
    } else {
        const first = sidebar.querySelector('.nav-link');
        if (first) {
            first.classList.add('active');
        }
    }
});
