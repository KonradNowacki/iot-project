# Use a lightweight JDK image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory inside container
WORKDIR /app

# Copy the Gradle build output (fat jar)
COPY build/libs/*.jar app.jar

# Expose port (optional, for host access)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
