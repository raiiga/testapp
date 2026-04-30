FROM gradle:9.4.1-jdk21-corretto AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
RUN gradle build -x test --no-daemon > /dev/null 2>&1 || true

COPY src ./src
RUN gradle clean bootJar -x test --no-daemon

FROM amazoncorretto:21-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m -X X:+UseZGC"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]