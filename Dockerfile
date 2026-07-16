FROM amazoncorretto:17-alpine
WORKDIR /app
COPY build/libs/User-Authentication-System-0.0.1-SNAPSHOT.jar spring-app.jar
ENTRYPOINT ["java", "-jar", "spring-app.jar"]
# ./gradlew clean build -x test - ps
# gradlew clean build -x test - cmd

# docker login
# docker build -t subhan2007/user-auth-system:1.0 .
# docker push subhan2007/user-auth-system:1.0


# docker tag subhan2007/user-auth-system:v1 subhan2007/user-auth-system:1.0
