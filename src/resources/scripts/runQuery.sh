#!/bin/bash
# start Indri, it will run a set of parameter files (search) 
# parameter file contains: 1 or more queries, index, query ID, ...
parameterFiles="/home/varvara/workspace/externalSources/ParameterFiles/VIP/Unstemmed/descrTop/DescrIndex/top4Title/*"

cd /home/varvara/workspace/externalSources/indri-5.3/runquery
for f in $parameterFiles
do
	IndriRunQuery $f
done
