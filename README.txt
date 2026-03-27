1. Importar la base de datos, se incluye un archivo docker-compose.yml y el archivo sql de la db para su importación.
2. Modificar los datos de conexión en el archivo de propiedades: usuarios, contraseña y url.
2. levantar el proyecto.
3. ejecutar las pruebas compartidas en la colección de postman.
4. Importante: Para la prueba de UpdateStatus, el sistema solo acepta los estados: DELIVERED o CANCELLED.
5. Para las pruebas de creación (Multipart), asegúrate de adjuntar un archivo PDF y una Imagen en los campos correspondientes del form-data.
6. Ejecución de Tests: Para validar la lógica de negocio, ejecuta
