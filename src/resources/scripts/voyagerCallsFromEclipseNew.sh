#!/bin/bash
echo Varvaraaaaa
while read -r system
do

    array+=("$system")
	echo $array
done < <(echo "select distinct(system) from voyRequests where experiment='$2';" | mysql -u root -pqwe123 'algorithms' --skip-column-names)

for item in ${array[*]}
do
	echo $item
	while read id request; 
	do     
		curl -d "y=1&x=3" $request -o $1/$item
		echo $1/$item
	done < <(echo "select id,request from voyRequests where system like '$item' ;" | mysql 'algorithms' -u root -pqwe123 --skip-column-names)
done

