#!/bin/sh

code=$1
cmd=$2

case $cmd in

'get')

test -f "/tmp/passwords-$code" || cat << EOF >/tmp/passwords-$code.txt
AS_MASTER_PASSWORD=12345678
AS_ADMIN_PASSWORD=12345678
EOF
echo "/tmp/passwords-$code.txt"

;;

'remove')

rm "/tmp/passwords-$code.txt"

;;

esac
