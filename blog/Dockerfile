# Use a imagem do Eclipse Temurin com JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Copiar pom.xml e src
COPY pom.xml ./
COPY src ./src

# Instalar Maven
RUN apk add --no-cache maven

# Build do projeto com UTF-8
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# Porta
ENV PORT=8080
EXPOSE 8080

# Rodar JAR
ENTRYPOINT ["java", "-jar", "target/blog-0.0.1-SNAPSHOT.jar"]
