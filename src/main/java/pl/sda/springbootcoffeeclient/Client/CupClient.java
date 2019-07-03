package pl.sda.springbootcoffeeclient.Client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import pl.sda.springbootcoffeeclient.Objects.Coffee;
import pl.sda.springbootcoffeeclient.Objects.Cup;

@Controller
public class CupClient {
    RestTemplate restTemplate = new RestTemplate();
    Cup cup;

    public Cup[] getCup(){
        ResponseEntity<Cup[]> exchange = restTemplate.exchange("https://coffee-api-db.herokuapp.com/api/", HttpMethod.GET, HttpEntity.EMPTY, Cup[].class);

        //Stream.of(exchange.getBody()).forEach(System.out::println); //metoda do wyswitlania w konsoli
        return exchange.getBody();
    }

}
