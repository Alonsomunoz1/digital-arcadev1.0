Digital Arcade

​Este proyecto es una tienda comunitaria pensada para que los jugadores puedan comprar y vender ítems de videojuegos entre ellos. La idea es muy similar a lo que hacen plataformas como el Steam Community Market o warframe.market. A nivel técnico, toda la plataforma está montada sobre una arquitectura de microservicios utilizando Spring Boot.  
​Sobre el proyecto

​Desarrollado por: Alonso muñoz 
​Mi aporte y lo que desarrollé
​En este proyecto me encargué de desarrollar cinco entidades clave del sistema. Para cada una de ellas armé el CRUD completo, 
estructurando el código bajo el clásico patrón de Controller, Service y Repository:
​Juego y Item (en ms-catalogo): Gestionan los juegos que tenemos disponibles en la tienda y los ítems del catálogo que le pertenecen a cada juego.
​Publicacion y MetodoPago (en ms-mercado): Manejan las ofertas de venta que publican los usuarios, las cuales pueden pasar por estados como DISPONIBLE, VENDIDO o CANCELADO, además de gestionar las opciones de pago.

​Rol (en ms-usuarios): Controla a los usuarios, sus roles dentro de la plataforma y si están conectados.
​Detalles técnicos de mi implementación
​Para mantener el código limpio y robusto, apliqué varias buenas prácticas:

​Uso de DTOs: Los controladores reciben y devuelven DTOs para no exponer directamente las entidades de nuestra base de datos.

​HATEOAS: Configuré las respuestas de la API (usando EntityModel y CollectionModel) para que incluyan enlaces útiles, como la ruta a la propia entidad (self), a la colección o a acciones relacionadas.
​Validaciones robustas: Le agregué validaciones a los DTOs usando anotaciones típicas como @NotBlank, @NotNull, @Size, @Min y @Email. Estas se activan automáticamente con @Valid cuando entran peticiones por el controlador.
​Consultas automáticas (Derived Queries): Aproveché la magia de Spring Data JPA para no tener que escribir SQL a mano. Con solo nombrar bien los métodos, como existsByNombre o findByIdJuego, el framework arma la consulta por detrás.
​Testing: Armé pruebas unitarias para la capa de servicios utilizando JUnit 5 y Mockito. Las estructuré siguiendo el formato clásico de Given - When - Then para que sean fáciles de leer.
​Guía para levantar el proyecto localmente
​Si quieres correr el proyecto, vas a necesitar tener instalados Java 21, Maven y una instancia de MySQL corriendo en localhost:3306.
​Es importante respetar este orden de arranque para que los servicios se comuniquen bien:
​Primero levanta eureka (es el servidor de descubrimiento y corre en el puerto 8761).
​Luego levanta los microservicios de dominio: ms-catalogo, ms-mercado y ms-usuarios.
​Por último, levanta el gateway (en el puerto 8080).
​Un detalle súper cómodo es que no tienes que configurar las tablas a mano; cada microservicio se encarga de crear su propia base de datos y correr sus migraciones con Flyway apenas arranca.

​¿Cómo verificar que todo levantó bien?
​Puedes mirar el panel de Eureka entrando a: http://localhost:8761
​O hacer una petición de prueba pasando por el Gateway: http://localhost:8080/api/v1/juegos
​Stack Tecnológico
​L
enguaje & Framework: Java 21, Spring Boot
​Infraestructura: Spring Cloud (Eureka, Gateway)
​Base de datos: MySQL, Spring Data JPA, Flyway para migraciones
​API & Documentación: Spring HATEOAS, Swagger (springdoc-openapi)
​Testing & Utilidades: JUnit 5, Mockito, Lombok
