#!/usr/bin/python3
import threading
from data_thread import DataThread

def heartbeats():
    print("Heartbeats:")
    print("\t1 - Regular")
    print("\t2 - Irregular")
    choice = int(input())
    if choice == 1 or choice == 2:
        set_biometrics("heart", choice)
        return
    else:
        print("Invalid choice.")

def blood_pressure():
    print("Blood pressure:")
    print("\t1 - Normal")
    print("\t2 - Low pressure")
    print("\t3 - High pressure")
    choice = int(input())
    if choice == 1 or choice == 2 or choice == 3:
        set_biometrics("blood", choice)
        return
    else:
        print("Invalid choice.")

def set_biometrics(key, value):
    biometrics[key] = value


if __name__ == '__main__':
    biometrics = {
        "heart": 1,
        "blood": 1
    }
    thread = DataThread(biometrics) 
    thread.start()

    while True:
        print("Select a menu:")
        print("\t1 - Heartbeats")
        print("\t2 - Blood pressure")
        choice = int(input())

        if choice == 1:
            heartbeats()

        elif choice == 2:
            blood_pressure()

        else:
            print("Invalid choice.")