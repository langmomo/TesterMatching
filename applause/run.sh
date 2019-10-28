#!/usr/bin/env bash
mvn clean install
java -cp ./target/assign-1.0-SNAPSHOT-jar-with-dependencies.jar Match.MatchingFile