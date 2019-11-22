from botocore.vendored import requests

host = 'Your ES Url'

headers = {'Content-Type': 'application/json; charset=utf-8'}

def lambda_handler(event, context):
    document = {
        "image_name": event["image_name"],
        "labels": event["labels"]
    }
    res = requests.post(host, headers=headers, json=document)
    print(res.text)