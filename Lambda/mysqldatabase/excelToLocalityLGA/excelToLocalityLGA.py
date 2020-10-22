import json
import boto3
import pandas as pd
import io
import mysql.connector
import os
import logging
import sys
from mysql.connector import errorcode


def lambda_handler(event, context):
    """
    This function initiates s3 client, then retrieves bucket_name and file_name and file_content
    
    After that, this function handles records deletion sql from given table names (LOCALITY and LGA)
    
    Finally this function uses sql insertion querry to insert records from data set which is uploaded to 'locality-lga' s3 bucket
    
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
        
        # read data (locality) into df with pandas
        locality = pd.read_excel(data, sheet_name='Locality')
        
        # read data (LGA) into df with pandas
        lga = pd.read_excel(data, sheet_name='LGA')
        
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
        
        # Delete all records from LGA table
        delete_LGA_querry = """DELETE FROM LGA """
        cur.execute(delete_LGA_querry)
        
        # Delete all records from LOCALITY table
        delete_LOCALITY_querry = """DELETE FROM LOCALITY """
        cur.execute(delete_LOCALITY_querry)
        
        # Update LOCALITY table
        locality = locality.where((pd.notnull(locality)), None)
        sql = """ INSERT INTO LOCALITY (LOCALITY_ID, SUBURB, LGA) VALUES (%s, %s, %s)"""
        for idx, row in locality.iterrows():
            locality_id = int(row['LOCALITY_ID'])
            suburb = row['SUBURB']
            locality_lga = row['LGA']
            cur.execute(sql,(locality_id, suburb, locality_lga))
            
        # Update LGA table
        lga = lga.where((pd.notnull(lga)), None)
        sql = """ INSERT INTO LGA (LGA_ID, LGA, PARK_NAME, CATEGORY, ADDRESS) VALUES (%s, %s, %s, %s, %s)"""
        for idx, row in lga.iterrows():
            lga_id = int(row['LGA_ID'])
            lga_lga = row['LGA']
            park_name = row['PARK_NAME']
            category = row['CATEGORY']
            address = row['ADDRESS']
            cur.execute(sql,(lga_id, lga_lga, park_name, category, address))
            
        conn.commit()
        cur.close()
        conn.close()
    
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
