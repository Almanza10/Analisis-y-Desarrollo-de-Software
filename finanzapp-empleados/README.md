# FinanzApp – Sistema de gestión de empleados

Proyecto dividido en dos carpetas independientes:

```
finanzapp-empleados/
├── backend/    Java 17 + Spring Boot 3 + MySQL  (API REST, puerto 8080)
└── frontend/   Angular 17                         (interfaz tipo celular, puerto 4200)
```

## Requisitos
- Java 17 y Maven (o el Maven de VS Code / IntelliJ)
- MySQL 8 en ejecución (puerto 3306)
- Node.js 18.19+ o 20+

## 1. Backend
```bash
cd backend
# Si su usuario de MySQL no es root o tiene contraseña:
#   Windows (PowerShell):  $env:DB_USER="root"; $env:DB_PASSWORD="su_clave"
#   Linux / macOS:         export DB_USER=root DB_PASSWORD=su_clave
mvn spring-boot:run
```
La base de datos `finanzapp_empleados_db` y la tabla `empleados` se crean solas al iniciar.

## 2. Frontend
```bash
cd frontend
npm install
npm start
```
Abra http://localhost:4200. La dirección del backend está en `frontend/src/app/services/empleado.service.ts` (`API_URL`).

El logo está en `frontend/src/assets/logo.svg`; reemplácelo por el logo real de FinanzApp (mismo nombre de archivo).

## API REST
| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| GET | /api/empleados | Lista todos los empleados | 200 |
| GET | /api/empleados/{id} | Consulta un empleado | 200 / 404 |
| POST | /api/empleados | Registra un empleado | 201 / 400 / 409 |
| PUT | /api/empleados/{id} | Edita un empleado | 200 / 400 / 404 / 409 |
| DELETE | /api/empleados/{id} | Elimina un empleado | 204 / 404 |

Campos: `nombres`, `apellidos`, `numeroIdentificacion` (único), `numeroEmpleado` (único), `cargo`, `departamento`, `correo`, `telefono`, `fechaIngreso`, `estatus`.
Los errores devuelven `{ "mensaje": "...", "errores": { campo: "texto" } }`.
