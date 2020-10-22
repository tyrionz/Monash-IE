import time, uuid , json
import urllib.request
import hmac, hashlib
from base64 import b64encode

class yahoo_api ( object ):
    def __init__ ( self ):
        self.consumer_secret = '43ece3de411c170adb7967ec70ffaf8d393d6790'
        self.url = 'https://weather-ydn-yql.media.yahoo.com/forecastrss'
        self.method = 'GET'
        self.app_id = 'PXinNbOg'
        self.consumer_key = 'dj0yJmk9QXF6V3Rlb3JReG9oJmQ9WVdrOVVGaHBiazVpVDJjbWNHbzlNQT09JnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTNj'
        self.concat = '&'        
    def process(self):
        #Prepare signature string (merge all params and SORT them
        merged_params = self.query.copy()
        merged_params.update(self.oauth)
        sorted_params = [k + '=' + urllib.parse.quote(merged_params[k], safe='') for k in sorted(merged_params.keys())]
        signature_base_str =  self.method + self.concat + urllib.parse.quote(self.url, safe='') + self.concat + urllib.parse.quote(self.concat.join(sorted_params), safe='')
        #Generate signature
        composite_key = urllib.parse.quote(self.consumer_secret, safe='') + self.concat
        oauth_signature = b64encode(hmac.new( bytes(composite_key , 'latin-1') , bytes(signature_base_str,'latin-1'), hashlib.sha1).digest())
        oauth_signature = oauth_signature.decode('utf-8')
        #Prepare Authorization header
        self.oauth['oauth_signature'] = oauth_signature
        auth_header = 'OAuth ' + ', '.join(['{}="{}"'.format(k,v) for k,v in self.oauth.items()])
        #Send request
        self.url = self.url + '?' + urllib.parse.urlencode(self.query)
        opener = urllib.request.build_opener()
        opener.addheaders = [('Authorization', auth_header),
                             ('X-Yahoo-App-Id', self.app_id),
                             ('Pragma', 'no-cache'),
                             ('User-Agent', 'Mozilla/5.0')]
        urllib.request.install_opener(opener)
        response = urllib.request.urlopen(self.url)
        data = response.read().decode('utf-8')
        del response
        del opener
        target = json.loads(data)
        return(target)

    def query_(self , location , unit = 'c'):
        # Location is the name of location (e.g. Moscow) , unit is the temp unit (C or F)
        # Returns a dictionary of weather data
        self.query = {'location': location, 'format': 'json' , 'u' : unit}
        self.url = 'https://weather-ydn-yql.media.yahoo.com/forecastrss'
        self.oauth = {
        'oauth_consumer_key': self.consumer_key,
        'oauth_nonce': uuid.uuid4().hex,
        'oauth_signature_method': 'HMAC-SHA1',
        'oauth_timestamp': str(int(time.time())),
        'oauth_version': '1.0'
        }
        return(self.process())

if __name__ == "__main__":
    obj = yahoo_api()
    #data = obj.query_("lat=37.372&lon=-122.038")
    data = obj.query_("Melbourne,AU")

my_city = data.get('location').get('city')
my_weather_info = data.get('current_observation')
my_temperature = my_weather_info.get('condition').get('temperature')
my_weather_condition = my_weather_info.get('condition').get('text')
