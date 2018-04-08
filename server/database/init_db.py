import sqlite3

sqlite_file = 'db.sqlite'

# Connecting to the database file
conn   = sqlite3.connect(sqlite_file)
cursor = conn.cursor()

cursor.execute("DROP TABLE IF EXISTS settings")
cursor.execute("DROP TABLE IF EXISTS doctors")
cursor.execute("DROP TABLE IF EXISTS alerts")

# create users table
cursor.execute("""
  CREATE TABLE settings (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    value TEXT NOT NULL
  )
""")

cursor.execute("""
  INSERT INTO settings (name, value)
  VALUES ('version', '1.0.0')
""")

# create users table
cursor.execute("""
  CREATE TABLE doctors (
    id INTEGER PRIMARY KEY,
    identifier TEXT NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  )
""")

# create users table
cursor.execute("""
  CREATE TABLE alerts (
    id INTEGER PRIMARY KEY,
    location TEXT NOT NULL,
    alert_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  )
""")

# Commit and close
conn.commit()
conn.close()