FROM maven as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar
ENTRYPOINT java -jar app.jar