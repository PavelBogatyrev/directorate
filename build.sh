#!/bin/bash

export APP=backend
export FRONTEND=frontend
export WIDGETS=widgets
# export WORKSPACE=`pwd`

# Prepare
cd ${WORKSPACE}/${WIDGETS}
npm install

# Compile widgets
webpack

# Copy widgets to frontend
cp ${WORKSPACE}/${WIDGETS}/build/build.js ${WORKSPACE}/${FRONTEND}/app/js/

# Copy frontend to Spring app
cd ${WORKSPACE}/${FRONTEND}/ && npm install
tar -czvf /tmp/app.tgz -C ${WORKSPACE}/${FRONTEND}/app/ .
tar -xzvf /tmp/app.tgz -C ${WORKSPACE}/${APP}/src/main/resources/static/
rm /tmp/app.tgz