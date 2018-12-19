FROM tomcat:8.5.35-jre11 

ADD ./target/*.war $CATALINA_HOME/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]
