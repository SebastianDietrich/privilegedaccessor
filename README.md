# Privileged Accessor
PrivilegedAccessor is a simple to use Java framework for accessing private methods, attributes, and constructors via reflection.
See https://sebastiandietrich.github.io/privilegedaccessor/ for usage instructions.

## Development instructions
[![Build Status](https://github.com/SebastianDietrich/privilegedaccessor/actions/workflows/maven.yml/badge.svg)](https://github.com/SebastianDietrich/privilegedaccessor/actions/workflows/maven.yml)
[![Coverage Status](https://coveralls.io/repos/github/SebastianDietrich/privilegedaccessor/badge.svg?branch=master)](https://coveralls.io/github/SebastianDietrich/privilegedaccessor?branch=master)
[![Stability: Maintenance](https://masterminds.github.io/stability/maintenance.svg)](https://masterminds.github.io/stability/maintenance.html)

With each java version internals become more and more restricted. E.g. final fields could be changed in previous versions of java. To find out what is possible with each version, we have excessive tests with a near 100% coverage. Nevertheless basics like accessing private fields or methods is and will be possible in all java versions.

## Publishing to Maven Central
See https://maven.apache.org/repository/guide-central-repository-upload.html and https://central.sonatype.org/publish/publish-guide/

To publish to the central repository you need to have the following:
* a jira account at sonatype (see https://issues.sonatype.org/browse/OSSRH-2978)
* gpg (e.g. https://www.gpg4win.org/thanks-for-download.html) in path
* a key matching the un/pw of the jira account at sonatype in gpg and uploaded to public key servers (e.g. http://pgp.mit.edu/)
* pom.xml prepared to be able to publish (done)
* global settings.xml prepared to know sonatype-nexus-snapshots and sonatype-nexus-staging servers (without passwords)
* local settings.xml holding passwords for those servers, passphrase for gpg key to sign artifacts and gpg.keyname for pom.xml to know which gpg key to take

### How to publish a SNAPSHOT release to sonatype central:
* simply run "mvn clean deploy" (e.g. from eclipse - no need to sign the jars)
remember that sonatype snapshots are not deployed to maven central. You need to add the snapshot repository to your Nexus, settings.xml, or pom.xml

### How to release a final release to maven central:
* be sure to have maven and gpg in path
* run the following commands from command-line, since they might need manual input of password for signing the jars
** cd to this projects directory and run:
** "mvn clean deploy"


