#FROM openjdk:14-alpine as build
#COPY gradlew /build/
#COPY gradle /build/gradle
#WORKDIR build
#RUN sh ./gradlew --version
#ADD . .
#RUN sh ./gradlew clean build -x test
#CMD ["echo", "build conteiner"]

FROM openjdk:14-alpine as prod
COPY ./build/libs/MyLittleChangelog-all.jar /app/my-little-changelog.jar
WORKDIR app
CMD ["java","-jar","my-little-changelog.jar"]

FROM prod as dev
EXPOSE 5005
CMD ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","my-little-changelog.jar"]