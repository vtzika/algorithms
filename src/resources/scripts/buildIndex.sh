#!/bin/bash
parameterFiles="/home/varvara/workspace/externalSources/L1Indexes/395/*"
cd /home/varvara/workspace/externalSources/indri-5.3/buildindex/
i=0
for f in $parameterFiles
do
	i=$1+1
	IndriBuildIndex $f >/home/varvara/workspace/externalSources/indri-5.3/repositoriesL1/395/.$i 
done
