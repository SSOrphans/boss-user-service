FROM openjdk
COPY ./boss-user/target/boss-user-0.0.1-SNAPSHOT.jar /app
CMD java -jar /app/boss-user-0.0.1-SNAPSHOT.jar