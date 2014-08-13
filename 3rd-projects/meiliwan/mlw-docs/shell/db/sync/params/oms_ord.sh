#!/bin/bash

SRC_SOCKET_FILE=/data/dbs/3306file/mysql.sock

#db name
SRC_DB=mlw_order

#TIME_FIELD used to determine to export data
TIME_FIELD=update_time

#export fields, can use '*' to export all fields
SRC_FILEDS=" order_id,order_type,real_pay_amount,order_sale_amount,order_pay_amount,favorable_total_amount,transport_fee,is_invoice,total_item,bill_type,client_id,uid,user_name,order_status,recv_addr_id,recv_name,pay_time,update_time,create_time,state,exception_code,stock_time,print_pick_count,print_send_count,remark_count "

TARGET_IP=10.249.15.141
TARGET_PORT=3306
TARGET_USER=13mlw
TARGET_PASSWD=mlwhappy543
TARGET_DB=mlw_dc_base

