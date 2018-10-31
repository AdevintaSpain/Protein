#!/bin/bash

if [ -n "$TRAVIS_TAG" ]; then
    ./gradlew createTweet
else
    echo "Tweet not created"
fi