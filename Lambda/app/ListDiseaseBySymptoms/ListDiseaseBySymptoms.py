import sys
import os
import logging
import pymysql
import json

def handler_name(event, context):
    rds_host  = os.environ['rds_host']
    name = os.environ['name']
    password = os.environ['password']
    db_name = os.environ['db_name']

    logger = logging.getLogger()
    logger.setLevel(logging.INFO)

    try:
        conn = pymysql.connect(rds_host, user=name, passwd=password, db=db_name, connect_timeout=5)
    except pymysql.MySQLError as e:
        logger.error("ERROR: Unexpected error: Could not connect to MySQL instance.")
        logger.error(e)
        sys.exit()

    logger.info("SUCCESS: Connection to RDS MySQL instance succeeded")
    
    sql = "SELECT DISTINCT(d.DISEASE_ID), d.COMMON_NAME, d.DISEASE_NAME, d.CAUSE FROM DISEASE d JOIN DISEASE_SYMPTOM ds ON ds.DISEASE_ID = d.DISEASE_ID JOIN SYMPTOM s on s.SYM_ID = ds.SYM_ID where s.SYM_DES LIKE %s order by d.COMMON_NAME;"
    with conn.cursor() as cur:
        cur.execute(sql, event['path'].split('/')[2].replace("+", " "))
        row_headers=[x[0] for x in cur.description]
        rows = cur.fetchall()
        jsonData = []
        for result in rows:
            jsonData.append(dict(zip(row_headers, result)))
    conn.commit()
    conn.close()
    

    return {
        "statusCode": 200,
        "body": json.dumps(jsonData)
    }