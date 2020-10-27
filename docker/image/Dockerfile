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
CMD java -XX:MaxRAMPercentage=80.0 -jar schemareader.jar
