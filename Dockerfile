# Base image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the jar file
#COPY target /app/target
ADD target /app

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "Bubble-1.0-SNAPSHOT.jar"]

# Set the default command
#CMD ["--help"]

# Expose port
EXPOSE 8000/udp