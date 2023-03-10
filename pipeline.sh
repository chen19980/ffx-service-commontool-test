#!/bin/sh

## This is a sample pipeline shell.
# please change global variables to meet your project settings.

CWD=$(pwd)

# fcb sonarqube setting
SONAR_URL_FCB=https://sourcescan.fcbcore-tt.com.tw
SONAR_TOKEN_FCB=sqp_7ab55b73bac396fb4e3dfc53342db4cbd44bbfa1
SONAR_PRJ_NAME=ffx-service-commontool
SONAR_PRJ_KEY=ffx-service-commontool

####
get_source() {
  echo $CWD
  pwd
}

fcb() {

  echo "RUN sonar"

  mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar \
    -Dsonar.projectKey=$SONAR_PRJ_KEY \
    -Dsonar.projectName=$SONAR_PRJ_NAME \
    -Dsonar.login=$SONAR_TOKEN_FCB \
    -Dsonar.host.url=$SONAR_URL_FCB \
    -Dmaven.wagon.http.ssl.insecure=true \
    -Dmaven.wagon.http.ssl.allowall=true \
    -Dmaven.wagon.http.ssl.ignore.validity.dates=true
    -Djavax.net.ssl.trustStore=/workspace/sonarqube.jks \
    -Djavax.net.ssl.trustStorePassword=${SONARQUBE_JKS_P} \
    --settings=/workspace/.m2/settings.xml
}

back_cwd() {
  echo $CWD
}

##### main
echo "DEV_ENV is assigned to ${DEV_ENV}"
get_source
fcb
back_cwd