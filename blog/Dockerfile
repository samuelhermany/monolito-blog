# Use a imagem do Eclipse Temurin com JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Copiar o pom.xml e o código-fonte
COPY pom.xml .
COPY src ./src

# Instalar Maven (se não estiver disponível no Alpine) e construir o JAR
RUN apk add --no-cache maven \
    && mvn clean package -DskipTests

# Expor a porta (Render usa PORT dinâmica)
ENV PORT=8080
EXPOSE 8080

# Comando para rodar o JAR
ENTRYPOINT ["java", "-jar", "target/blog-0.0.1-SNAPSHOT.jar"]
