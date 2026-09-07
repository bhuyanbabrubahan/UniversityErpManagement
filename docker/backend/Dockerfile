FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/target/student-management-system-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/uploads/student-documents
EXPOSE 8089
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
