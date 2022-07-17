FROM gradle:7.4.0-jdk11-alpine as build
LABEL stage=intermediate

WORKDIR /home/app
COPY . .
RUN gradle --no-daemon --build-cache -x test build


FROM openjdk:15
MAINTAINER www.digitalleague.ru

ENV TZ="Europe/Moscow"
ENV JAVA_OPTS="-Duser.timezone=$TZ -Dfile.encoding=UTF-8 -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"

ENV SPRING_PROFILE="default"
ENV APP_HOME /opt/somedir
ENV LOG_FOLDER="$APP_HOME/log"
ENV JAR_NAME=app.jar

VOLUME /tmp
VOLUME $APP_HOME
VOLUME $APP_HOME/log

WORKDIR $APP_HOME
COPY --from=build /home/app/build/libs/*.jar $JAR_NAME
RUN chmod -R 777 $APP_HOME

EXPOSE 80
CMD exec java $JAVA_OPTS \
    -Dlogs.base.dir=$LOG_FOLDER \
    -Dspring.profiles.active=$SPRING_PROFILE \
    -jar $JAR_NAME