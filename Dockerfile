FROM openjdk:8-jre-alpine
WORKDIR /app
COPY build/libs/*-SNAPSHOT.jar chat-api.jar



EXPOSE 8080

ENTRYPOINT ["java"]

CMD [ "-XX:+PrintFlagsFinal", "-XX:+UnlockExperimentalVMOptions", \
      "-XX:+UseCGroupMemoryLimitForHeap", "-XX:MaxRAMFraction=1", "-XshowSettings:vm", \
      "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/chat-api.jar" ]
