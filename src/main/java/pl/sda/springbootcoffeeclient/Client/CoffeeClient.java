package pl.sda.springbootcoffeeclient.Client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import pl.sda.springbootcoffeeclient.Objects.Coffee;
import pl.sda.springbootcoffeeclient.Objects.Place;

@Controller
public class CoffeeClient {
    RestTemplate restTemplate = new RestTemplate();
    Coffee coffee;
//    @EventListener(ApplicationReadyEvent.class)
//    public void start(){
//        addCoffies();
//        getCoffies();
//    }


    public Coffee[] getCoffies(){
        ResponseEntity<Coffee[]> exchange = restTemplate.exchange("https://coffee-api-db.herokuapp.com/api", HttpMethod.GET, HttpEntity.EMPTY, Coffee[].class);

        //Stream.of(exchange.getBody()).forEach(System.out::println); //metoda do wyswitlania w konsoli
        return exchange.getBody();
    }

    public void addCoffies(String name, String type){
        coffee=new Coffee();
        coffee.setNameCoffee(name);
        coffee.setTypeCoffee(type);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/json");
        HttpEntity httpEntity = new HttpEntity(coffee,httpHeaders);

        restTemplate.exchange("https://coffee-api-db.herokuapp.com/api/", HttpMethod.POST, httpEntity, Void.class);
    }

    public void delCoffee(Long id){
        restTemplate.exchange("https://coffee-api-db.herokuapp.com/api?id="+id, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
    }
}
