import json
import boto3
import pandas as pd
import io
import mysql.connector
import os
import logging
import sys
from mysql.connector import errorcode


# define a function to delete records from selected tables
def delete_records(table, cursor):
    '''
    This function takes string as first input, connection.cursor() as second input
    
    After the inputs, this function execute this sql deletion querry: DELETE FROM table
    '''
    sql = """DELETE FROM %s """ % table
    cursor.execute(sql)


def lambda_handler(event, context):
    """
    This function initiates s3 client, then retrieves bucket_name and file_name and file_content
    
    After that, this function handles records deletion sql from given table names (all tables except LOCALITY and LGA)
    
    Finally this function uses sql insertion querry to insert records from data set which is uploaded to 'disease-dataset-except-localitylga' s3 bucket
    
    """

    
    # initiate s3 client
    s3 = boto3.client('s3')
    
    if event:
        s3_records = event['Records'][0]
        # bucket is the s3 bucket that is storing dataset for infantime's database
        bucket_name = str(s3_records['s3']['bucket']['name'])
    
        # data_file is the response of the s3 bucket.
        # data_file is the data set file with suffix of xlsx. In this case, it's 'Disease+table.xlsx'
        file_name = ' '.join(s3_records['s3']['object']['key'].split('+'))
    
        # get the object
        file_object = s3.get_object(Bucket=bucket_name, Key=file_name)
        # byte data in the data_file
        file_content = file_object['Body'].read()
    
        # read byte data with io
        data = io.BytesIO(file_content)
        
        # read data (SEASON) into df with pandas
        season = pd.read_excel(data, sheet_name='Season')
    
        # read data (season_ent_data) into df with pandas
        season_ent_data = pd.read_excel(data, sheet_name='Season_ENT_data')
        
        # read data (disease) into df with pandas
        disease = pd.read_excel(data, sheet_name='Disease')
        
        # read data (disease_symptom) into df with pandas
        disease_symptoms = pd.read_excel(data, sheet_name='Disease_Symptoms')
        
        # read data (symptom) into df with pandas
        symptom = pd.read_excel(data, sheet_name='Symptoms')
        
        # read data (tip) into df with pandas
        tip = pd.read_excel(data, sheet_name='Tip')
        
        # read data (area) into df with pandas
        area = pd.read_excel(data, sheet_name='Area')
        
        # read data (disease_season) into df with pandas
        disease_season = pd.read_excel(data, sheet_name='Disease_Season')
        
        # read data (disease_tip) into df with pandas
        disease_tip = pd.read_excel(data, sheet_name='Disease_Tip')

    
    
    
        # connect to MySQL
        rds_host  = os.environ['rds_host']
        name = os.environ['name']
        password = os.environ['password']
        db_name = os.environ['db_name']

        logger = logging.getLogger()
        logger.setLevel(logging.INFO)

        try:
            conn = mysql.connector.connect(host=rds_host, user=name, passwd=password, database=db_name, connect_timeout=5)
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
                print("Something is wrong with your user name or password")
            elif err.errno == errorcode.ER_BAD_DB_ERROR:
                print("Database does not exist")
            else:
                print(err)
                logger.error("ERROR: Unexpected error: Could not connect to MySQL instance.")
                logger.error(e)
                sys.exit()

        logger.info("SUCCESS: Connection to RDS MySQL instance succeeded")
        
        
        
        cur = conn.cursor()
        
        # This list contains all the table names that needs to perform record deletion (The table names are in the order of child tables, then parent tables)
        table_list = ['SEASON_ENT_DATA', 'DISEASE_SEASON', 'DISEASE_TIP',
                    'DISEASE_SYMPTOM', 'SEASON', 'AREA', 'TIP', 'SYMPTOM', 'DISEASE']
        
        # loop to delete all the records except Locality and LGA
        for table in table_list:
            delete_records(table, cur)
        
        
        
        

            
        # Update DISEASE table
        sql = """ INSERT INTO DISEASE (DISEASE_ID, DISEASE_NAME, CAUSE, COMMON_NAME) VALUES (%s, %s, %s, %s)"""
        for idx, row in disease.iterrows():
            disease_id = int(row['DISEASE_ID'])
            disease_name = row['DISEASE_NAME']
            cause = row['CAUSE']
            common_name = row['COMMON_NAME']
            cur.execute(sql,(disease_id,disease_name, cause, common_name))
        
        # Update AREA table
        sql = """ INSERT INTO AREA (AREA_ID, AREA_DESC) VALUES (%s, %s)"""
        for idx, row in area.iterrows():
            area_id = int(row['AREA_ID'])
            area_desc = row['AREA_DESC']
            cur.execute(sql,(area_id, area_desc))
            
        # Update Tip table
        sql = """ INSERT INTO TIP (TIP_ID, TIP_DES, TIP_NAME, PURPOSE, TYPE, `WHEN`, AGE) VALUES (%s, %s, %s, %s, %s, %s, %s)"""
        tip = tip.where((pd.notnull(tip)), None)
        for idx, row in tip.iterrows():
            tip_id = int(row['TIP_ID'])
            tip_des = row['TIP_DES']
            tip_name = row['TIP_NAME']
            purpose = row['PURPOSE']
            tyPe = row['TYPE']
            wHen = row['WHEN']
            age = int(row['AGE'])
            cur.execute(sql,(tip_id, tip_des, tip_name, purpose, tyPe, wHen, age))
            
            
        # Update SYMPTOM table
        sql = """ INSERT INTO SYMPTOM (SYM_ID, AREA_ID, SYM_DES) VALUES (%s, %s, %s)"""
        for idx, row in symptom.iterrows():
            sym_id = int(row['SYM_ID'])
            area_id = int(row['AREA_ID'])
            sym_des = row['SYM_DES']
            cur.execute(sql,(sym_id, area_id, sym_des))
            
        # Update SEASON table
        sql = """ INSERT INTO SEASON (SEASON_ID, SEASON_NAME) VALUES (%s, %s)"""
        for idx, row in season.iterrows():
            season_id = int(row['SEASON_ID'])
            season_name = row['SEASON_NAME']
            cur.execute(sql,(season_id, season_name))
            
            
        # Update DISEASE_TIP table
        sql = """ INSERT INTO DISEASE_TIP (DISEASE_ID, SEASON_ID, TIP_ID) VALUES (%s, %s, %s)"""
        for idx, row in disease_tip.iterrows():
            disease_id = int(row['DISEASE_ID'])
            season_id = int(row['SEASON_ID'])
            tip_id = int(row['TIP_ID'])
            cur.execute(sql,(disease_id, season_id, tip_id))
            
        # Update DISEASE_SEASON table
        sql = """ INSERT INTO DISEASE_SEASON (DISEASE_ID, SEASON_ID) VALUES (%s, %s)"""
        for idx, row in disease_season.iterrows():
            disease_id = int(row['DISEASE_ID'])
            season_id = int(row['SEASON_ID'])
            cur.execute(sql,(disease_id, season_id))
        
        # Update SEASON_ENT_DATA table
            
        sql = """ INSERT INTO SEASON_ENT_DATA (SEASON_ID, ADMID_NUM) VALUES (%s, %s)"""
        for idx, row in season_ent_data.iterrows():
            season_id = int(row['SEASON_ID'])
            admid_num = int(row['ADMID_NUM'])
            cur.execute(sql,(season_id,admid_num))
            
        # Update DISEASE_SYMPTOM table
        sql = """ INSERT INTO DISEASE_SYMPTOM (SYM_ID, DISEASE_ID) VALUES (%s, %s)"""
        for idx, row in disease_symptoms.iterrows():
            sym_id = int(row['SYM_ID'])
            disease_id = int(row['DISEASE_ID'])
            cur.execute(sql,(sym_id, disease_id))
            
            
        
        conn.commit()
        cur.close()
        conn.close()
    
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
