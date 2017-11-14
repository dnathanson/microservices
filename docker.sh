#!/bin/bash

set -e
set -x

SCRIPT_DIR=$(dirname $0)
. ${SCRIPT_DIR}/config.sh

VERSION_FROM_POM=$($MAVEN_HOME/bin/mvn -f "${SCRIPT_DIR}/pom.xml" org.apache.maven.plugins:maven-help-plugin:2.2:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)')
VERSION=${VERSION:-$VERSION_FROM_POM}

function build {
    for CONTAINER in $*; do
        CONTEXT_PATH="${SCRIPT_DIR}/microservices-netflix/${CONTAINER}"

        docker build "${CONTEXT_PATH}"
    done
}

function wait_push {
    while [ -n "$(ps aux |grep -v grep |grep -E 'docker .*push')" ]
    do
        echo "Docker is pushing another image to the registry. Sleeping 5s"
        sleep 5
    done
    docker push "$1"
}

function push {
    for CONTAINER in $*; do
        VERSION_TAG="${REGISTRY}/${REGISTRY_USER}/${CONTAINER}:${VERSION}"
        LATEST_TAG="${REGISTRY}/${REGISTRY_USER}/${CONTAINER}:latest"
        CONTEXT_PATH="${SCRIPT_DIR}/microservices-netflix/${CONTAINER}"

        if [ "$PUSH_LATEST" == "yes" ]; then
            LATEST_TAG_ARG="--tag='${LATEST_TAG}'"
        fi

        docker build --tag="${VERSION_TAG}" "${CONTEXT_PATH}"

        while [ -n "$(ps aux |grep -v grep |grep -E 'docker .*push')" ]
        do
            echo "Docker is pushing another image to the registry. Sleeping 5s"
            sleep 5
        done

        wait_push "${VERSION_TAG}"
        if [ "$PUSH_LATEST" == "yes" ]; then
            docker tag -f "${VERSION_TAG}" "${LATEST_TAG}"
            wait_push "${LATEST_TAG}"
        fi
    done
}

case $1 in
    build)
        if [ -z $2 ]; then
            build $CONTAINERS
        else
            shift
            build $*
        fi
        ;;
    push)
        if [ -z $2 ]; then
            push $CONTAINERS
        else
            shift
            push $*
        fi
        ;;
    *)
        echo usage $(basename $0) build [container ...]
        echo usage $(basename $0) push [container ...]
esac
