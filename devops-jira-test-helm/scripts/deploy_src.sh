#!/bin/bash
# uncomment to debug the script
# set -x
# copy the script below into your app code repo (e.g. ./scripts/deploy_helm.sh) and 'source' it from your pipeline job
#    source ./scripts/deploy_helm.sh
# alternatively, you can source it from online script:
#    source <(curl -sSL "https://raw.githubusercontent.com/open-toolchain/commons/master/scripts/deploy_helm.sh")
# ------------------

# Perform a helm deploy of container image and check on outcome

# source: https://raw.githubusercontent.com/open-toolchain/commons/master/scripts/deploy_helm.sh
# Input env variables (can be received via a pipeline environment properties.file.
export IMAGE_NAME=jira-iks-test
export IMAGE_TAG=cpt-act-hank
export REGISTRY_URL=us.icr.io
export REGISTRY_NAMESPACE=devops-jira
export CLUSTER_NAMESPACE=jira-iks-test
