#!/bin/sh
#
# Shared functions for jbigi build scripts
#

detect_bits() {
  if [ -n "$BITS" ]; then
    return
  fi
  
  UNAME="$(uname -m)"
  if test "${UNAME#*x86_64}" != "$UNAME"; then
    BITS=64
  elif test "${UNAME#*i386}" != "$UNAME"; then
    BITS=32
  elif test "${UNAME#*i686}" != "$UNAME"; then
    BITS=32
  elif test "${UNAME#*armv6}" != "$UNAME"; then
    BITS=32
  elif test "${UNAME#*armv7}" != "$UNAME"; then
    BITS=32
  elif test "${UNAME#*aarch32}" != "$UNAME"; then
    BITS=32
  elif test "${UNAME#*aarch64}" != "$UNAME"; then
    BITS=64
  else
    echo "Unable to detect default setting for BITS variable" >&2
    exit 1
  fi
  printf "BITS variable not set, %s bit system detected\n" "$BITS" >&2
}

check_java_home() {
  if [ -z "$JAVA_HOME" ]; then
    SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
    SCRIPT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
    if [ -f "$SCRIPT_DIR/../find-java-home" ]; then
      . "$SCRIPT_DIR/../find-java-home"
    fi
  fi
  
  if [ ! -f "$JAVA_HOME/include/jni.h" ]; then
    echo "Cannot find jni.h! Looked in '$JAVA_HOME/include/jni.h'" >&2
    echo "Please set JAVA_HOME to a java home that has the JNI" >&2
    exit 1
  fi
}
