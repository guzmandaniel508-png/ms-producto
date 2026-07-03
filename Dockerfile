FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar


COPY --from=builder /app/src/main/resources/wallet /app/wallet

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]