#!/bin/sh
APPLICATION="qoober"
if [ -x jdk/bin/java ]; then
    JAVA=./jdk/bin/java
    JAR=./jdk/bin/jar
elif [ -x ../jdk/bin/java ]; then
    JAVA=../jdk/bin/java
    JAR=../jdk/bin/jar
else
    JAVA=java
    JAR=jar
fi
${JAVA} -cp classes qoober.tools.ManifestGenerator
/bin/rm -f ${APPLICATION}.jar
${JAR} cfm ${APPLICATION}.jar resource/qoober.manifest.mf -C classes . || exit 1
/bin/rm -f ${APPLICATION}service.jar
${JAR} cfm ${APPLICATION}service.jar resource/qooberservice.manifest.mf -C classes . || exit 1

echo "jar files generated successfully"