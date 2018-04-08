#!/usr/bin/python3
import os

sqlite_file = 'db.sqlite'

try:
  os.remove(sqlite_file)
except:
  pass
