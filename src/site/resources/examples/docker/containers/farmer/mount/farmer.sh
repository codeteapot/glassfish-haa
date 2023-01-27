#!/bin/sh

chmod 700 ~/.ssh
echo -e "Host *\n\tStrictHostKeyChecking no\n\n" >~/.ssh/config
chmod 600 ~/.ssh/id_rsa 2>/dev/null \
|| echo 'Could not change permissions of private key. Ignoring...' 

ssh_result=$(ssh -l glassfish glassfish-das echo 'success' 2>/dev/null)
while [ "$ssh_result" != "success" ]
do
  echo 'SSH connection to glassfish-das is not available yet. Retrying...'
  sleep 1
  ssh_result=$(ssh -l glassfish glassfish-das echo 'success' 2>/dev/null)
done

scp /tmp/admin-setup.sh glassfish@glassfish-das:/tmp/farmer.sh
ssh -l glassfish glassfish-das 'source ~/.bash_profile && /tmp/farmer.sh && rm /tmp/farmer.sh'
