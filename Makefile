local-compile:
	mvn clean compile

start:
	docker-compose up

stop:
	docker-compose down

test:
	mvn clean test

sonar:
	mvn clean test verify sonar:sonar

jacoco-report:
	mvn clean package

run-apps:
	mvn spring-boot:run
