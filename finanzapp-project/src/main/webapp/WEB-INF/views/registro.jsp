<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>FinanzApp — Registro</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/registro.css">
</head>
<body>

<div class="phone">
  <div class="notch"></div>
  <div class="screen">

    <div class="bg-slot">imagen de fondo completa de la pantalla<br>(tarjetas + gráfico)</div>
    <div class="bg-overlay"></div>

    <div class="content">
      <div class="screen-title">Registro</div>

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
          <label>Fecha de nacimiento</label>
          <div class="input-wrap"><input type="date" name="fechaNacimiento" value="${fechaNacimiento}" required><div class="icon-slot">cal</div></div>
        </div>
        <div class="field">
          <label>Email</label>
          <div class="input-wrap"><div class="icon-slot">mail</div><input type="email" name="email" value="${email}" required></div>
        </div>
        <div class="field">
          <label>Contraseña</label>
          <div class="input-wrap"><div class="icon-slot">lock</div><input type="password" name="password" required></div>
        </div>

        <button type="submit" class="btn-primary">Crear cuenta</button>
      </form>

      <div class="center-link">¿Ya tienes cuenta? <a href="${pageContext.request.contextPath}/login" class="link">Inicia sesión aquí</a></div>

      <div class="tabs">
        <a href="${pageContext.request.contextPath}/registro" style="text-decoration:none;flex:1;"><div class="tab active">Registro</div></a>
        <a href="${pageContext.request.contextPath}/login" style="text-decoration:none;flex:1;"><div class="tab">Login</div></a>
      </div>

      <div class="brand">
        <div class="brand-mark">logo</div>
        <div class="brand-name">FinanzApp</div>
      </div>
    </div>
  </div>
</div>

</body>
</html>
