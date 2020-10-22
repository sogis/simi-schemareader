#!/bin/bash

export SPRING_APPLICATION_JSON='{"dbs":[{"key":"pub","url":"jdbc:postgresql://localhost:5432/postgres","user":"postgres","pass":"postgres"},{"key":"edit","url":"jdbc:postgresql://localhost:5432/postgres","user":"postgres","pass":"postgres"}]}'

if [ "$1" == "bg" ] #bg - background
  then
    PARA="-d"
  else
    PARA="-it"
fi

docker run $PARA \
    --name simi_schemareader \
    -e "SPRING_APPLICATION_JSON" \
    -p 8081:8080 \
    --rm \
    sogis/simi_schemareader
