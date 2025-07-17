# Linkfix

**Linkfix** es una plataforma que conecta a usuarios con técnicos calificados para la reparación de electrodomésticos. A través de un sistema georreferenciado, puedes encontrar especialistas cercanos, coordinar horarios, y calificarlos al finalizar el servicio.

## Autores

- **Horacio Zamora** – [@HoracioZamora7](https://github.com/HoracioZamora7)
- **Alessandro Bossio** – [@Bossi0](https://github.com/Bossi0)
- **César Manya** – [@Manyasab](https://github.com/Manyasab)
- **Rodrigo Castañeda** – [@RodrigoCas789](https://github.com/RodrigoCas789)
- **Joaquín Galarza** – [@Snakeblaster](https://github.com/Snakeblaster)

##  ¿Qué puedes hacer con Linkfix?

- ✔ Registro y validación de técnicos por especialidad y ubicación
- ✔ Búsqueda inteligente según ubicación y horario disponible
- ✔ Sistema de solicitudes, chat y calificaciones
- ✔ Soporte de incidencias y seguimiento administrativo

###  ¿Cómo funciona?

Los técnicos definen su disponibilidad semanal, y los clientes pueden buscar y solicitar servicios según día, hora y ubicación. Los técnicos aceptan las solicitudes y ambos pueden comunicarse a través del sistema. Luego del servicio, los usuarios pueden dejar valoraciones.

### 🌟 Beneficios esperados

- 🌱 **Menos residuos electrónicos:** Fomentamos la reparación, lo que podría reducir hasta un 20% los desechos en 5 años.
- ⏱ **Ahorro de tiempo:** Encuentra y coordina un servicio en menos de 30 minutos.
- 💼 **Más empleos técnicos:** Apoyamos el crecimiento de técnicos independientes, incluso en zonas rurales.
- 📍 **Mejor cobertura:** Amplía el acceso a servicios en zonas con poca oferta técnica.


# Instalación y despliegue

## Requisitos

- [Docker](https://www.docker.com/) y [Docker Compose](https://docs.docker.com/compose/)
- Git (opcional, para clonar el repositorio)

> [!NOTE]
> Consumo de memoria RAM de la aplicación: 
>- Aproximadamente **350 MB de  RAM** para el backend  
>- Aproximadamente **150 MB de RAM** para la database (MySQL) durante operaciones pico.

## 1. Clona o descarga el repositorio
Enlace al repositorio remoto: 
https://github.com/HoracioZamora7/linkfix/archive/refs/heads/main.zip

```bash
git clone https://github.com/HoracioZamora7/linkfix.git
cd linkfix
```

## 2. Configurar las variables de entorno

Copia el archivo .env.example como .env y completa los valores necesarios según tu configuración.

Variables de entorno que debes configurar:

| Variable| Descripción|
|--------------------|--------------------------------------------------------------------|
| `DB_USERNAME`      | Usuario de MySQL|
| `DB_PASSWORD`      | Contraseña del usuario MySQL|
| `APIPERUDEV_TOKEN` | Token de acceso a API externa (apiperu.dev)|
| `SMTP_USERNAME`    | Correo para envío de notificaciones|
| `SMTP_PASSWORD`    | Contraseña o clave de **aplicación** del correo SMTP|
| `APP_HOST_BASEURL` | URL pública del servidor donde se ejecuta la aplicación |


## Iniciar los contenedores de Docker

Ejecuta estos comandos en la raíz del proyecto.

```bash
docker-compose up --build
```
Esto creará e iniciará dos servicios:
- **app_linkfix**: la aplicación SpringBoot
- **linkfix_db**: el contenedor de MySQL

## Restaurar base de datos desde un backup
#### 1. Ubicate en el directorio raíz del proyecto
#### 2. Accede al directorio db/ donde está el archivo linkfix_backup.sql
```bash
cd db
```
#### 3. Obtén la ruta absoluta del archivo
```bash
realpath linkfix_backup.sql
```
Esto te devolverá algo como
```bash
/root/linkfixDeploy/linkfix/db/linkfix_backup.sql
```
#### 4. Ejecuta el comando para restaurar la base de datos
Reemplaza la ruta por la que obtuviste en el paso anterior.

```bash
docker exec -i linkfix_linkfix_db_1 mysql -u root linkfix < ~/linkfixDeploy/linkfix/db/linkfix_backup.sql
```
Esto cargará datos iniciales en las tablas como estado, rol, etc.

## Una vez desplegado
Una vez desplegado, podrás acceder a la aplicación desde:
http://localhost:8080

>  [!TIP]
> Es posible cambiar el puerto en linkfix/src/main/resources/application.properties. Solo cambia la línea 4:
> server.port=8080

# Licencia

Este proyecto se distribuye bajo la licencia MIT. Puedes usarlo, modificarlo y distribuirlo libremente.
Para más detalles, véase el archivo [LICENSE](https://github.com/HoracioZamora7/linkfix/blob/main/LICENSE).


