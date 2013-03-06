#!/bin/bash
while read -r system
do
    array+=("$system")

done < <(echo "select distinct(system) from voyRequests;" | mysql -u root -pqwe123 -Dtests --skip-column-names)

for item in ${array[*]}
do
	echo $item
	while read id request; 
	do     
		curl -d "y=1&x=3" $request -o /home/varvara/workspace/Results/similarItems/voyager/$item/$id
		echo /home/varvara/workspace/Results/similarItems/voyager/$item/$id
	done < <(echo "select id,request from voyRequests where system like '$item' ;" | mysql 'tests' -u root -pqwe123 --skip-column-names)
done

