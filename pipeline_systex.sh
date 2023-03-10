#!/bin/sh

## This is a sample pipeline shell.
# please change global variables to meet your project settings.

CWD=$(pwd)

GIT_USER_NAME="tapuser"
GIT_USER_EMAIL="tapuser@fstop.com.tw"

#systex sonarqube setting
SONAR_URL_FSTOP=http://10.109.110.203:9000
SONAR_TOKEN_FSTOP=sqp_97f45d542ba01ef212c2806f82f607591e22a279
SONAR_PRJ_NAME=ffx-service-commontool
SONAR_PRJ_KEY=ffx-service-commontool

####
get_source() {
  echo $CWD
  pwd
}

fstop() {

  git config --global user.name $GIT_USER_NAME
  git config --global user.email $GIT_USER_EMAIL

  echo "RUN Report"
  mvn -P fstop,systexdev --settings /workspace/.m2/settings.xml clean verify site site:stage scm-publish:publish-scm

  echo "RUN sonar"
  mvn -P fstop,systexdev sonar:sonar \
    -Dsonar.projectKey=$SONAR_PRJ_KEY \
    -Dsonar.projectName=$SONAR_PRJ_NAME \
    -Dsonar.login=$SONAR_TOKEN_FSTOP \
    -Dsonar.host.url=$SONAR_URL_FSTOP \
    -Dmaven.wagon.http.ssl.insecure=true \
    -Dmaven.wagon.http.ssl.allowall=true \
    -Dmaven.wagon.http.ssl.ignore.validity.dates=true
}

back_cwd() {
  echo $CWD
}

##### main
echo "DEV_ENV is assigned to ${DEV_ENV}"
get_source
fstop
back_cwd