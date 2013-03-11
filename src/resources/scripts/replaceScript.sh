#!/bin/sh
# change the retrieval method in the parameter file(s)
cd /home/varvara/workspace/externalSources/ParameterFiles/VIP/Unstemmed/EntireDocIndex/DiscrTermsEntire/tfidf/
for file in $(grep -il "okapi" *)
do
echo $file
sed -e "s/okapi,k1:1.0,b:0.3/tfidf,k1:1.2,b:0.75/ig" $file > tempfile.tmp
mv tempfile.tmp $file
done
