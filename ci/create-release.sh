#!/usr/bin/env sh

set -e

RELEASE=$1
SNAPSHOT=$2

mvn versions:set -DnewVersion=$RELEASE -DgenerateBackupPoms=false
git add .
git commit --message "v$RELEASE Release"

git tag -s v$RELEASE -m "v$RELEASE"
git reset --hard HEAD^1

mvn versions:set -DnewVersion=$SNAPSHOT -DgenerateBackupPoms=false
git add .
git commit --message "v$SNAPSHOT Development"
