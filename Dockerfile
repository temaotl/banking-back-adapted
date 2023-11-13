# Use an oracle java runtime as a base image
FROM openjdk:17-oraclelinux7

# pointing "/tmp" to home/docker because that is where a Spring Boot application creates
# working directories for Tomcat by default.
VOLUME /tmp

# Copy the current jar into the container as app.jar
ADD target/app.jar app.jar

# command to "touch" the jar file so that it has a file modification time
# Docker creates all container files in an "unmodified" state by default, but any static
# content (e.g. "index.html") would require the file to have a modification time.
RUN sh -c 'touch /app.jar'

# Define environment variable
ENV JAVA_OPTS="-Dspring.profiles.active=\${SPRING_PROFILES_ACTIVE} -Duser.language=cs -Duser.country=CZ -agentlib:jdwp=transport=dt_socket,address=*:8000,server=y,suspend=n"

# Make port 8080 available to the world outside this container
EXPOSE 8080 8000
#EXPOSE 8000

# The project JAR file is ADDed to the container as "app.jar" and then executed in the ENTRYPOINT
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]
