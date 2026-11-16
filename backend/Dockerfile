# Usado pelo servidor de hospedagem para construir a API (você NÃO precisa ter Docker instalado).
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# -Xmx limita a memória (planos gratuitos costumam ter ~512 MB)
ENTRYPOINT ["java", "-Xmx350m", "-jar", "app.jar"]
