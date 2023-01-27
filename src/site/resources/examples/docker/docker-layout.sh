#!/bin/bash

function docker_net_address() {
  docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $1
}

echo "DAS:       2201 -> $(docker_net_address docker_glassfish-das_1):22"
echo "Nodes 01:  2202 -> $(docker_net_address docker_glassfish-nodes-01_1):22"
echo "Nodes 02:  2203 -> $(docker_net_address docker_glassfish-nodes-02_1):22"

