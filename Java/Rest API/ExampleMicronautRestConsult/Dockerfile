FROM openjdk:14-alpine
COPY build/libs/RestConsult-*-all.jar RestConsult.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "RestConsult.jar"]