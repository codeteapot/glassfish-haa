#!/bin/sh

PROVISIONER_DIR=/tmp/provisioner.d

for PROVISIONER_USERDIR in $PROVISIONER_DIR/*
do
  PROVISIONER_USER=$(basename $PROVISIONER_USERDIR)
  if [ "$(ls -A $PROVISIONER_DIR/$PROVISIONER_USER 2>>/dev/null)" ]
  then
    for PROVISIONER_SCRIPT in $PROVISIONER_DIR/$PROVISIONER_USER/*.sh
    do
      su -l $PROVISIONER_USER -c $PROVISIONER_SCRIPT
    done
  fi
done
