FROM openjdk:11
EXPOSE  8087
WORKDIR /app
ADD   ./target/*.jar /app/customer-service.jar
ENTRYPOINT ["java","-jar","/app/customer-service.jar"]
