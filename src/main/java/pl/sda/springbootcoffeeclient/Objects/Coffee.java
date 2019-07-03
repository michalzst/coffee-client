package pl.sda.springbootcoffeeclient.Objects;

public class Coffee {

    private Long idCoffee;
    private String nameCoffee;
    private String typeCoffee;

    public Coffee(){}

    public Long getIdCoffee() {
        return idCoffee;
    }

    public void setIdCoffee(Long idCoffee) {
        this.idCoffee = idCoffee;
    }

    public String getNameCoffee() {
        return nameCoffee;
    }

    public void setNameCoffee(String nameCoffee) {
        this.nameCoffee = nameCoffee;
    }

    public String getTypeCoffee() {
        return typeCoffee;
    }

    public void setTypeCoffee(String typeCoffee) {
        this.typeCoffee = typeCoffee;
    }
}
