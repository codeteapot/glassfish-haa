#!/bin/sh

code="setup"

asadmin \
--user admin \
--passwordfile $(/var/glassfish/passwords.sh $code get) \
create-domain example.net
/var/glassfish/passwords.sh $code remove
sleep 4

asadmin start-domain example.net
sleep 4

asadmin \
--user admin \
--passwordfile $(/var/glassfish/passwords.sh $code get) \
enable-secure-admin
/var/glassfish/passwords.sh $code remove
sleep 4

asadmin restart-domain example.net
sleep 4

asadmin \
--user admin \
--passwordfile $(/var/glassfish/passwords.sh $code get) \
create-cluster \
example
/var/glassfish/passwords.sh $code remove
