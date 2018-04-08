from flask import Flask, request
from symptom_checker import SymptomChecker
import requests
import sqlite3 as sql
import base64
import json

app = Flask(__name__)
db = "database/db.sqlite"

diag_checker = SymptomChecker()
diag_checker.train_model()

@app.route('/diagnostic', methods=['POST'])
def diagnostic():
    try:
        ids  = request.get_json()["ids"]
        diagnostic = diag_checker.ml_diagnostic(ids)[0]
        response = {"diagnostic": diag_checker.id_to_diag(diagnostic)}
        return json.dumps(response)
    except Exception as e:
        return json.dumps({"error": str(e)})


@app.route('/register', methods=['POST'])
def register():
    try:
        data    = request.get_json()
        role    = data["role"]
        id      = data["identifier"]

        response = {"alerts": []}

        if role == "ROLE_DOCTOR":
            with sql.connect(db) as con:
                con.row_factory = sql.Row
                cursor = con.cursor()

                # remove all old alerts
                cursor.execute("DELETE FROM alerts WHERE (JulianDay() - JulianDay(alert_time)) * 24 * 60 >= 15")

                cursor.execute("SELECT * FROM doctors WHERE identifier = ?", (id,))
                if cursor.fetchone() == None:
                    cursor.execute("INSERT INTO doctors (identifier) VALUES (?)", (id,))

                cursor.execute("SELECT * FROM doctors WHERE identifier = ?", (id,))
                doctor = cursor.fetchone()
                cursor.execute("SELECT location FROM alerts WHERE alert_time > ?", (doctor["last_updated"],))
                alerts = cursor.fetchall()
                for alert in alerts:
                    response["alerts"].append(alert["location"])


                cursor.execute("UPDATE doctors SET last_updated = CURRENT_TIMESTAMP WHERE identifier = ?", (id,))
                con.commit()
                return json.dumps(response)
            return json.dumps({"error": "Invalid request"}) # yuk, ugly code
        return json.dumps(response)
    except Exception as e:
        return json.dumps({"error": str(e)})



@app.route('/alert', methods=['POST'])
def alert():
    try:
        location = request.get_json()["location"]
        with sql.connect(db) as con:
            con.row_factory = sql.Row
            cursor = con.cursor()
            cursor.execute("INSERT INTO alerts (location) VALUES(?)", (location,))
            return json.dumps({"status": "RECEIVED"})
        return json.dumps({"error": "Error happened."})
    except Exception as e:
        return json.dumps({"error": str(e)})




if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)