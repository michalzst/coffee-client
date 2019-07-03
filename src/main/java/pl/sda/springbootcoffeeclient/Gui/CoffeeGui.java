package pl.sda.springbootcoffeeclient.Gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.EventTrigger;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sda.springbootcoffeeclient.Client.CoffeeClient;
import pl.sda.springbootcoffeeclient.Objects.Coffee;
import pl.sda.springbootcoffeeclient.Objects.Cup;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Route("get")
public class CoffeeGui extends VerticalLayout {
    private Label label;
    private CoffeeClient coffeeClient;
    private Button thumbsUpButton;
    private List<Coffee> coffeeList;
    private Grid<Coffee> grid;
    private ListDataProvider<Coffee> dataProvider;
    private Long id;
    private String columnId = "Id";
    private String columnName = "Name";
    private String columnType = "Type";
    private String columnIdCup = "Cup_id";

    @Autowired
    public CoffeeGui(CoffeeClient coffeeClient) {

        this.coffeeClient = coffeeClient;
        this.label = new Label();
//        thumbsUpButton = new Button(new Icon(VaadinIcon.DOWNLOAD));
//        thumbsUpButton.addClickListener(buttonClickEvent -> label.setText(Arrays.toString(coffeeClient.getCoffies())));
        this.coffeeList = Arrays.asList(coffeeClient.getCoffies());
        this.grid = new Grid<>();
        this.dataProvider = new ListDataProvider<>(coffeeList);

        grid.setDataProvider(dataProvider);

        Grid.Column<Coffee> idColumn = grid.addColumn((Coffee::getIdCoffee), columnId).setHeader(columnId);
        Grid.Column<Coffee> nameColumn = grid.addColumn(Coffee::getNameCoffee, columnName).setHeader(columnName);
        Grid.Column<Coffee> typeColumn = grid.addColumn(Coffee::getTypeCoffee, columnType).setHeader(columnType);
        //Grid.Column<Coffee> idCupColumn = grid.addColumn(Coffee::getCupId, columnIdCup).setHeader(columnIdCup);

        grid.setSelectionMode(Grid.SelectionMode.NONE);
        //grid.addItemClickListener(event -> event.getItem().getIdLong());
        grid.setHeightByRows(true);
        HeaderRow filterRow = grid.appendHeaderRow();

// First filter
        TextField idField = new TextField();
        idField.addValueChangeListener(event -> dataProvider
                .addFilter(coffee -> StringUtils.containsIgnoreCase(
                        String.valueOf(coffee.getIdCoffee()), idField.getValue())));

        idField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(idColumn).setComponent(idField);
        idField.setSizeFull();
        idField.setPlaceholder("Filter");

// Second filter
        TextField typeField = new TextField();
        typeField.addValueChangeListener(event -> dataProvider
                .addFilter(coffee -> StringUtils.containsIgnoreCase(
                        String.valueOf(coffee.getTypeCoffee()), typeField.getValue())));

        typeField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(typeColumn).setComponent(typeField);
        typeField.setSizeFull();
        typeField.setPlaceholder("Filter");

// Third filter
        TextField nameField = new TextField();
        nameField.addValueChangeListener(event -> dataProvider.addFilter(
                coffee -> StringUtils.containsIgnoreCase(coffee.getNameCoffee(),
                        nameField.getValue())));

        nameField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(nameColumn).setComponent(nameField);
        nameField.setSizeFull();
        nameField.setPlaceholder("Filter");

//// Four filter
//        TextField cupIdFiled = new TextField();
//        cupIdFiled.addValueChangeListener(event -> dataProvider.addFilter(
//                coffee -> StringUtils.containsIgnoreCase(String.valueOf(coffee.getCupId()), cupIdFiled.getValue())));
//
//        cupIdFiled.setValueChangeMode(ValueChangeMode.EAGER);
//
//        filterRow.getCell(idCupColumn).setComponent(cupIdFiled);
//        cupIdFiled.setSizeFull();
//        cupIdFiled.setPlaceholder("Filter");

        Button addButton = new Button("Add Item", event -> {
            coffeeClient.addCoffies("", "");
            // The dataProvider knows which List it is based on, so when you edit the list you edit the dataprovider.
            grid.setDataProvider(dataProvider = new ListDataProvider<>(Arrays.asList(coffeeClient.getCoffies())));
            grid.getDataProvider().refreshAll();
        });

        grid.addItemClickListener(event1 -> {
            label.setText("Selected item: " + event1.getItem().getIdCoffee() + " Name: " + event1.getItem().getNameCoffee() + " Type: " + event1.getItem().getTypeCoffee());
            id = Long.parseLong(String.valueOf(event1.getItem().getIdCoffee()));
            //grid.asMultiSelect().select();
        });

        Button removeButton = new Button("Remove Selected Item", event -> {
            Notification notification;
            if (id != null) {
                coffeeClient.delCoffee(id);
                // The dataProvider knows which List it is based on, so when you edit the list you edit the dataprovider.
                grid.setDataProvider(dataProvider = new ListDataProvider<>(Arrays.asList(coffeeClient.getCoffies())));
                grid.getDataProvider().refreshAll();
                notification = new Notification("Delete " + label.getText(), 5000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
                label.setText(null);
                id = null;
            } else {
                notification = new Notification("Nothing to deleted", 5000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
            }
        });

        FooterRow footerRow = grid.prependFooterRow();
        footerRow.getCell(idColumn).setComponent(addButton);
        footerRow.getCell(nameColumn).setComponent(removeButton);


        Checkbox multiSort = new Checkbox("Multiple column sorting enabled");
        multiSort.addValueChangeListener(
                event -> grid.setMultiSort(event.getValue()));

// you can set the sort order from server-side with the grid.sort method
        Button invertAllSortings = new Button(
                "Invert all sort directions", event -> {
            List<GridSortOrder<Coffee>> newList = grid.getSortOrder()
                    .stream()
                    .map(order -> new GridSortOrder<>(order.getSorted(),
                            order.getDirection().getOpposite()))
                    .collect(Collectors.toList());
            grid.sort(newList);
        });

        Button resetAllSortings = new Button("Reset all sortings",
                event -> grid.sort(null));

        FooterRow footerRow2 = grid.appendFooterRow();
        footerRow2.getCell(idColumn).setComponent(multiSort);
        footerRow2.getCell(nameColumn).setComponent(invertAllSortings);
        footerRow2.getCell(typeColumn).setComponent(resetAllSortings);

        Button viewBtn = new Button("Click me");
        viewBtn.addClickListener(ev -> {
                    BrowserWindowOpener opener = new BrowserWindowOpener(new ExternalResource("http://www.example.com"));
                    opener.setWindowName("_blank");
                });

        add(label);
//        add(thumbsUpButton);
        add(grid);
        add(viewBtn);
    }
}

