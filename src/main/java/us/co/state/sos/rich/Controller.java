package us.co.state.sos.rich;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;

import com.google.gson.Gson;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;


@RestController
@EnableAutoConfiguration
public class Controller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DatabaseReader reader = getDatabaseReader();
    private List chainrSpecJSON = JsonUtils.jsonToList(this.getClass().getClassLoader().getResourceAsStream("json/maxmind-jolt-shift-transform.json"));
    private Chainr chainr = Chainr.fromSpec( chainrSpecJSON );

    public Controller() throws IOException {
    }

    @RequestMapping(value = "/ip-lookup", params = {"ip"})
    private String getIpDetails(@RequestParam("ip") String ip) throws IOException, GeoIp2Exception {

        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = reader.city(ipAddress);

        Object transformedOutput = chainr.transform( JsonUtils.jsonToMap(response.toJson()));

        Gson gson = new Gson();
        return gson.toJson(transformedOutput);

    }

    private DatabaseReader getDatabaseReader() throws IOException {
        DatabaseReader reader;
        synchronized (this) {
            InputStream database = this.getClass().getClassLoader().getResourceAsStream("geoip/GeoIP2-City.mmdb");
            reader = new DatabaseReader.Builder(database).build();
        }
        return reader;
    }

}