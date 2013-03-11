#!/bin/bash
# Get search log records for a set of IPs
while read -r orig_id
do
    array+=($orig_id)
done < <(echo "select distinct(ip) from logs;" | mysql -u root -pqwe123 -DResults --skip-column-names)



for item in ${array[*]}
do
	echo $item
	grep $item /home/varvara/workspace/external/searchLogsVarvara/searchLogs/logFiles/* >/home/varvara/workspace/external/searchLogsVarvara/searchLogs/IplogFiles/$item.txt
done
