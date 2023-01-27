#!/bin/bash

pushd images

pushd debian10sshd
docker build -t examples/debian10sshd .
popd

pushd glassfish7
docker build -t examples/glassfish7 .
popd

popd
