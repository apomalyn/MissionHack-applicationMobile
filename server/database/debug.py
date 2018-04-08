import sqlite3

sqlite_file = 'db.sqlite'

# Connecting to the database file
conn   = sqlite3.connect(sqlite_file)
cursor = conn.cursor()

for row in cursor.execute("SELECT * FROM doctors"):
  print(row)

# Commit and close
conn.commit()
conn.close()
