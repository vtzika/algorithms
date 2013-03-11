#!/bin/bash
while read -r system
do
    array+=("$system")

done < <(echo "select distinct(system) from queries;" | mysql -u root -pqwe123 -D150MarktVIP --skip-column-names)

for item in ${array[*]}
do
	echo $item
	while read vip query; 
	do     
		request="http://10.249.123.123:4242/query?Qy=RANK(${query})&Fl=AD_ID&Rk=1&Nr=1000&Sk=0&Hx=no"; 
		echo $request
		curl -d "y=1&x=3" $request -o /home/varvara/workspace/Results/similarItems/voyager/$item/$vip
	done < <(echo "select vip,voyFinalQuery from queries where system like '$item' ;" | mysql '150MarktVIP' -u root -pqwe123 --skip-column-names)
done

