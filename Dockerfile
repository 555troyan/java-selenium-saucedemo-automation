FROM maven:3.8.5-openjdk-17-slim

RUN apt-get update && apt-get install -y wget gnupg curl unzip --no-install-recommends \
    && wget -q -O - https://dl-ssl.google.com | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com stable main" >> /etc/apt/sources.list.d/google.list \
    && apt-get update && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

CMD ["mvn", "clean", "test", "-Dheadless=true"]
