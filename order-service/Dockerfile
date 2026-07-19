# ==========================================
# Stage 1: Build the application using Java 25 JDK
# ==========================================
FROM eclipse-temurin:25-jdk-jammy AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==========================================
# Stage 2: Run the application using Java 25 JRE
# ==========================================
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN useradd -m springuser && chown -R springuser:springuser /app
USER springuser
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
