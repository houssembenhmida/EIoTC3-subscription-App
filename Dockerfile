FROM image-registry.openshift-image-registry.svc:5000/fcee/maven:3.8.6-openjdk-11 AS builder
WORKDIR /app
COPY ./src /app/src
COPY pom.xml /app/
RUN mvn clean package -Dmaven.test.skip=true

FROM image-registry.openshift-image-registry.svc:5000/fcee/openjdk:8
WORKDIR /app
COPY  --from=builder /app/target/iotapp-0.0.2.jar /app/app.jar
CMD [ "java","-jar","app.jar" ]
