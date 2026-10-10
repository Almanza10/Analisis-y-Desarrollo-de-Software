<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>FinanzApp — Error</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/error.css">
</head>
<body>

<div class="phone">
  <div class="notch"></div>
  <div class="screen">

    <div class="bg-slot">imagen de fondo completa de la pantalla<br>(monedas + tarjeta + gráfico)</div>
    <div class="bg-overlay"></div>

    <div class="content">
      <div class="screen-title">Registro</div>

      <div class="error-banner">
        <div class="line1">⚠ ERROR: Revisa los datos marcados</div>
        <div class="line2">Corrige los campos en rojo e inténtalo de nuevo.</div>
      </div>

      <form method="post" action="${pageContext.request.contextPath}/registro">
        <div class="field">
          <label>Nombres</label>
          <div class="input-wrap"><div class="icon-slot">user</div><input type="text" name="nombres" value="${nombres}" required></div>
        </div>
        <div class="field">
          <label>Apellidos</label>
          <div class="input-wrap"><div class="icon-slot">user</div><input type="text" name="apellidos" value="${apellidos}" required></div>
        </div>
        <div class="field">
          <label>Cédula</label>
          <div class="input-wrap"><div class="icon-slot">id</div><input type="text" name="cedula" value="${cedula}" required></div>
        </div>

        <div class="field">
          <label class="${not empty errorFecha ? 'err' : ''}">
            Fecha de nacimiento
            <c:if test="${not empty errorFecha}"><span class="field-hint">${errorFecha}</span></c:if>
          </label>
          <div class="input-wrap ${not empty errorFecha ? 'err' : ''}">
            <input type="date" name="fechaNacimiento" value="${fechaNacimiento}"><div class="icon-slot">cal</div>
          </div>
        </div>

        <div class="field">
          <label class="${not empty errorEmail ? 'err' : ''}">
            Email
            <c:if test="${not empty errorEmail}"><span class="field-hint">${errorEmail}</span></c:if>
          </label>
          <div class="input-wrap ${not empty errorEmail ? 'err' : ''}">
            <div class="icon-slot">mail</div><input type="email" name="email" value="${email}">
          </div>
        </div>

        <div class="field">
          <label>Contraseña</label>
          <div class="input-wrap"><div class="icon-slot">lock</div><input type="password" name="password" required></div>
        </div>

        <button type="submit" class="btn-primary">Crear cuenta</button>
      </form>

      <div class="brand">
        <div class="brand-mark">logo</div>
        <div class="brand-name">FinanzApp</div>
      </div>
    </div>
  </div>
</div>

</body>
</html>
