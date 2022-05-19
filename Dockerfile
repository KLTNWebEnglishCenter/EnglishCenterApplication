FROM openjdk:11
LABEL maintainer="englishcenterapplication"
ADD target/EnglishCenterApplication-0.0.1-SNAPSHOT.jar springboot-docker-englishcenterapplication.jar
ENTRYPOINT ["java","-jar","springboot-docker-englishcenterapplication.jar"]