#!/bin/sh

if [ -f /tmp/provisioner ]
then
  /tmp/provisioner
  # rm -r /tmp/provisioner.d
  rm /tmp/provisioner
fi

if [ -f /tmp/authkeys-install ]
then
  /tmp/authkeys-install
  rm /tmp/authkeys-install
fi

/usr/sbin/sshd -D
