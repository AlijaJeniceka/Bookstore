# Start with the official OpenJDK image
FROM openjdk:17-jdk-alpine

# Copy the built application JAR into the container
COPY target/Bookstore-0.0.1-SNAPSHOT.jar bookstore.jar

# Expose the application port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java","-jar","/bookstore.jar"]
