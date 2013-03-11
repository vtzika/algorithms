#!/bin/sh
# replace mistakes in some of the output files
cd /home/varvara/workspace/externalSources/ParameterFiles/L2aob/Unstemmed/EntireIndex/Browse/l1/
for file in $(grep -il "L1Indexes" *)
do
echo $file
sed -e "s/L1Indexes/indri-5.3\/repositoriesL1/ig" $file > tempfile.tmp
mv tempfile.tmp $file
done

