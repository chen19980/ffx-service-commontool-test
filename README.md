# ffx-service-commontool 

## Install JDK 17

The Project must be executed in a JDK 17 environment by setting the JAVA_HOME environment variable

```shell
export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
```

## Install dependent artifacts in local maven repository

```shell
mvn install:install-file -Dfile=pom.xml -DgroupId=tw.com.firstbank.fcbcore -DartifactId=fcbframework -Dversion=1.0.0 -Dpackaging=pom -DpomFile=pom.xml
mvn install:install-file -Dfile=fcbframework-core\fcbframework-core-1.0.0.jar -DgroupId=tw.com.firstbank.fcbcore.fcbframework -DartifactId=fcbframework-core -Dversion=1.0.0 -Dpackaging=jar -DpomFile=fcbframework-core\pom.xml
mvn install:install-file -Dfile=fcbframework-core-extensions-transaction\fcbframework-core-extensions-transaction-1.0.0.jar -DgroupId=tw.com.firstbank.fcbcore.fcbframework -DartifactId=fcbframework-core-extensions-transaction -Dversion=1.0.0 -Dpackaging=jar -DpomFile=fcbframework-core-extensions-transaction\pom.xml
mvn install:install-file -Dfile=fcbframework-adapters-rest\fcbframework-adapters-rest-1.0.0.jar -DgroupId=tw.com.firstbank.fcbcore.fcbframework -DartifactId=fcbframework-adapters-rest -Dversion=1.0.0 -Dpackaging=jar -DpomFile=fcbframework-adapters-rest\pom.xml
mvn install:install-file -Dfile=fcbframework-adapters-rabbitmq\fcbframework-adapters-rabbitmq-1.0.0.jar -DgroupId=tw.com.firstbank.fcbcore.fcbframework -DartifactId=fcbframework-adapters-rabbitmq -Dversion=1.0.0 -Dpackaging=jar -DpomFile=fcbframework-adapters-rabbitmq\pom.xml
mvn install:install-file -Dfile=fcbframework-adapters-jms\fcbframework-adapters-jms-1.0.0.jar -DgroupId=tw.com.firstbank.fcbcore.fcbframework -DartifactId=fcbframework-adapters-jms -Dversion=1.0.0 -Dpackaging=jar -DpomFile=fcbframework-adapters-jms\pom.xml
mvn install:install-file -Dfile=fcbframework-adapters-soap\fcbframework-adapters-soap-1.0.0.jar -DgroupId=tw.com.firstbank.fcbcore.fcbframework -DartifactId=fcbframework-adapters-soap -Dversion=1.0.0 -Dpackaging=jar -DpomFile=fcbframework-adapters-soap\pom.xml
```

## Package ffx-service-commontool module

Packaging jar for all environments 

```shell
mvn clean package
```

## Run ffx-service-commontool

?????? **spring_profiles_active** ????????????????????????????????? application yaml

* spring_profiles_active=default (????????????)
* spring_profiles_active=dev (DEV??????) **default**
* spring_profiles_active=sit (SIT??????)
* spring_profiles_active=uat (UAT??????)
* spring_profiles_active=prod (PROD??????)

?????? **logging.console** ???????????????????????? ap log ?????????????????? console (???????????? application.yaml
????????????) 

* logging.console=true (????????? console)
* logging.console=false (????????? logstash) **default**

```shell
export spring_profiles_active=[dev|sit|uat|prod]
java -jar target/empty-service-1.0.0.jar
```
