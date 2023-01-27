#!/bin/sh

mkdir -p $HOME/.ssh
ssh-keygen \
  -t rsa \
  -b 4096 \
  -m pem \
  -N '' \
  -f $HOME/.ssh/id_rsa >/dev/null
 