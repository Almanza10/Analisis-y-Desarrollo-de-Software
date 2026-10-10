# FinanzApp — Spring Boot + JSP/JSTL + MySQL

Proyecto base con 4 pantallas conectadas a MySQL, manteniendo el mismo diseño
visual (fondo oscuro, gradientes morado/cian, tipografías Poppins + Manrope)
de los mockups originales.

## Pantallas

| Pantalla | URL | Descripción |
|---|---|---|
| Login | `/login` | Valida usuario (email) + contraseña contra MySQL. |
| Registro | `/registro` | Crea un usuario nuevo (rol `USER` por defecto). |
| Error de registro | (automática) | Se muestra si la fecha de nacimiento o el email son inválidos. |
| Inicio | `/inicio` | Home del usuario logueado (saludo, saldo desde BD). |
| Admin | `/admin/usuarios` | Panel para ver, editar y eliminar usuarios (solo rol `ADMIN`). |

## 1. Requisitos

- Java 17+
- Maven 3.8+
- MySQL 8 corriendo en `localhost:3306`

## 2. Base de datos

No necesitas crear las tablas a mano: `spring.jpa.hibernate.ddl-auto=update`
las genera solas. Solo crea la base (o deja que la cree sola gracias a
`createDatabaseIfNotExist=true` en la URL):

```sql
CREATE DATABASE IF NOT EXISTS finanzapp_db;
```

Ajusta usuario/contraseña de MySQL en:
`src/main/resources/application.properties`

```properties
spring.datasource.username=root
spring.datasource.password=root
```

## 3. Ejecutar en desarrollo

```bash
mvn spring-boot:run
```

Abre: http://localhost:8080/login

## 4. Usuario administrador por defecto

Al arrancar por primera vez se crea automáticamente (ver `DataSeeder.java`):

```
email:    admin@finanzapp.com
password: admin123
```

Úsalo para entrar directo al panel `/admin/usuarios`. Cámbialo o bórralo
antes de pasar a producción.

## 5. Empaquetar (WAR)

```bash
mvn clean package
```

Genera `target/finanzapp.war`, desplegable en cualquier servidor de
aplicaciones compatible con Servlet/JSP (Tomcat, etc.) o ejecutable
directamente:

```bash
java -jar target/finanzapp.war
```

## 6. Notas importantes / próximos pasos

- **Contraseñas en texto plano**: por pedido explícito, las contraseñas se
  guardan sin hash en la tabla `usuarios`. Es válido para esta etapa del
  proyecto académico, pero antes de producción se recomienda migrar a
  Spring Security + BCrypt.
- **Imágenes de fondo y logo**: los `.bg-slot` y `.brand-mark` en los JSP
  siguen igual que en el mockup original — son placeholders. Cuando tengas
  las imágenes finales, reemplázalas así:
  ```html
  <!-- antes -->
  <div class="bg-slot">...</div>
  <!-- después -->
  <img src="${pageContext.request.contextPath}/resources/img/fondo-login.png" class="bg-img">
  ```
  y guarda las imágenes en `src/main/webapp/resources/img/`.
- **Transacciones/gastos**: la pantalla de inicio todavía muestra gastos de
  ejemplo estáticos (igual que el mockup). El saldo sí es real (columna
  `saldo` de la tabla `usuarios`). Si luego quieres gastos reales, se puede
  agregar una entidad `Transaccion` con su propio controlador.
- **Verificación de compilación**: este sandbox no tiene acceso a Maven
  Central, así que no pude correr `mvn compile` aquí. El código sigue los
  patrones estándar de Spring Boot 3 / Jakarta EE 9+, pero corre
  `mvn clean compile` en tu máquina como primer paso para detectar cualquier
  detalle de versión.
