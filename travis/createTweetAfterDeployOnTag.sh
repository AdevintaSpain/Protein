#!/bin/bash

if [ -n "$TRAVIS_TAG" ] && [["$ARTIFACTORY_DEPLOY"=="false"]]; then
    ./gradlew createTweet
else
    echo "Tweet not created"
fi