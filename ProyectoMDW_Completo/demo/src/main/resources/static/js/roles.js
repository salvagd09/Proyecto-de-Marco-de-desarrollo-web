const ROLES_API = '/api/roles';
const PERMISOS_API = '/api/permisos';
const TOKEN_KEY = 'jwtToken';

let roles = [];
let permisosCatalogo = [];
let rolEnEdicion = null;
let filtroRol = 'all';

const tablaBody = document.querySelector('#tablaRoles tbody');
const infoRegistros = document.getElementById('infoRegistros');
const formRol = document.getElementById('formRol');
const rolModalElement = document.getElementById('rolModal');
const rolModal = rolModalElement ? new bootstrap.Modal(rolModalElement) : null;
const modalTitle = document.getElementById('modalLabel');
const inputNombre = document.getElementById('nombreRol');
const inputDescripcion = document.getElementById('descripcionRol');
const inputRolId = document.getElementById('rolId');
const checklistPermisos = document.getElementById('permisosChecklist');

function obtenerToken() {
  return sessionStorage.getItem(TOKEN_KEY);
}

async function fetchConToken(url, options = {}) {
  const headers = Object.assign({}, options.headers || {});
  headers['Content-Type'] = 'application/json';
  const token = obtenerToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return fetch(url, { ...options, headers });
}

async function cargarPermisos() {
  try {
    const response = await fetchConToken(PERMISOS_API, { method: 'GET' });
    if (!response.ok) {
      throw new Error('No se pudieron cargar los permisos');
    }
    permisosCatalogo = await response.json();
    renderPermisosChecklist();
  } catch (error) {
    console.error('❌ Error al cargar permisos:', error);
    mostrarMensaje(error.message || 'No se pudieron cargar los permisos', 'danger');
    permisosCatalogo = [];
    renderPermisosChecklist();
  }
}


