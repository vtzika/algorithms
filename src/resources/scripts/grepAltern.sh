#!/bin/bash
while read -r orig_id
do
    array+=($orig_id)
done < <(echo "select distinct(luckyNo) from logs;" | mysql -u root -pqwe123 -DResults --skip-column-names)



for item in ${array[*]}
do
	echo $item
	grep $item /home/varvara/workspace/external/searchLogsVarvara/searchLogs/logFiles/* >/home/varvara/workspace/external/searchLogsVarvara/searchLogs/LuckyNologFiles/$item.txt
done
