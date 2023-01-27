#!/bin/sh

mkdir /tmp/provisioner.d

cp /tmp/setup/files/init.sh /init
cp /tmp/setup/files/authkeys.sh /usr/bin/authkeys
cp /tmp/setup/files/authkeys-install.sh /tmp/authkeys-install
cp /tmp/setup/files/provisioner.sh /tmp/provisioner

chmod +x /init
chmod +x /usr/bin/authkeys
chmod +x /tmp/authkeys-install
chmod +x /tmp/provisioner

sed -i 's/^#.*StrictModes .*$/StrictModes yes/g' \
/etc/ssh/sshd_config
sed -i 's/^#.*PubkeyAuthentication .*$/PubkeyAuthentication yes/g' \
/etc/ssh/sshd_config
sed -i 's/^#.*AuthorizedKeysCommand .*$/AuthorizedKeysCommand \/usr\/bin\/authkeys %u/g' \
/etc/ssh/sshd_config
sed -i 's/^#.*AuthorizedKeysCommandUser .*$/AuthorizedKeysCommandUser nobody/g' \
/etc/ssh/sshd_config

# FIX: Missing privilege separation directory: /run/sshd
mkdir /var/run/sshd
chmod 0755 /var/run/sshd
