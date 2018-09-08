FROM java:8
VOLUME /tmp
ADD maat-server-1.0-SNAPSHOT.jar maat-server.jar
RUN sh -c 'touch /maat-server.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /maat-server.jar" ]