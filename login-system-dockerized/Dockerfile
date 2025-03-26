FROM openjdk:23-jdk-slim

# Copia wait-for-it.sh al contenedor
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

WORKDIR /app
COPY target/login-system-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Espera a que MySQL esté listo y luego ejecuta la aplicación
ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]