#!/bin/sh

ids="3,4"
table="NMS.main"

qry="select id,data from $table where id in ($ids)"

echo "Executing the following query"
echo $qry

/usr/bin/mysql -u root << eof
$qry
eof
