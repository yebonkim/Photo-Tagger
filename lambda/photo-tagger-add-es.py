from botocore.vendored import requests
import json

host = '<Your ES Address>'
indexing_host = host + "/photo_tag/_doc"

headers = {'Content-Type': 'application/json; charset=utf-8'}

def lambda_handler(event, context):
    document = {
        "image_name": event["image_name"],
        "labels": event["labels"]
    }
    if check_duplicate(event["image_name"]) is False :
        res = requests.post(indexing_host, headers=headers, json=document)
        print(res.text)
    
def check_duplicate(image_name):
    document = {
        "query" : {
            "match" : {
                "image_name" : image_name
            }
        }
    }
    res = requests.get(host + "/photo_tag/_search/"+image_name, headers=headers, json=document)
    res_json = json.loads(res.text)
    res_count = 0
    if "hits" in res_json and "hits" in res_json["hits"]:
        res_count = len(res_json["hits"]["hits"])
    
    if res_count > 0:
        return True
    else:
        return False