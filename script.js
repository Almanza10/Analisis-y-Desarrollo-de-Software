


// ============================================================
//  1. NAVEGACIÓN — cambia de página y resalta link activo
// ============================================================

function configurarNavegacion() {
  var links = document.querySelectorAll("nav ul li a");
  var paginaActual = window.location.pathname.split("/").pop();

  // Si estamos en la raíz o index.html, marcar "Inicio" como activo
  if (paginaActual === "" || paginaActual === "index.html") {
    paginaActual = "index.html";
  }

  links.forEach(function(link) {
    var destino = link.getAttribute("href");

    // Resaltar el link de la página donde estamos
    if (destino === paginaActual) {
      link.classList.add("activo");
    } else {
      link.classList.remove("activo");
    }

    // Al hacer clic, navegar a la página correspondiente
    link.addEventListener("click", function(e) {
      e.preventDefault(); // evitar comportamiento por defecto
      window.location.href = destino; // navegar a la página
    });
  });
}

configurarNavegacion();


// ============================================================
//  2. NEWSLETTER — guardar correos en lista y mostrar mensaje
// ============================================================

// Lista donde se guardan los correos durante la sesión
var listaCorreos = [];

function manejarSuscripcion() {
  var inputEmail = document.querySelector(".newsletter-form input[type='email']");
  var botonSuscribir = document.querySelector(".newsletter-form button");

  // Si no existe el formulario en esta página, salir
  if (!inputEmail || !botonSuscribir) return;

  botonSuscribir.addEventListener("click", function() {
    var email = inputEmail.value.trim();

    // Validar que el campo no esté vacío
    if (email === "") {
      mostrarMensaje("Por favor ingresa tu correo electrónico.", "error");
      return;
    }

    // Validar formato básico de email
    var formatoEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formatoEmail.test(email)) {
      mostrarMensaje("El correo no tiene un formato válido.", "error");
      return;
    }

    // Validar que el correo no esté ya registrado
    if (listaCorreos.includes(email)) {
      mostrarMensaje("Este correo ya está suscrito.", "error");
      return;
    }

    // Guardar el correo en la lista
    listaCorreos.push(email);

    // Mostrar en consola la lista actualizada
    console.log("✅ Nuevo suscriptor:", email);
    console.log("📋 Lista de correos guardados:", listaCorreos);

    // Mostrar mensaje de éxito en pantalla
    mostrarMensaje("¡Gracias! Te has suscrito correctamente.", "exito");

    // Limpiar el campo
    inputEmail.value = "";
  });

  // También funciona al presionar Enter en el input
  inputEmail.addEventListener("keydown", function(evento) {
    if (evento.key === "Enter") {
      botonSuscribir.click();
    }
  });
}

// Crea y muestra un mensaje debajo del formulario
function mostrarMensaje(texto, tipo) {
  // Eliminar mensaje anterior si existe
  var mensajeAnterior = document.getElementById("newsletter-mensaje");
  if (mensajeAnterior) {
    mensajeAnterior.remove();
  }

  var mensaje = document.createElement("p");
  mensaje.id = "newsletter-mensaje";
  mensaje.textContent = texto;
  mensaje.style.marginTop = "10px";
  mensaje.style.fontWeight = "700";
  mensaje.style.fontSize = "14px";
  mensaje.style.width = "100%";

  if (tipo === "exito") {
    mensaje.style.color = "#2e7d32"; // verde
  } else {
    mensaje.style.color = "#c62828"; // rojo
  }

  var formulario = document.querySelector(".newsletter");
  formulario.appendChild(mensaje);

  // El mensaje desaparece solo después de 4 segundos
  setTimeout(function() {
    mensaje.remove();
  }, 4000);
}

manejarSuscripcion();
