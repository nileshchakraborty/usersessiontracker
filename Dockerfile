FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/user-session-tracker-0.0.1-SNAPSHOT.jar app.jar
RUN apk add --no-cache bash
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dlogging.level.root=DEBUG", "-jar", "app.jar", "--debug"] 