<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>FinanzApp — Login</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css">
</head>
<body>

<div class="phone">
  <div class="notch"></div>
  <div class="screen">

    <div class="bg-slot">imagen de fondo completa de la pantalla<br>(ilustración monedas + gráfico)</div>
    <div class="bg-overlay"></div>

    <div class="content">
      <div class="brand">
        <div class="brand-mark">logo</div>
        <div class="brand-name">FinanzApp</div>
      </div>

      <div class="screen-title">Inicia sesión</div>
      <p class="screen-sub">Bienvenido de nuevo, gestiona tus finanzas fácilmente.</p>

      <c:if test="${not empty param.registrado}">
        <div class="msg-ok">✔ Cuenta creada con éxito. Ya puedes iniciar sesión.</div>
      </c:if>
      <c:if test="${not empty error}">
        <div class="msg-error">⚠ ${error}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="field">
          <label>Usuario (email):</label>
          <div class="input-wrap">
            <div class="icon-slot">user</div>
            <input type="text" name="email" value="${email}" required>
          </div>
        </div>

        <div class="field">
          <label>Contraseña:</label>
          <div class="input-wrap">
            <div class="icon-slot">lock</div>
            <input type="password" name="password" required>
          </div>
        </div>

        <button type="submit" class="btn-primary">Iniciar Sesión <span>→</span></button>
      </form>

      <div class="center-link">¿No tiene una cuenta? <a href="${pageContext.request.contextPath}/registro" class="link">Regístrese</a></div>
      <div class="center-link">🔒 <a href="#" class="link">Olvidó la contraseña</a></div>
    </div>

    <div class="home-dot">home</div>
  </div>
</div>

</body>
</html>
