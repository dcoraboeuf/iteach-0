#!/bin/bash

#############################
# Check environment variables
#############################

if [ "$BUILD_NUMBER" == "" ]
then
	echo BUILD_NUMBER is required.
	exit 1
fi

if [ "$MVN" == "" ]
then
	echo MVN is required.
	exit 1
fi

if [ "$NEXUS_URL" == "" ]
then
        echo NEXUS_URL is required.
        exit 1
fi

if [ "$NEXUS_ID" == "" ]
then
        echo NEXUS_ID is required.
        exit 1
fi

# Listing environment
echo "BUILD_NUMBER = ${BUILD_NUMBER}"
echo "MVN          = ${MVN}"
echo "NEXUS_URL    = ${NEXUS_URL}"
echo "NEXUS_ID     = ${NEXUS_ID}"

##########################
# General MVN options
##########################

export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=128m -Djava.net.preferIPv4Stack=true"

##########################
# Preparation of the build
##########################

# Gets the version number from the POM
VERSION=`${MVN} help:evaluate -Dexpression=project.version $MVN_OPTIONS | grep -E "^[A-Za-z\.0-9]+-SNAPSHOT$" | sed -e 's/\-SNAPSHOT//'`

# Release number is made of the version and the build number
RELEASE=${VERSION}-${BUILD_NUMBER}
echo Building release ${RELEASE}...


#####################################################
# Runs the build itself and as much tests as possible
#####################################################

# Clean
git checkout -- .

# Changing the versions
${MVN} versions:set -DnewVersion=${RELEASE} -DgenerateBackupPoms=false

# Maven build
${MVN} clean install
if [ $? -ne 0 ]
then
	echo Build failed.
	exit 1
fi

# Creates the TAR that contains:
# 1. the WAR
# 2. all the deployment files
echo Creating the archive...
mkdir target
tar --create --file=target/iteach-${RELEASE}.tar --directory=iteach-web/target iteach-web-${RELEASE}.war
tar --update --file=target/iteach-${RELEASE}.tar pipeline/*.*
if [ $? -ne 0 ]
then
	echo Archiving failed.
	exit 1
fi

###############################
# Upload the archive into Nexus
###############################

echo Uploading to Nexus @ ${NEXUS_URL} with id = ${NEXUS_ID}
${MVN} deploy:deploy-file -Dfile=target/iteach-${RELEASE}.tar -DrepositoryId=${NEXUS_ID} -Durl=${NEXUS_URL} -DgroupId=net.iteach -DartifactId=iteach -Dversion=${RELEASE} -DgeneratePom=true -Dpackaging=tar
if [ $? -ne 0 ]
then
	echo Deployment failed.
	exit 1
fi


##################################################################
# After the build is complete, create the tag in Git and pushes it
##################################################################

echo Tagging...

# We don't care about Jenkins tags
for TAG in $(git tag -l jenkins-myconfig*) ; do
	git tag -d $TAG
done

# Tag
TAG=iteach-${RELEASE}
# Tagging the build
git tag ${TAG}
# Pushing the result
git push origin ${TAG}

echo Done.
