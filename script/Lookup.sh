#!/bin/bash
BUILD_DIR=/cul/src/SampleProject
export OPTS="-Dlog4j.configuration=file:./log4j.properties"
export CLASSPATH=`mvn -f ../pom.xml -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath"`
java -cp $CLASSPATH $OPTS edu.cornell.library.sample.Lookup
