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
    
    with conn.cursor() as cur:
        sql = "SELECT d.DISEASE_ID, d.DISEASE_NAME, s.SYM_DES FROM DISEASE d JOIN DISEASE_SYMPTOM ds ON ds.DISEASE_ID = d.DISEASE_ID JOIN SYMPTOM s on s.SYM_ID = ds.SYM_ID"
        cur.execute(sql)
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