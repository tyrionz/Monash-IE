import sys
import os
import logging
import pymysql
from flask import Flask, request
import json

app = Flask(__name__)

#rds settings
def connectRDS():
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
    return conn
    

###Test
@app.route('/hello', methods=['GET'])
def index():
    return{
        'statusCode': 200,
        'body': 'OK'
    }

### Disease table
#List all disease
@app.route('/listDisease', methods=['GET'])
def listDisease():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from DISEASE")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()
        

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseByID/<DISEASE_ID>', methods=['GET'])
def listDiseaseByID(DISEASE_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE WHERE DISEASE_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, DISEASE_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseByName/<DISEASE_NAME>', methods=['GET'])
def listDiseaseByName(DISEASE_NAME):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE WHERE DISEASE_NAME = %s"
        with conn.cursor() as cur:
            cur.execute(sql, DISEASE_NAME)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseByArea/<AREA>', methods=['GET'])
def listDiseaseByArea(AREA):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE WHERE AREA = %s"
        with conn.cursor() as cur:
            cur.execute(sql, AREA)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }
            

### Disease_symptom table
#List all disease symptoms
@app.route('/listDiseaseSymptoms', methods=['GET'])
def listDiseaseSymptoms():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from DISEASE_SYMPTOM")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseSymptomsBySID/<SYM_ID>', methods=['GET'])
def listDiseaseSymptomsBySID(SYM_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE_SYMPTOM WHERE SYM_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, SYM_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseSymptomsByDID/<DISEASE_ID>', methods=['GET'])
def listDiseaseSymptomsByDID(DISEASE_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE_SYMPTOM WHERE DISEASE_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, DISEASE_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }
            

### Symptom table
@app.route('/listSymptom', methods=['GET'])
def listSymptom():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from SYMPTOM")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listSymptomByID/<SYM_ID>', methods=['GET'])
def listSymptomByID(SYM_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from SYMPTOM WHERE SYM_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, SYM_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listSymptomByDescription/<SYM_DES>', methods=['GET'])
def listSymptomByDescription(SYM_DES):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from SYMPTOM WHERE SYM_DES LIKE %s"
        with conn.cursor() as cur:
            cur.execute(sql, SYM_DES)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }
            

### Disease_task table
@app.route('/listDiseaseTask', methods=['GET'])
def listDiseaseTask():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from DISEASE_TASK")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseTaskByDID/<DISEASE_ID>', methods=['GET'])
def listDiseaseTaskByDID(DISEASE_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE_TASK WHERE DISEASE_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, DISEASE_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseTaskByTID/<TASK_ID>', methods=['GET'])
def listDiseaseTaskByTID(TASK_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE_TASK WHERE TASK_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, TASK_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }
            

### Task table
@app.route('/listTask', methods=['GET'])
def listTask():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from TASK")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listTaskByID/<TASK_ID>', methods=['GET'])
def listTaskByID(TASK_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from TASK WHERE TASK_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, TASK_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listTaskByDescription/<TASK_DES>', methods=['GET'])
def listTaskByDescription(TASK_DES):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from TASK WHERE TASK_DES LIKE %s"
        with conn.cursor() as cur:
            cur.execute(sql, TASK_DES)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }


### Disease_tip table
@app.route('/listDiseaseTip', methods=['GET'])
def listDiseaseTip():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from DISEASE_TIP")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseTipByDID/<DISEASE_ID>', methods=['GET'])
def listDiseaseTipByDID(DISEASE_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE_TIP WHERE DISEASE_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, DISEASE_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseTipByTID/<TIP_ID>', methods=['GET'])
def listDiseaseTipByTID(TIP_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE_TIP WHERE TIP_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, TIP_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listDiseaseTipBySeasonID/<SEASON_ID>', methods=['GET'])
def listDiseaseTipBySeasonID(SEASON_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from DISEASE_TIP WHERE SEASON_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, SEASON_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }


### Season table
@app.route('/listSeason', methods=['GET'])
def listSeason():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from SEASON")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listSeasonByID/<SEASON_ID>', methods=['GET'])
def listSeasonByID(SEASON_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from SEASON WHERE SEASON_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, SEASON_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listSeasonByName/<SEASON_NAME>', methods=['GET'])
def listSeasonByName(SEASON_NAME):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from SEASON WHERE SEASON_NAME LIKE %s"
        with conn.cursor() as cur:
            cur.execute(sql, SEASON_NAME)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }


### Tip table
@app.route('/listTip', methods=['GET'])
def listTip():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from TIP")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listTipByID/<TIP_ID>', methods=['GET'])
def listTipByID(TIP_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from TIP WHERE TIP_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, TIP_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listTipByDescription/<TIP_DES>', methods=['GET'])
def listTipByDescription(TIP_DES):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from TIP WHERE TIP_DES LIKE %s"
        with conn.cursor() as cur:
            cur.execute(sql, TIP_DES)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }


### Season_ent_data table
@app.route('/listSeasonENTData', methods=['GET'])
def listSeasonENTData():
    conn = connectRDS()
    if request.method == 'GET':
        with conn.cursor() as cur:
            cur.execute("select * from SEASON_ENT_DATA")
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listSeasonENTDataBySeasonID/<SEASON_ID>', methods=['GET'])
def listSeasonENTDataBySeasonID(SEASON_ID):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from SEASON_ENT_DATA WHERE SEASON_ID = %s"
        with conn.cursor() as cur:
            cur.execute(sql, SEASON_ID)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

@app.route('/listSeasonENTDataByAdmissionNumber/<ADMID_NUM>', methods=['GET'])
def listSeasonENTDataByAdmissionNumber(ADMID_NUM):
    conn = connectRDS()
    if request.method == 'GET':
        sql = "select * from SEASON_ENT_DATA WHERE ADMID_NUM = %s"
        with conn.cursor() as cur:
            cur.execute(sql, ADMID_NUM)
            row_headers=[x[0] for x in cur.description]
            rows = cur.fetchall()
            jsonData = []
            for result in rows:
                jsonData.append(dict(zip(row_headers, result)))
        conn.commit()
        conn.close()

        return {
            "statusCode": 200,
            "body": jsonData
        }

if __name__ == "__main__":
    app.run()