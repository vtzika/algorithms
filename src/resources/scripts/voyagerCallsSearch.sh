#!/bin/bash
while read -r system
do
    array+=("$system")

done < <(echo "select distinct(system) from keywords;" | mysql -u root -pqwe123 -D150SearchKeywords --skip-column-names)

for item in ${array[*]}
do
	echo $item
	while read vip query; 
	do     
		request="http://10.249.123.123:4242/query?Qy=AND(${query})&Fl=AD_ID&Rk=1&Nr=10000000000&Sk=0&Hx=no"
		echo $request
		curl -d "y=1&x=3" $request -o /home/varvara/workspace/externalSources/Results/Voyager/SearchTopBlock/Unstemmed/voyFiles/TitleIndex/$vip
	done < <(echo "select id,newQuery from keywords where system like '$item' ;" | mysql '150SearchKeywords' -u root -pqwe123 --skip-column-names)
done

