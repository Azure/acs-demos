FROM biglittlechallenge/trials-base

COPY ./trials-ai /usr/src/app/trials-ai
WORKDIR /usr/src/app/trials-ai
RUN mvn -s /m2/maven-settings.xml package shade:shade

CMD ["java", "-jar", "/usr/src/app/trials-ai/target/trials-ai-0.0.1-SNAPSHOT.jar", "8888"]