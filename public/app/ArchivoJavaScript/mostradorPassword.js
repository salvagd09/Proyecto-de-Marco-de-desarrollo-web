const contra=document.getElementById("contraseña");
const icono=document.querySelector(".fa-eye");
function cambioFormato(){
    if(contra.type==="password"){
        contra.type="text";
        icono.classList.remove('fa-eye');
        icono.classList.add('fa-eye-slash');
    }
    else{
        contra.type="password";
        icono.classList.remove('fa-eye-slash');
        icono.classList.add('fa-eye');
    }
}