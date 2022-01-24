class nuevoHeader extends HTMLElement {
  constructor() {
    super();
  }
  connectedCallback() {
    this.innerHTML = `  <header>
    <nav class="navbar navbar-expand-lg navbar-light header">
      <a class="navbar-brand text-light" href="#">La Cumbrecita</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link text-light" href="#">Inicio <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-light" href="#" id="navbarDropdown" role="button"
              data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              Alquileres
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <a class="dropdown-item" href="#">Hoteles</a>
              <a class="dropdown-item" href="#">Cabañas</a>
              <a class="dropdown-item" href="#">Departamentos</a>
            </div>
          </li>
          <li class="nav-item">
            <a class="nav-link text-light" href="#">Quienes Somos</a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-light" href="#">Contacto</a>
          </li>
        </ul>
        <ul class="navbar-nav my-2 my-lg-0">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-light" href="#" id="navbarDropdown" role="button"
              data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              Login
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <a class="dropdown-item" href="/">Como dueño</a>
              <a class="dropdown-item" href="/">Como viajero</a>
            </div>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-light" href="#" id="navbarDropdown" role="button"
              data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              Registrarse
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <a class="dropdown-item" href="/">Como dueño</a>
              <a class="dropdown-item" href="/">Como viajero</a>
            </div>
          </li>
        </ul>
      </div>
    </nav>
  </header>`
  }
};

window.customElements.define('nuevo-header', nuevoHeader);
class nuevoFooter extends HTMLElement {
  constructor() {
    super();
  }
  connectedCallback() {
    this.innerHTML =
      `<footer>
      <div>
        <div class="row">
          <div class="col-xs-12 col-sm-4 col-lg-3">
            <ul>
              <li><a href="PgFrecuentes.html" class="pgFrecuentes"><b>PREGUNTAS FRECUENTES</b><span
                    class="sr-only">PREGUNTAS FRECUENTES</span></a></li>
              <li><a href="PgFrecuentes.html#reservas" class="reservas">Reservas<span class="sr-only">Consultas
                    de stock</span></a></li>
              <li><a href="PgFrecuentes.html#pago" class="pago">Formas de pago<span class="sr-only">Formas de
                    pago</span></a></li>
              <li><a href="PgFrecuentes.html#devoluciones" class="cambios">Cancelación y devoluciones<span
                    class="sr-only">Cambios
                    y devoluciones</span></a></li>
              <li><a href="PgFrecuentes.html#generales" class="generales">Preguntas Generales<span
                    class="sr-only">Preguntas Generales</span></a></li>
            </ul>
          </div>
          <div class="col-xs-12 col-sm-4 col-lg-3">
            <ul>
              <li><a href="#guias" class="guias"><i class="fa fa-book"></i><b>GUIAS TURÍSTICAS</b><span
                    class="sr-only">GUIAS TURÍSTICAS</span></a></li>
              <li><a href="#contacto" class="contacto"><i class="fa fa-envelope"></i><b>CONTACTO</b><span
                    class="sr-only">CONTACTO</span></a></li>
            </ul>
          </div>
  
          
          <div class="col-xs-12 col-sm-4 col-lg-2">
            <ul>
              <li><b>SEGUINOS EN:</b></li>
              <li><a href="https://www.facebook.com/">
                  <span<i class="fab fa-facebook" id="redessociales"></i></span>
                </a>
                <a href="https://twitter.com/">
                  <span<i class="fab fa-twitter" id="redessociales"></i></i></span>
                </a>
                <a href="https://www.instagram.com/">
                  <span<i class="fab fa-google-plus" id="redessociales"></i></i></span>
                </a>
              </li>
            </ul>
          </div>
  
    
      </div>
    </footer>`
  }
};

window.customElements.define('nuevo-footer', nuevoFooter);
/* mostrar/ocultar carrito */
function muestra_oculta(id) {
  if (document.getElementById) { //
    var el = document.getElementById(id);
    el.style.display = (el.style.display == 'none') ? 'block' : 'none';
  }
}
window.onload = function () {
  muestra_oculta('contenido');
}
