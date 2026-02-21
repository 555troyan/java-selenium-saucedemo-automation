FROM markhobson/maven-chrome:jdk-17

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY . .

CMD ["mvn", "clean", "test", "-Dheadless=true"]
