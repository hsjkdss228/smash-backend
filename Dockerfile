FROM eclipse-temurin:18

WORKDIR /u/myapp

COPY build/libs/*[^plain].jar ./

CMD java -Dserver.port=8000 -Dspring.profiles.active=production -jar *.jar
