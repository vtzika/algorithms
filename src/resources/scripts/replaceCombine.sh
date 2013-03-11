#!/bin/sh
# replace combine to use this in Voyager or in console commands of Indri
cd /home/varvara/workspace/externalSources/ParameterFiles/VIP/Unstemmed/TitleIndex/TitleDescr/okapi/
for file in $(grep -il "combine" *)
do
echo $file
sed -e "s/#combine(//ig" $file > tempfile.tmp
mv tempfile.tmp $file
done
