FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ADD ./target/sample-kubernetes-0.0.1-SNAPSHOT.jar sample-kubernetes.jar
ENTRYPOINT ["java","-jar","/sample-kubernetes.jar"]