#!/bin/bash

export ARTIFACTORY_DEPLOY="false"

./gradlew buildPlugin
./gradlew patchPluginXml
ls build/distributions
./gradlew publishPlugin
