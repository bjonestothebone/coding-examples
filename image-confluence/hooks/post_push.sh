#!/bin/bash

set -e

# Parse image name for repo name
tagStart=$(expr index "$IMAGE_NAME" :)  
repoName=${IMAGE_NAME:0:tagStart-1}

# Tag and push image for each additional tag
for tag in `git tag -l --points-at HEAD`; do  
    docker tag $IMAGE_NAME ${repoName}:${tag}
    docker push ${repoName}:${tag}
done
