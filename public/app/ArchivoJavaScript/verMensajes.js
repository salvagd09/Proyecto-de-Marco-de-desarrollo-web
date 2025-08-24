    document.addEventListener('DOMContentLoaded', async () => {
 const tabla = document.getElementById('Tabla');
  const loading = document.getElementById('loading');
  
  try {
    // Mostrar carga
    if (loading) loading.style.display = 'block';
    if (tabla) tabla.innerHTML = '';
    
    const response = await fetch('../../router/routerMensaje.php?accion=listar');
    
    // Verificar si la respuesta es OK (200-299)
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Error ${response.status}: ${errorText.substring(0, 100)}`);
    }
    
    // Intentar parsear como JSON
    const data = await response.json();
    
    if (!data.success) {
      throw new Error(data.error || 'Error en el servidor');
    }
    
    renderizarMensajes(data.data);
  } catch (error) {
    console.error('Error al cargar mensajes:', error);
    if (tabla) {
      tabla.innerHTML = `
        <tr>
          <td colspan="3" style="color: red; text-align: center;">
            ${escapeHtml(error.message)}
          </td>
        </tr>
      `;
    }
  } finally {
    if (loading) loading.style.display = 'none';
  }
    });

    function renderizarMensajes(mensajes) {
      const tabla = document.getElementById('Tabla');
      
      if (mensajes.length === 0) {
        tabla.innerHTML = '<tr><td colspan="3" style="text-align: center;">No hay mensajes disponibles</td></tr>';
        return;
      }
      
      tabla.innerHTML = mensajes.map(mensaje => `
        <tr>
          <td>${escapeHtml(mensaje.nombre)}</td>
          <td>${escapeHtml(mensaje.correo)}</td>
          <td>${escapeHtml(mensaje.Mensaje).replace(/\n/g, '<br>')}</td>
        </tr>
      `).join('');
    }

    function escapeHtml(unsafe) {
      return unsafe.toString()
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
    }