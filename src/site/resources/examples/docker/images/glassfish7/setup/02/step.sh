#!/bin/sh

mkdir /var/glassfish
mkdir /var/glassfish/domains
mkdir /var/glassfish/nodes

mkdir -p /tmp/cache
wget -nv -P /tmp/cache https://download.eclipse.org/ee4j/glassfish/glassfish-7.0.0.zip
unzip /tmp/cache/glassfish-7.0.0.zip -d /usr/lib
rm /tmp/cache/glassfish-7.0.0.zip

sed -i 's@="../domains"@="/var/glassfish/domains"@g' \
/usr/lib/glassfish7/glassfish/config/asenv.conf
sed -i 's@="../nodes"@="/var/glassfish/nodes"@g' \
/usr/lib/glassfish7/glassfish/config/asenv.conf

useradd --shell /bin/bash --home-dir /var/glassfish glassfish

echo 'export PATH="$PATH:/usr/lib/glassfish7/bin"' >/var/glassfish/.bash_profile

chown -R glassfish:glassfish /var/glassfish
chown -R glassfish:glassfish /usr/lib/glassfish7
