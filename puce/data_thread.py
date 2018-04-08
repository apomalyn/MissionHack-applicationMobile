from bluetooth import *
import threading
import base64
import time
import json

class DataThread(threading.Thread):

    def __init__(self, biometrics):
        threading.Thread.__init__(self)
        self.biometrics = biometrics

    def run(self):
        addr  = "94:65:2D:C6:EF:AA"
        uuid  = "c1c3b6c6-8ca2-4ef2-94e4-805e0f6cfe93"
        match = find_service(uuid = uuid, address = addr)
        if len(match) < 1:
            print("DEVICE NOT FOUND")
            exit(1)
        match = match[0]
        port = match["port"]
        host = match["host"]
        
        # Create the client socket
        sock = BluetoothSocket( RFCOMM )
        sock.connect((host, port))

        while True:
            data = generate_data(self.biometrics)
            data_json = json.dumps(data)
            data_b64  = base64.b64encode(data_json.encode('utf8'))
            sock.send(data_b64)
            time.sleep(5)


def generate_data(biometrics):
    if biometrics["heart"] == 1: # normal
        heartbeats = 100
    else: # heart attack
        heartbeats = 200

    if biometrics["blood"] == 1: # normal
        blood_pressure = [70, 110]
    elif biometrics["blood"] == 2: # low_pressure
        blood_pressure = [50, 80]
    else: # high_pressure
        blood_pressure = [95, 170]

    data = {
        "heartbeat": heartbeats,
        "systolic":  blood_pressure[0],
        "diastelic": blood_pressure[1]
    }
    return data