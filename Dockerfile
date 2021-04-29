FROM openjdk:15.0.1-jdk

COPY . /app
WORKDIR /app
ENV mysqlhost=127.0.0.1
RUN ./gradlew build
EXPOSE 7080/tcp
ENTRYPOINT java -jar ./build/libs/questionproject-1.0-all.jar
