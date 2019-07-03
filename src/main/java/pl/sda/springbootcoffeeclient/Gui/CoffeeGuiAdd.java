package pl.sda.springbootcoffeeclient.Gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sda.springbootcoffeeclient.Client.CoffeeClient;


@Route("add")
public class CoffeeGuiAdd extends VerticalLayout {
    private Label label;
    private TextField textFieldName;
    private TextField textFieldType;
    private Button thumbsUpButton;
    private CoffeeClient coffeeClient;

    @Autowired
    public CoffeeGuiAdd(CoffeeClient coffeeClient) {
        this.coffeeClient = coffeeClient;
        this.label = new Label();
        this.textFieldName = new TextField();
        this.textFieldType = new TextField();
        this.thumbsUpButton = new Button(new Icon(VaadinIcon.ADD_DOCK));
        Image image = new Image("https://media.tenor.com/images/0f44d1960381f1729f3b6f995cccd857/tenor.gif", "nie ma obrazka :(");

        textFieldName.setLabel("Set name");
        textFieldType.setLabel("Set type");

        thumbsUpButton.addClickListener(buttonClickEvent -> {
            coffeeClient.addCoffies(textFieldName.getValue(),textFieldType.getValue());
            Notification notification = new Notification("Added new item: " + textFieldName.getValue() + " of " + textFieldType.getValue(), 5000);
                    notification.open();
                    add(image);
        });
        thumbsUpButton.addClickListener(buttonClickEvent ->label.setText("Zapisano "+textFieldName.getValue()+" "+textFieldType.getValue()));

        add(textFieldName);
        add(textFieldType);
        add(thumbsUpButton);
        add(label);
    }
}
