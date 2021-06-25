FROM openjdk:15.0.1-jdk

COPY . /app
WORKDIR /app
ENV mysqlhost=127.0.0.1
# RUN ./gradlew build
EXPOSE 7080/tcp
ENTRYPOINT java -jar ./build/libs/fblaserver-1.0-all.jar
