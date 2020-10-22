import requests

# request API based on Geolocation -37.8821;144.9821
response = requests.get("https://api.waqi.info/feed/geo:-37.8821;144.9821/?token=793555a685454d5bed6179dc3abe21b241a372ae")

data = response.json()
air_data = data.get('data')

aqi = air_data.get('aqi')

city_info = air_data.get('city')

main_pollutant = air_data.get('dominentpol')

all_pollutants = air_data.get('iaqi')

# Define 6 level of severity based on NSW guideline:
# https://www.health.nsw.gov.au/environment/air/Pages/aqi.aspx
def determine_severity(aiq):
    severity = ''
    if aqi >= 200:
        severity = 'Hazardous'
    elif aqi >= 150:
        severity = 'Very poor'
    elif aqi >= 100:
        severity = 'Poor'
    elif aqi >= 67:
        severity = 'Fair'
    elif aqi >= 34:
        severity = 'Good'
    elif aqi >= 0:
        severity = 'Very good'
    return severity

# Make suggestion based on sensitive group (Infants, Elderly, Allergy, Pregnant women)
def suggest_activity(severity):
    suggestion = ''
    if severity == 'Good' or severity == 'Very good':
        suggestion = 'Safe to go out with infants'
    elif severity == 'Fair':
        suggestion = 'Reduce or reschedule outdoor activities with infants'
    elif severity == 'Poor':
        suggestion = 'Not recommend outdoor activities with infants'
    elif severity == 'Very poor' or severity == 'Hazardous':
        suggestion = 'Reschedule outdoor activities with infants'
    return suggestion

# test code below by uncomment
# suggest_activity(determine_severity(aqi))
