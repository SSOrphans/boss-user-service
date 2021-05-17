FROM maven as stage1
COPY ./ /app/boss-user-service
RUN cd /app/boss-user-service && mvn -q clean package

FROM openjdk
COPY --from=stage1 /app/boss-user-service /app/boss-user-service
CMD java -jar /app/boss-user-service/boss-user/target/boss-user-0.0.1-SNAPSHOT.jar