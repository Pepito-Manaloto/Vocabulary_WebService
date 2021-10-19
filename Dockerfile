ARG APP_DIR=/opt/app

FROM adoptopenjdk/openjdk11:alpine-jre as builder
ARG APP_DIR
WORKDIR ${APP_DIR}
COPY deployment/prod/config ./config
COPY deployment/db ./db
COPY target/vocabulary-api.jar vocabulary-api.jar

FROM adoptopenjdk/openjdk11:alpine-jre
LABEL maintainer="aaronjohn.asuncion@gmail.com"
ARG APP_DIR
WORKDIR ${APP_DIR}
RUN mkdir ${APP_DIR}/logs
RUN addgroup -S AaronGroup && adduser -S Aaron -G AaronGroup
RUN chown -R Aaron:AaronGroup ${APP_DIR}
USER Aaron
COPY --from=builder ${APP_DIR}/config/ ./config
COPY --from=builder ${APP_DIR}/db/ ./db
ARG APP_NAME=vocabulary-api.jar
COPY --from=builder ${APP_DIR}/${APP_NAME} ./${APP_NAME}
ENTRYPOINT ["java", "-jar", "vocabulary-api.jar"]