FROM openjdk:11-slim

USER root

WORKDIR /home/schemareader
COPY tmp/schemareader.jar /home/schemareader

RUN cd /home/schemareader && \
    chown -R 1001:0 /home/schemareader && \
    chmod -R g+rw /home/schemareader && \
    ls -la /home/schemareader

USER 1001
EXPOSE 8080

#Log4j 2 CVE-2021-44228
ENV LOG4J_FORMAT_MSG_NO_LOOKUPS=true

CMD java -XX:MaxRAMPercentage=80.0 -jar schemareader.jar
