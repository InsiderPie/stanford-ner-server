FROM amazoncorretto:17-alpine

RUN mkdir app
WORKDIR /app
RUN mkdir lib

# ./gradlew requires xargs
RUN microdnf install findutils

# Copy only the StanfordNERDownloader
COPY src/main/java/de/insiderpie/StanfordNERDownloader.java src/main/java/de/insiderpie/StanfordNERDownloader.java
# Compile and run the StanfordNERDownloader
RUN javac src/main/java/de/insiderpie/StanfordNERDownloader.java
RUN java -classpath src/main/java de.insiderpie.StanfordNERDownloader
RUN rm src/main/java/de/insiderpie/StanfordNERDownloader.{java,class}

# Copy the remaining source files
COPY . .
# Set the micronaut port to $PORT or 8080 as default
RUN printf "micronaut.server.host: 0.0.0.0\nmicronaut.server.port: ${PORT:=8080}\nmicronaut.application.name: stanfordNerServer\nnetty.default.allocator.max-order: 3" > src/main/resources/application.yml

ENTRYPOINT ./gradlew run
