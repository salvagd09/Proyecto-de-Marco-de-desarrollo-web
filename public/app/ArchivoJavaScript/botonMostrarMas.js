function MostrarMasMenos(){
    const boton=document.getElementById("Agregar");
    if(boton.value=="MostrarMas"){
        boton.value="MostrarMenos";
        document.getElementById("oculto").style.display='flex';
        boton.innerText="Mostrar Menos";
    } else if(boton.value="Mostrar Menos"){
        boton.value="MostrarMas";
        document.getElementById("oculto").style.display='none';
        boton.innerText="Mostrar mas";
    }
}
