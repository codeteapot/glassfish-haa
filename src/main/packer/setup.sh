#!/bin/sh

mkdir /var/agent
useradd --shell /bin/bash --home-dir /var/agent agent
chown -R agent:agent /var/agent

mkdir $APPLICATION_LIB_DIR

cat << EOF >>/init
#!/bin/sh
java \
  -Dcom.sun.management.jmxremote.port=9999 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Djava.util.logging.config.file=/var/agent/logging.properties \
  -Dcom.github.codeteapot.glassfish.haa.config.file="\$GLASSFISH_AGENT_CONFIG" \
  -classpath $APPLICATION_LIB_DIR \
  -jar \
  $APPLICATION_LIB_DIR/${project.artifactId}-${project.version}.jar
  
# echo "Running ${project.name} ${project.version}..."
# java \
#   -Dcom.github.codeteapot.glassfish.haa.configurationFile="\$GLASSFISH_AGENT_CONFIG" \
#   -classpath $APPLICATION_LIB_DIR \
#   com.github.codeteapot.glassfish.haa.XMLAgent
EOF

chmod +x /init
