#!/usr/bin/env python3

from os import walk, rename
import json


path = "../src/main/resources/assets/irlw/lang"

def translate_langfile(txt):
    res = {}
    for line in txt.split("\n"):
        line = line.strip()
        if line == "":
            continue
        try:
            key, value = line.split("=")
        except:
            print("Invalid line: " + line)
        res[key] = value
    return json.dumps(res, indent=4)


for root, dirs, files in walk(path):
    for fname in files:
        if fname.endswith(".lang"):
            fpath = root + "/" + fname
            print("Processing", fpath)
            with open(fpath, 'r') as f:
                data = f.read()
            data = translate_langfile(data)
            with open(fpath, 'w') as f:
                f.write(data)
            #rename(fpath, fpath[-5:] + ".json")

