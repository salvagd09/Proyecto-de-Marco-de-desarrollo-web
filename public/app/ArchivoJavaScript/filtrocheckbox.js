const checkboxes_sexo=document.querySelectorAll('.categoria');
const productos=document.querySelectorAll('.Tarjeta');
checkboxes_sexo.forEach(checkbox=>{
    checkbox.addEventListener('change',filtrarProductos());
})
function filtrarProductos(){
    const categoriasSeleccionadas=[];
    checkboxes_sexo.forEach(checkbox => {
    if (checkbox.checked) {
      categoriasSeleccionadas.push(checkbox.value);
    }
  });

  productos.forEach(producto => {
    const categoriasProducto = producto.className.split(' ').filter(c => c !== 'Tarjeta');
    let mostrarProducto = false;
    if (categoriasSeleccionadas.length === 0) {
      mostrarProducto = true; // Mostrar todos si no hay filtros
    } else {
      mostrarProducto = categoriasSeleccionadas.every(categoriaSeleccionada => 
                categoriasProducto.includes(categoriaSeleccionada)
       );
    }
    if (mostrarProducto) {
      producto.style.display = 'block';
    } else {
      producto.style.display = 'none';
    }
  });
}