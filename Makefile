.PHONY: build run test clean

build:
	./mvnw clean package -DskipTests

run:
	java -jar target/*.jar

test:
	./mvnw test

clean:
	./mvnw clean