function formatoFecha(fechaIso) {
  if (!fechaIso) {
    return '—';
  }
  try {
    const fecha = new Date(fechaIso);
    return fecha.toLocaleString('es-PE', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch (e) {
    return fechaIso;
  }
}

function mostrarMensaje(mensaje, tipo = 'info') {
  const toast = document.createElement('div');
  toast.className = `alert alert-${tipo}`;
  toast.textContent = mensaje;
  document.body.appendChild(toast);
  setTimeout(() => toast.remove(), 2500);
}

function renderRoles() {
  tablaBody.innerHTML = '';
  const mostrarColUsuarios = filtroRol !== 'all';
  document.querySelectorAll('th.col-usuarios').forEach(th => {
    th.style.display = mostrarColUsuarios ? '' : 'none';
  });
  if (!roles.length) {
    const row = document.createElement('tr');
    const cell = document.createElement('td');
    cell.colSpan = 8;
    cell.className = 'text-center text-secondary py-3';
    cell.textContent = 'No hay roles registrados todavía.';
    row.appendChild(cell);
    tablaBody.appendChild(row);
    infoRegistros.textContent = 'Sin registros';
    return;
  }

  const filtrados = roles.filter((rol) => coincideConFiltro(rol.nombre));

  if (!filtrados.length) {
    const row = document.createElement('tr');
    const cell = document.createElement('td');
    cell.colSpan = 8;
    cell.className = 'text-center text-secondary py-3';
    cell.textContent = 'No hay roles que coincidan con este filtro.';
    row.appendChild(cell);
    tablaBody.appendChild(row);
    infoRegistros.textContent = `Sin registros para la pestaña seleccionada (Total: ${roles.length})`;
    return;
  }

  filtrados.forEach((rol) => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td class="fw-semibold">${rol.nombre}</td>
      <td>${rol.descripcion ? rol.descripcion : '—'}</td>
      <td class="text-center">${rol.usuariosAsignados}</td>
      <td class="col-usuarios">${formatearUsuarios(rol.usuarios)}</td>
      <td>${formatoFecha(rol.fechaCreacion)}</td>
      <td>${formatoFecha(rol.ultimaActualizacion)}</td>
      <td>${rol.actualizadoPor ? rol.actualizadoPor : '—'}</td>
      <td class="d-flex gap-2">
        <button type="button" class="btn btn-sm btn-outline-secondary" data-accion="editar">Editar</button>
      </td>
    `;
    row.querySelector('[data-accion="editar"]').addEventListener('click', () => abrirModalEdicion(rol));
    if (!mostrarColUsuarios) {
      row.querySelectorAll('.col-usuarios').forEach(td => td.style.display = 'none');
    }
    tablaBody.appendChild(row);
  });

  infoRegistros.textContent = `Mostrando ${filtrados.length} rol${filtrados.length === 1 ? '' : 'es'} (de ${roles.length})`;
  actualizarKpisRoles();
}

async function cargarRoles() {
  try {
    const response = await fetchConToken(ROLES_API, { method: 'GET' });
    if (!response.ok) {
      throw new Error('No se pudieron cargar los roles');
    }
    roles = await response.json();
    renderRoles();
  } catch (error) {
    console.error('❌ Error al cargar roles:', error);
    mostrarMensaje(error.message, 'danger');
  }
}

function abrirModalCreacion() {
  rolEnEdicion = null;
  formRol.reset();
  inputRolId.value = '';
  modalTitle.textContent = 'Crear rol';
  if (inputNombre) {
    inputNombre.selectedIndex = 0;
  }
  renderPermisosChecklist(obtenerPermisosPorRolDefecto(inputNombre ? inputNombre.value : null));
  if (rolModal) {
    rolModal.show();
  }
}

function abrirModalEdicion(rol) {
  rolEnEdicion = rol;
  inputRolId.value = rol.id;
  if (inputNombre) {
    const valorOriginal = rol.nombre || '';
    const valor = valorOriginal.toUpperCase();
    let optionExiste = Array.from(inputNombre.options).some(opt => opt.value === valor);
    if (!optionExiste && valor) {
      const nuevaOpcion = document.createElement('option');
      nuevaOpcion.value = valor;
      nuevaOpcion.textContent = valorOriginal || valor;
      inputNombre.appendChild(nuevaOpcion);
      optionExiste = true;
    }
    inputNombre.value = optionExiste ? valor : '';
  }
  inputDescripcion.value = rol.descripcion || '';
  modalTitle.textContent = 'Editar rol';
  const seleccionados = (rol.permisos || []).map((permiso) => permiso.id);
  renderPermisosChecklist(seleccionados);
  if (rolModal) {
    rolModal.show();
  }
}

async function guardarRol(event) {
  event.preventDefault();

  const payload = {
    nombre: inputNombre.value,
    descripcion: inputDescripcion.value || null,
    permisosIds: obtenerPermisosSeleccionados(),
  };

  const rolId = inputRolId.value;
  const url = rolId ? `${ROLES_API}/${rolId}` : ROLES_API;
  const method = rolId ? 'PUT' : 'POST';

  try {
    const response = await fetchConToken(url, {
      method,
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorBody = await response.json().catch(() => ({}));
      throw new Error(errorBody.error || 'No se pudo guardar el rol');
    }

    const mensaje = rolId ? 'Rol actualizado correctamente.' : 'Rol creado correctamente.';
    mostrarMensaje(mensaje, 'success');

    if (rolModal) {
      rolModal.hide();
    }
    formRol.reset();
    rolEnEdicion = null;
    await cargarRoles();
  } catch (error) {
    console.error('❌ Error al guardar rol:', error);
    mostrarMensaje(error.message, 'danger');
  }
}

function inicializarEventos() {
  const btnAgregar = document.getElementById('btnAgregarRol');
  if (btnAgregar) {
    btnAgregar.addEventListener('click', abrirModalCreacion);
  }

  if (formRol) {
    formRol.addEventListener('submit', guardarRol);
  }

  if (inputNombre) {
    inputNombre.addEventListener('change', () => {
      if (rolEnEdicion) {
        return;
      }
      const seleccion = obtenerPermisosPorRolDefecto(inputNombre.value);
      renderPermisosChecklist(seleccion);
    });
  }

  configurarTabsRol();
}

document.addEventListener('DOMContentLoaded', async () => {
  inicializarEventos();
  await cargarPermisos();
  await cargarRoles();
});

function configurarTabsRol() {
  const tabs = document.querySelectorAll('#rolesTabs .nav-link');
  if (!tabs.length) {
    return;
  }

  tabs.forEach((tab) => {
    tab.addEventListener('click', () => {
      const rolFiltro = tab.dataset.rolTab || 'all';
      if (rolFiltro === filtroRol) {
        return;
      }
      filtroRol = rolFiltro;
      actualizarTabsRolUI();
      renderRoles();
    });
  });

  actualizarTabsRolUI();
}

function actualizarTabsRolUI() {
  const tabs = document.querySelectorAll('#rolesTabs .nav-link');
  tabs.forEach((tab) => {
    const rolFiltro = tab.dataset.rolTab || 'all';
    if (rolFiltro === filtroRol) {
      tab.classList.add('active');
    } else {
      tab.classList.remove('active');
    }
  });
}

function coincideConFiltro(nombreRol) {
  if (filtroRol === 'all') {
    return true;
  }

  const nombreNormalizado = (nombreRol || '').trim().toLowerCase();

  switch (filtroRol) {
    case 'administrador':
      return nombreNormalizado.includes('admin');
    case 'vendedor':
      return nombreNormalizado.includes('vendedor');
    case 'contador':
      return nombreNormalizado.includes('contador');
    default:
      return true;
  }
}

function renderPermisosChecklist(seleccionados = []) {
  if (!checklistPermisos) {
    return;
  }

  checklistPermisos.innerHTML = '';

  if (!permisosCatalogo.length) {
    const aviso = document.createElement('div');
    aviso.className = 'text-secondary small';
    aviso.textContent = 'Sin registros de permisos';
    checklistPermisos.appendChild(aviso);
    return;
  }

  const seleccionSet = new Set(seleccionados || []);

  const listaOrdenada = [...permisosCatalogo].sort((a, b) => {
    const nombreA = (a.nombre || '').toLowerCase();
    const nombreB = (b.nombre || '').toLowerCase();
    return nombreA.localeCompare(nombreB);
  });

  listaOrdenada.forEach((permiso) => {
    const col = document.createElement('div');
    col.className = 'col';

    const wrapper = document.createElement('div');
    wrapper.className = 'form-check';

    const input = document.createElement('input');
    input.className = 'form-check-input';
    input.type = 'checkbox';
    input.id = `permiso-${permiso.id}`;
    input.value = permiso.id;
    input.name = 'permisoRol';
    if (seleccionSet.has(permiso.id)) {
      input.checked = true;
    }

    const label = document.createElement('label');
    label.className = 'form-check-label';
    label.setAttribute('for', `permiso-${permiso.id}`);
    label.textContent = permiso.nombre;

    wrapper.appendChild(input);
    wrapper.appendChild(label);

    if (permiso.descripcion) {
      const hint = document.createElement('div');
      hint.className = 'form-text';
      hint.textContent = permiso.descripcion;
      wrapper.appendChild(hint);
    }

    col.appendChild(wrapper);
    checklistPermisos.appendChild(col);
  });
}

function obtenerPermisosSeleccionados() {
  if (!checklistPermisos) {
    return [];
  }
  return Array.from(checklistPermisos.querySelectorAll('input[name="permisoRol"]:checked')).map((input) => parseInt(input.value, 10));
}

function obtenerPermisosPorRolDefecto(nombreRol) {
  if (!nombreRol) {
    return [];
  }
  const nombre = nombreRol.trim().toUpperCase();
  let objetivos;
  switch (nombre) {
    case 'ADMINISTRADOR':
      return permisosCatalogo.map((permiso) => permiso.id);
    case 'CONTADOR':
      objetivos = ['ver'];
      break;
    case 'VENDEDOR':
      objetivos = ['crear', 'editar', 'eliminar', 'ver'];
      break;
    default:
      return [];
  }
  const objetivosSet = new Set(objetivos.map((p) => p.toLowerCase()));
  return permisosCatalogo
    .filter((permiso) => objetivosSet.has((permiso.nombre || '').toLowerCase()))
    .map((permiso) => permiso.id);
}

function construirDropdownPermisos(row, rol) {
  const lista = row.querySelector('.permis-lista');
  if (!lista) {
    return;
  }

  lista.innerHTML = '';

  if (!rol.permisos || rol.permisos.length === 0) {
    const li = document.createElement('li');
    const empty = document.createElement('span');
    empty.className = 'dropdown-item text-secondary';
    empty.textContent = 'Sin permisos asignados';
    li.appendChild(empty);
    lista.appendChild(li);
    return;
  }

  rol.permisos.forEach((permiso) => {
    const li = document.createElement('li');
    const item = document.createElement('span');
    item.className = 'dropdown-item';
    const nombre = permiso && permiso.nombre ? permiso.nombre : permiso;
    item.textContent = nombre;
    if (permiso && permiso.descripcion) {
      item.title = permiso.descripcion;
    }
    li.appendChild(item);
    lista.appendChild(li);
  });
}

function formatearUsuarios(lista) {
  if (!lista || !lista.length) {
    return '—';
  }
  const max = 3;
  if (lista.length <= max) {
    return lista.join(', ');
  }
  return `${lista.slice(0, max).join(', ')} y ${lista.length - max} más`;
}

function actualizarKpisRoles() {
  const totalRoles = roles.length;
  const totalUsuariosActivos = roles.reduce((sum, rol) => sum + (rol.usuariosActivos || 0), 0);
  const totalUsuariosInactivos = roles.reduce((sum, rol) => sum + (rol.usuariosInactivos || 0), 0);
  const permisosUnicos = new Set();
  roles.forEach((rol) => {
    if (rol.permisos) {
      rol.permisos.forEach((permiso) => {
        const nombre = permiso && permiso.nombre ? permiso.nombre : permiso;
        if (nombre) {
          permisosUnicos.add(nombre.toUpperCase());
        }
      });
    }
  });

  const totalPermisos = permisosUnicos.size;

  const totalRolesEl = document.getElementById('kpiTotalRoles');
  const usuariosActivosEl = document.getElementById('kpiUsuariosActivos');
  const usuariosInactivosEl = document.getElementById('kpiUsuariosInactivos');
  const permisosUnicosEl = document.getElementById('kpiPermisosUnicos');

  if (totalRolesEl) totalRolesEl.textContent = totalRoles;
  if (usuariosActivosEl) usuariosActivosEl.textContent = totalUsuariosActivos;
  if (usuariosInactivosEl) usuariosInactivosEl.textContent = totalUsuariosInactivos;
  if (permisosUnicosEl) permisosUnicosEl.textContent = totalPermisos;
}
