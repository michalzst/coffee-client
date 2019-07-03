package pl.sda.springbootcoffeeclient.Objects;

public class Cup {

    private Long id;
    private Size size;

    public Cup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
