# Retourne le diagnostic le plus probable en
# fonction de tous les symptomes

/diagnostic

REQUEST:
{
    ids: [1, 2, 3, ...]
}

RESPONSE:
{
    "diagnostic": "Cancer"
}





# Retourne la location de toutes les alertes dont
# le docteur n'a pas encore été avertis.

/register

REQUEST
{
    "role": "ROLE_DOCTOR",
    "identifier": "super-secret-and-unique-id"
}

RESPONSE
{
    "alerts": ["location1", "location2"]
}





# Ajoute une alerte à la database. Les docteurs 
# seront avertis lors de leur prochain ping (/register)

/alert

REQUEST
{
    "location": "location xyz"
}


RESPONSE
{
    "status": "RECEIVED"
}
