import json

def handler_name(event, context):
    return{
        'statusCode': 200,
        'body': json.dumps('OK')
    }