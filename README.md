# GeoIP lookup REST service

This is a Java Spring RESTful service to lookup GeoIP attributes from the Maxmind binary database. Here's an example call:

    $ curl http://localhost:8080/ip-lookup?ip=165.127.77.1


This is the result:

    {
        "city": {
            "name": "Denver"
        },
        "continent": {
            "name": "North America",
            "code": "NA"
        },
        "country": {
            "name": "United States",
            "iso_code": "US"
        },
        "location": {
            "accuracy_radius": 10,
            "latitude": 39.7441,
            "longitude": -104.987,
            "metro_code": 751,
            "time_zone": "America/Denver"
        },
        "postal": {
            "code": "80290"
        },
        "registered_country": {
            "name": "United States",
            "iso_code": "US"
        },
        "subdivisions": [{
            "iso_code": "CO",
            "names": "Colorado"
        }],
        "traits": {
            "ip_address": "165.127.77.1",
            "is_anonymous_proxy": false,
            "is_legitimate_proxy": false,
            "is_satellite_provider": false
        }
    }

I ran a quick [siege](https://www.joedog.org/siege-manual/) test against this service on my laptop to get an idea of the throughput:

    $ siege http://localhost:8080/ip-lookup?ip=165.127.77.1 -c 100 -t 30s

    Lifting the server siege...
    Transactions:		       11417 hits
    Availability:		      100.00 %
    Elapsed time:		       29.91 secs
    Data transferred:	        5.57 MB
    Response time:		        0.01 secs
    Transaction rate:	      381.71 trans/sec
    Throughput:		        0.19 MB/sec
    Concurrency:		        3.41
    Successful transactions:       11417
    Failed transactions:	           0
    Longest transaction:	        0.10
    Shortest transaction:	        0.00

381 transactions per second on a late 2013 Macbook Pro.
