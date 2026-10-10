<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>FinanzApp — Panel Administrador</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin.css">
</head>
<body>

<div class="topbar">
  <div class="brand">
    <div class="brand-mark">FA</div>
    <div class="brand-name">FinanzApp</div>
    <div class="brand-tag">/ ADMIN</div>
  </div>
  <div class="session-box">
    <span>Sesión: <b>${adminNombre}</b></span>
    <a href="${pageContext.request.contextPath}/logout" class="logout-link">Cerrar sesión</a>
  </div>
</div>

<div class="wrap">
  <div class="page-title">Gestión de usuarios</div>
  <p class="page-sub">Consulta, edita o elimina las cuentas registradas en FinanzApp.</p>

  <div class="card">
    <table>
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Cédula</th>
          <th>Email</th>
          <th>Rol</th>
          <th>Saldo</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${empty usuarios}">
            <tr><td colspan="6" class="empty">No hay usuarios registrados todavía.</td></tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="u" items="${usuarios}">
              <tr>
                <td data-label="Nombre">${u.nombres} ${u.apellidos}</td>
                <td data-label="Cédula">${u.cedula}</td>
                <td data-label="Email">${u.email}</td>
                <td data-label="Rol">
                  <c:choose>
                    <c:when test="${u.rol == 'ADMIN'}"><span class="badge badge-admin">ADMIN</span></c:when>
                    <c:otherwise><span class="badge badge-user">USER</span></c:otherwise>
                  </c:choose>
                </td>
                <td data-label="Saldo"><fmt:formatNumber value="${u.saldo}" type="currency" currencySymbol="$" maxFractionDigits="0"/></td>
                <td data-label="Acciones">
                  <div class="actions">
                    <a class="btn-sm btn-edit" href="${pageContext.request.contextPath}/admin/usuarios/${u.id}/editar">Editar</a>
                    <form method="post" action="${pageContext.request.contextPath}/admin/usuarios/${u.id}/eliminar"
                          onsubmit="return confirm('¿Eliminar al usuario ${u.nombres} ${u.apellidos}? Esta acción no se puede deshacer.');" style="display:inline;">
                      <button type="submit" class="btn-sm btn-del">Eliminar</button>
                    </form>
                  </div>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>

  <c:if test="${not empty editando}">
    <div class="edit-panel">
      <div class="edit-title">Editando: ${editando.nombres} ${editando.apellidos}</div>
      <form method="post" action="${pageContext.request.contextPath}/admin/usuarios/${editando.id}/editar">
        <div class="grid-2">
          <div class="field">
            <label>Nombres</label>
            <input type="text" name="nombres" value="${editando.nombres}" required>
          </div>
          <div class="field">
            <label>Apellidos</label>
            <input type="text" name="apellidos" value="${editando.apellidos}" required>
          </div>
          <div class="field">
            <label>Cédula</label>
            <input type="text" name="cedula" value="${editando.cedula}" required>
          </div>
          <div class="field">
            <label>Fecha de nacimiento</label>
            <input type="date" name="fechaNacimiento" value="${editando.fechaNacimiento}">
          </div>
          <div class="field">
            <label>Email</label>
            <input type="email" name="email" value="${editando.email}" required>
          </div>
          <div class="field">
            <label>Rol</label>
            <select name="rol">
              <option value="USER" ${editando.rol == 'USER' ? 'selected' : ''}>USER</option>
              <option value="ADMIN" ${editando.rol == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
            </select>
          </div>
        </div>
        <div class="edit-actions">
          <button type="submit" class="btn-primary">Guardar cambios</button>
          <a href="${pageContext.request.contextPath}/admin/usuarios" class="btn-cancel">Cancelar</a>
        </div>
      </form>
    </div>
  </c:if>
</div>

</body>
</html>
