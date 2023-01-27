FROM examples/debian10sshd

RUN [ "mkdir", "/tmp/setup" ]
ADD [ "setup/01/", "/tmp/setup/" ]
RUN [ "sh", "/tmp/setup/step.sh" ]
RUN [ "rm", "-r", "/tmp/setup" ]

RUN [ "mkdir", "/tmp/setup" ]
ADD [ "setup/02/", "/tmp/setup/" ]
RUN [ "sh", "/tmp/setup/step.sh" ]
RUN [ "rm", "-r", "/tmp/setup" ]

RUN [ "mkdir", "/tmp/setup" ]
ADD [ "setup/03/", "/tmp/setup/" ]
RUN [ "sh", "/tmp/setup/step.sh" ]
RUN [ "rm", "-r", "/tmp/setup" ]

ENTRYPOINT [ "/init" ]