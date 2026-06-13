#!/bin/sh
# Gradle wrapper – auto-downloads Gradle if not present
GRADLE_HOME="${HOME}/.gradle/wrapper/dists"
GRADLE_VERSION="8.4"
GRADLE_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"

exec gradle "$@" 2>/dev/null || {
  echo "Downloading Gradle ${GRADLE_VERSION}..."
  mkdir -p "${GRADLE_HOME}"
  curl -L "${GRADLE_URL}" -o "/tmp/gradle.zip"
  unzip -q /tmp/gradle.zip -d "${GRADLE_HOME}"
  "${GRADLE_HOME}/gradle-${GRADLE_VERSION}/bin/gradle" "$@"
}
