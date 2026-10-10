<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>FinanzApp — Inicio</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/inicio.css">
</head>
<body>

<div class="phone">
  <div class="notch"></div>
  <div class="screen">

    <div class="bg-slot">imagen de fondo completa de la pantalla<br>(monedas + luces de fondo)</div>
    <div class="bg-overlay"></div>

    <div class="content">
      <div class="brand">
        <div class="brand-mark">logo</div>
        <div class="brand-name">FinanzApp</div>
      </div>

      <div class="greeting">
        <div class="avatar-slot">foto</div>
        <div class="greeting-text">¡Hola ${usuario.nombres}, bienvenido!</div>
      </div>

      <div class="balance-card">
        <div class="balance-label">Saldo Disponible</div>
        <div class="balance-amount">
          <fmt:formatNumber value="${usuario.saldo}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
        </div>
      </div>

      <div class="section-row">
        <div class="gastos-col">
          <div class="section-title">Gastos Recientes</div>

          <div class="gasto-item">
            <div class="gasto-icon">ic</div>
            <div class="gasto-text">
              <div class="gasto-title">Gastos</div>
              <div class="gasto-sub">Suplto</div>
            </div>
            <div class="gasto-amount">-$3,500</div>
          </div>

          <div class="gasto-item">
            <div class="gasto-icon">ic</div>
            <div class="gasto-text">
              <div class="gasto-title">Gastos</div>
              <div class="gasto-sub">Suplto</div>
            </div>
            <div class="gasto-amount">-$13,500</div>
          </div>

          <div class="gasto-item">
            <div class="gasto-icon">ic</div>
            <div class="gasto-text">
              <div class="gasto-title">Gastos</div>
              <div class="gasto-sub">Suplto</div>
            </div>
            <div class="gasto-amount">-$12,500</div>
          </div>
        </div>

        <div class="chart-col">
          <div class="section-title">Mensualmente</div>
          <div class="chart-card">
            <div class="chart-bars">
              <div class="bar" style="height:35%;"></div>
              <div class="bar" style="height:55%;"></div>
              <div class="bar" style="height:80%;"></div>
              <div class="bar" style="height:45%;"></div>
              <div class="bar" style="height:65%;"></div>
            </div>
            <div class="chart-labels">
              <span>E</span><span>F</span><span>M</span><span>A</span><span>M</span>
            </div>
          </div>
        </div>
      </div>

      <button class="btn-primary">Añadir Transacción</button>
      <div class="center-link" style="margin-top:10px;">
        <a href="${pageContext.request.contextPath}/logout" class="link">Cerrar sesión</a>
      </div>
    </div>

    <div class="bottom-nav">
      <div class="nav-item active"><div class="nav-icon">home</div></div>
      <div class="nav-item"><div class="nav-icon">graf</div></div>
      <div class="nav-item"><div class="nav-icon">doc</div></div>
      <div class="nav-item"><div class="nav-icon">cfg</div></div>
    </div>
  </div>
</div>

</body>
</html>
