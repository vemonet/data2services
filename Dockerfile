FROM maven:3-jdk-8 
LABEL maintainer "Alexander Malic <alexander.malic@maastrichtuniversity.nl>"
ENV TMP_DIR /tmp
WORKDIR $TMP_DIR
# caching dependencies - this only runs if pom.xml changes
COPY pom.xml .
RUN mvn verify clean --fail-never
# buld process
COPY src/ ./src/
RUN mvn install

FROM tomcat:8.5.35-jre8
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=0 /tmp/target/*.war $CATALINA_HOME/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
