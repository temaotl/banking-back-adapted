# Sample Spring boot project

Výchozí spring boot projekt. Obsahuje základní třídy a konfiguraci databáze, swaggeru a bezpečnosti.

## Předpoklady
1. Nainstalované IDE, doporučujeme https://www.jetbrains.com/idea/
1. Nainstalovaná java 17 nebo vyšší
1. Nainstalovaná PostgreSQL 13 nebo vyšší
   1. vytvořený uživatel _sample_ s heslem _sample_ a právy _login, superuser_  
   1. vytvořená databáze **sample** s vlastníkem _sample_
1. Apache maven https://maven.apache.org/ v případě buildu a spuštění z command line

## Sestavení aplikace v IDE
Projekt - build projekt v IDE

## Spuštění aplikace v IDE
Spuštění Spring Boot applikace _SampleProjectApplication_ v IDE. Nezapomeňte přidat spring profil **dev**

## Sestavení aplikace pomocí maven
1`mvn clean install -DskipTests`

## Spuštění aplikace z command line
1`java -Dspring.profiles.active=dev -jar target/app.jar`

## Ověření že aplikace běží
http://localhost:8080/sample-be/swagger-ui/index.html
