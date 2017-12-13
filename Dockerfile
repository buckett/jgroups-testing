FROM java:9-jdk

RUN mkdir /opt/app
WORKDIR /opt/app
COPY /target/jgroups-testing-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/app/app.jar

CMD java -jar app.jar jgroups-config.xml
