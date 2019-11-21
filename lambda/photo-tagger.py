import boto3
import os
import sys
import uuid
from urllib.parse import unquote_plus
from botocore.vendored import requests

s3_client = boto3.client('s3')
rekognition_client = boto3.client('rekognition')
indexing_url = "Your URL"
headers = {'Content-Type': 'application/json; charset=utf-8'}

def lambda_handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = unquote_plus(record['s3']['object']['key'])

        response = rekognition_client.detect_labels(Image={'S3Object':{'Bucket':bucket,'Name':key}},
            MaxLabels=10)
        
        print('Detected labels for ' + key) 
        labels = []
        for label in response['Labels']:
            labels.append(label['Name'])
            print ("Label: " + label['Name'])
            print ("Confidence: " + str(label['Confidence']))
        
        send_to_es(key, labels)
            
def send_to_es(image_name, labels):
    document = {
        "image_name" : image_name,
        "labels" : labels
    }
    res = requests.post(indexing_url, headers=headers, json=document)
    print("request sent to es " + res.text)