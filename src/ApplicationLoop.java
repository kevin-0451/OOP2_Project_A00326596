import Enums.Country;
import Products.DiscontinuedProduct;
import Products.PreOrderProduct;
import Products.Product;
import Sales.CountrySalesRecord;
import Sales.ProductSalesBatch;
import Sales.ProductSalesRecord;
import com.sun.source.tree.Tree;

import javax.swing.text.Keymap;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//DEMO - java.lang is imported by default. But we can import specific classes from it to make calling its methods easier
import static java.lang.System.out;

public final class ApplicationLoop implements Runnable {

    private boolean isRunning = true;
    private final Scanner scanner = new Scanner(System.in);

    //DEMO Lambdas using the Predicate functional interface
    //private Predicate<String> isYes = s -> (s.trim().toLowerCase().equals("y"));
    //private Predicate<String> isNo = s -> (s.trim().toLowerCase().equals("n"));
    private Consumer<String> output = x-> System.out.println(x);
    //private Consumer<String> output = System.out::println; //Method reference version
    private Stream<ProductSalesBatch> lastestData = null;
    private final String[] options = {
            "Lowest products sold [GroupBy String][Tree Map][Sum Int][Min]"
            , "Highest products sold [GroupBy String][Tree Map][Sum Int][Max]"
            , "Lowest revenue by country [GroupBy enum][TreeMap][Collectors Sum][min][comparing]"
            , "Highest revenue by country [GroupBy enum][TreeMap][Collectors Sum][max][comparing]"
            , "Sales from Germany [filter by enum][collect by String][TreeMap][Sum int][foreach]"
            , "Sales revenue from Spain [filter by enum][Map to double][sum]"
            , "First iPhone sales result [filter by String][find first]"
            , "First 10 products in sales batch [limit][for each]"
            , "Sorted revenue results by country [collect][group by enum][Tree map][sum double][sort by comparing lambda][foreach]"
            , "Top 5 countries by revenue [collect][Group by enum][Tree map][sum Double][sort][limit][foreach]"
            , "Show total revenue of each Pre-order and discontinued product [collect][partition by string contains][get TRUE][foreach]"
            , "Find any product with > €30k revenue [Map to Records][Group by product][Tree map][Sum Double][Filter][find any]"
            , "Show pre-order products by release date [Collect][Partition][Map to Product-LocalDateTime][Sort by date][Distinct][Output time duration]"
            , "Show discontinued products by discontinued date [Collect][Partition By][sorted Comparator][distinct][forEach]"
            , "Create sales record by country and output France's revenue [collect][group by][map]"
            , "Get count of countries with sales data [collect][GroupingBy][count]"
            , "Output Pre-order and discontinued products [map][distinct][sorted comparator][Switch pattern matching]"
            , "All match pre-order product [map][distinct][all match]"
            , "Any match discontinued product [map][distinct][any match]"
            , "None match product name [map][distinct][none match]"
            , "List all distinct products and their prices [map][distinct][sort][comparator][foreach]"
            , "Output Country and Revenue map [Collect][toMap]"


    };

    public void run(){
        String lastInput = "";
        output.accept("Welcome!");
        listOptions();
        boolean firstRun = true;
        while(isRunning){
            //DEMO - Enhanced Switch statement:
            try {
                try{
                    int temp = Integer.parseInt(lastInput) - 1;
                    if(temp < options.length){
                        output.accept(options[temp] + ": "); //Output the selected option.
                    }
                } catch (NumberFormatException e) {
                    //No need to do anything with the error. Any unsupported inputs will prompt the user to try again
                    //This try catch is to make it so that the code to output the selected option is in a single location
                }

                switch (lastInput) {
                    case "exit" -> {
                        isRunning = false;
                    }
                    case "1" -> { //"Lowest products sold [GroupBy String][Tree Map][Sum Int][Min]"
                            var res = lastestData.collect(Collectors.groupingBy(x -> x.getProduct().getProductName()
                                    , TreeMap::new
                                    , Collectors.summingInt(ProductSalesBatch::getQuantitySold)))
                                    .entrySet()
                                    .stream()
                                    .min(Comparator.comparing(x -> x.getValue()));
                            output.accept(res.get().getKey() + " has lowest sales with " + String.format("%,d",res.get().getValue()));
                    }
                    case "2" -> { //"Highest products sold [GroupBy String][Tree Map][Sum Int][Max]"
                            var res = lastestData.collect(Collectors.groupingBy(x -> x.getProduct().getProductName()
                                            , TreeMap::new
                                            , Collectors.summingInt(ProductSalesBatch::getQuantitySold)))
                                    .entrySet().stream()
                                    .max(Comparator.comparing(x -> x.getValue()));
                            output.accept(res.get().getKey() + " has highest sales with " + String.format("%,d",res.get().getValue()));
                    }
                    case "3" -> { //"Lowest revenue by country [GroupBy enum][TreeMap][Collectors Sum][min][comparing]"
                        var res = lastestData.collect(Collectors.groupingBy(x -> x.getCountry()
                                        , TreeMap::new
                                        , Collectors.summingDouble(ProductSalesBatch::getRevenue)))
                                .entrySet()
                                .stream()
                                .min(Comparator.comparing(x -> x.getValue()));

                        output.accept(res.get().getKey() + " has lowest revenue with €" + String.format("%,.2f",res.get().getValue()));
                    }
                    case "4" -> { //"Highest revenue by country [GroupBy enum][TreeMap][Collectors Sum][max][comparing]"
                        var res = lastestData.collect(Collectors.groupingBy(x -> x.getCountry()
                                        , TreeMap::new
                                        , Collectors.summingDouble(ProductSalesBatch::getRevenue)))
                                .entrySet()
                                .stream()
                                .max(Comparator.comparing(x -> x.getValue()));

                        output.accept(res.get().getKey() + " has highest revenue with €" + String.format("%,.2f",res.get().getValue()));
                    }
                    case "5" -> { //"Sales from Germany [filter by enum][collect by String][TreeMap][Sum int][foreach]"
                        lastestData.filter(x -> x.getCountry() == Country.DE)
                                .collect(Collectors.groupingBy(x -> x.getProduct().getProductName(), TreeMap::new,
                                        Collectors.summingInt(ProductSalesBatch::getQuantitySold)))
                                .forEach((x, y) -> System.out.println(String.format("%,d",y) + " " + x + "(s) sold"));

                    }
                    case "6" -> { //"Sales revenue from Spain [filter by enum][Map to double][sum]"
                        output.accept("Total sales revenue for Spain €" + String.format("%,.2f",
                                lastestData.filter(x -> x.getCountry() == Country.ES)
                                        .mapToDouble(ProductSalesBatch::getRevenue)
                                        .sum())
                        );
                    }
                    case "7" -> { //"First iPhone sales result [filter by String][find first]"
                        output.accept(
                            lastestData.filter(x -> x.getProduct().getProductName().contains("iPhone"))
                                .findFirst().toString()
                        );
                    }
                    case "8" -> { //"First 10 products in sales batch [limit][for each]"
                        lastestData.limit(10).forEach(System.out::println);
                    }
                    case "9" -> { //"Sorted revenue results by country [collect][group by enum][Tree map][sum double][sort by comparing lambda][foreach]"
                        lastestData.collect(Collectors
                                .groupingBy(ProductSalesBatch::getCountry
                                        , TreeMap::new
                                        , Collectors.summingDouble(ProductSalesBatch::getRevenue)))
                                .entrySet().stream()
                                .sorted(Comparator.comparing(x -> x.getKey().name()))
                                .forEach(m -> System.out.println(m.getKey().toString() + ":"+ String.format(" €%,.2f", m.getValue())));
                    }
                    case "10" -> { //"Top 5 countries by revenue [collect][Group by enum][Tree map][sum Double][sort][limit][foreach]"
                        output.accept("Top 5 countries by revenue:");
                                lastestData.collect(Collectors.groupingBy(ProductSalesBatch::getCountry
                                                , TreeMap::new
                                                , Collectors.summingDouble(ProductSalesBatch::getRevenue)))
                                        .entrySet()
                                        .stream()
                                        .sorted(Map.Entry.<Country, Double>comparingByValue().reversed())
                                        .limit(5)
                                        .forEach(x ->
                                                System.out.println( x.getKey().toString() + ": €" +String.format("%,.2f", x.getValue())));

                    }
                    case "11" -> { //Create Pre-order/discontinued product Partition Map [collect][partition by string contains][get TRUE][GroupBy][foreach]
                        lastestData.collect(Collectors.partitioningBy(
                                        x -> x.getProduct().getProductName().toLowerCase().contains("pre-order")
                                                || x.getProduct().getProductName().toLowerCase().contains("discontinued")))
                                .get(Boolean.TRUE)
                                .stream().collect(Collectors.groupingBy(x->x.getProduct().getProductName()
                                        , TreeMap::new, Collectors.summingDouble(x->x.getRevenue())))
                                .entrySet().stream().forEach(x->output.accept(x.getKey() +" made " + String.format("€%,.2f", x.getValue()) + " in revenue." ));;
                    }
                    case "12" -> {  //"Find any product with > €30k revenue [Map to Records][Group by product][Tree map][Sum Double][Filter][find any]"
                        var res = lastestData.map(entry -> new ProductSalesRecord(entry.getProduct().getProductName(), entry.getRevenue()))
                                .toList();

                                res.stream().collect(Collectors.groupingBy(x->x.productName()
                                        , TreeMap::new
                                        , Collectors.summingDouble(x->x.revenue())))
                                        .entrySet()
                                        .stream()
                                        .filter(x -> x.getValue() > 30_000)
                                        .findAny().ifPresent(x-> System.out.println(x.getKey().toString() + " has a total revenue of "
                                                + String.format("€%,.2f",x.getValue())));
                    }
                    case "13" -> { //Show pre-order products by release date [Collect][Partition][Map to Product-LocalDateTime][Sort by date][Distinct][Output time duration]
                        lastestData.collect(Collectors.partitioningBy(
                                        x -> x.getProduct().getProductName().toLowerCase().contains("pre-order")))
                                .get(Boolean.TRUE)
                                .stream()
                                .collect(Collectors.toMap(x -> x.getProduct()
                                        , y-> ((PreOrderProduct) y.getProduct()).getReleaseDate()
                                        , (first, duplicate) -> first ))
                                .entrySet().stream()
                                .sorted(Comparator.comparing(x-> x.getValue()))
                                .distinct()
                                .forEach(x-> System.out.println(x.getKey().getProductName() + " releases on "
                                        + x.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        + " at " + x.getValue().format(DateTimeFormatter.ofPattern("HH:mm"))
                                        + " (in " + Duration.between(LocalDateTime.now(), x.getValue()).toDays() + " days)."
                                        + " China release date is " + x.getValue().withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
                                            .toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

                    }
                    case "14" -> { //Show discontinued products by discontinued date [Collect][Partition By][sorted Comparator][distinct][forEach]
                        lastestData.collect(Collectors.partitioningBy( //partitioningBy uses a predicate
                                        x -> x.getProduct().getProductName().toLowerCase().contains("discontinued")))
                                .get(Boolean.TRUE)
                                .stream()
                                .collect(Collectors.toMap(x -> x.getProduct()
                                        , y-> ((DiscontinuedProduct) y.getProduct()).getDiscontinuedDate()
                                        , (first, duplicate) -> first ))
                                .entrySet().stream()
                                .sorted(Comparator.comparing(x-> x.getValue()))
                                .distinct()
                                .forEach(x-> System.out.println(x.getKey().getProductName() + " stopped production on "
                                        + x.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        + " (" + Duration.between(x.getValue(), LocalDateTime.now()).toDays() + " days ago)."));
                    }
                    case "15" -> { //Create sales record by country and output France's revenue [collect][group by][map]
                        var recordsList = lastestData.collect(Collectors.groupingBy(x -> x.getCountry(), TreeMap::new, Collectors.summingDouble(ProductSalesBatch::getRevenue)))
                                .entrySet().stream()
                                .map(x -> new CountrySalesRecord(x.getKey(), x.getValue()))
                                .toList();

                        for(CountrySalesRecord record : recordsList) {
                            if(record.country() == Country.FR) {
                                output.accept(record.toString());
                            }
                        }
                    }
                    case "16" -> { //Get count of countries with sales data [collect][GroupingBy][count]
                        output.accept("There are " + lastestData.collect(Collectors.groupingBy(x -> x.getCountry()))
                                .entrySet().stream().count() + " countries with sales data");
                    }
                    case "17" -> { //Get distinct product objects. Output Pre-order and discontinued products [map][distinct][sorted comparator][Switch pattern matching]
                        var res = lastestData.map(x -> x.getProduct())
                                .distinct()
                                .sorted(Comparator.comparing(x->x.getProductName()))
                                .toList();
                        for(Products.Product r : res){ //Product class is extended by DiscontinuedProduct and PreOrderProduct
                            switch(r){
                                case Products.DiscontinuedProduct d ->{
                                    output.accept("Discontinued Product: " + d.getProductName() + " discontinued on "
                                            + d.getDiscontinuedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                }
                                case Products.PreOrderProduct p ->{
                                    output.accept( "Pre-Order Product: " + p.getProductName() + " releases on "
                                            + p.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                }
                                case null -> { /*Do nothing. Same as the default case but including the "null" case for demonstration*/ }
                                default -> { /*Do nothing*/ }
                            }
                        }
                    }
                    case "18" -> { //"All match pre-order product [map][distinct][all match]"
                        var res = lastestData.map(x -> x.getProduct()).distinct().toList().stream();
                        if (res.allMatch(x-> x instanceof Products.PreOrderProduct)){
                            output.accept("All products are pre-order products");
                        }
                        else{
                            output.accept("Not all products are pre-order products");
                        }
                    }
                    case "19" -> { //"Any match discontinued product [map][distinct][any match]"
                        var res = lastestData.map(x -> x.getProduct()).distinct().toList().stream();
                        if (res.anyMatch(x-> x instanceof Products.DiscontinuedProduct && x.getPrice() > 150)){
                            output.accept("Some products are discontinued products with a price > €150");
                        }
                        else{
                            output.accept("No products are discontinued products with a price > €150");
                        }
                    }
                    case "20" -> { //"None match product name [map][distinct][none match]"
                        var res = lastestData.map(x -> x.getProduct()).distinct().toList().stream();
                        if (res.noneMatch(x-> x.getPrice() < 100)){
                            output.accept("No products are < €100" );
                        }
                        else{
                            output.accept("Some products are < €100" );
                        }
                    }
                    case "21" -> { //List all distinct products and their prices [map][distinct][sort][comparator][foreach]
                        lastestData.map(x -> x.getProduct()).distinct().toList().stream()
                                .sorted(Comparator.comparing(x->x.getProductName()))
                                .forEach(x->output.accept(x.getProductName() + " costs " + String.format("€%,.2f", x.getPrice())));
                    }
                    case "22" -> { //Output unsorted Country and Total Revenue map [Collect][toMap]
                        var map = lastestData.collect(Collectors.toMap(productSalesBatch -> productSalesBatch.getCountry()
                                , productSalesBatch -> productSalesBatch.getRevenue(), (a, b) -> a + b));
                        map.forEach((x,y)->output.accept(x + ": " + String.format("€%,.2f", y)));
                    }
                    case "" ->{
                        if(!firstRun){
                            listOptions();
                            output.accept("Empty input was received. Please try again");
                        }
                        firstRun = false;
                    }
                    default -> {
                        listOptions();
                        output.accept("Input '" + lastInput+ "' not recognised. Please try again");
                    }
                }
                lastInput = getNextInput("Please type your response");
            }
            catch (Exception e){
                //output.accept(e.getMessage()); //Output error message. Uncomment for testing.
                // Leave commented because the stream will be recreated and the appropriate code will run at a later time
                try {
                    output.accept("Waiting for the next batch of data");
                    Thread.sleep(500);
                }
                catch (InterruptedException ex) {

                }
            }
        }
        out.println("\r\nClosing application. Goodbye!");
    }

    private void listOptions(){
        output.accept("Select an option:");
        for (int i = 0; i < options.length; i++) {
            output.accept((i + 1) + ": " + options[i]);
        }
        output.accept("If you want to quit the application at any point, type in 'exit' to quit.");
    }

    private String getNextInput(String message){
        out.println(message + ": ");
        var next = scanner.nextLine().trim();
        checkInputForExitCommand(next);

        message = "This will not affect the original message reference due to call by value";
        return next;
    }

    private int getNextNumberedSelection(String message){
        String next = getNextInput(message);
        Object testObj = null; //Create an Object reference just for the Switch type pattern matching

        //Try catch here to catch any NumberFormatErrors
        try{
            testObj = Integer.parseInt(next); //Would normally just return the result here. The below is for demo purposes
        }
        catch(NumberFormatException e){
            testObj = next;
        }

        //DEMO Switch type pattern matching using either and int or String from above.
        switch(testObj){
            case Integer i -> {return i;}
            case String s -> {return -1;}
            default -> {return -1;}
        }
    }

    //DEMO - LAMBDA EFFECTIVELY FINAL - exitString value cannot be changed
    //final keyword isn't being used here, but compiler will treat exitString like it is final.
    private void checkInputForExitCommand(String text){
        String exitString = "exit";
        Predicate<String> isExit = s -> (s.trim().toLowerCase().equals(exitString));

        //exitString="something else"; //Uncommenting this line will result in a compile error because it would change the check in the Lambda
        if(isExit.test(text)) {isRunning = false;}

        //Lambda use is the equivalent of the below
        //if(text.trim().toLowerCase().equals("exit")){ isRunning = false; }
    }

    //DEMO - Lambda being passed into method
    private boolean getNextYesOrNo(String message, Predicate<String> lambdaCheck){
        var next = getNextInput(message);
        return lambdaCheck.test(next);
    }

    private void Wait(int milliseconds){
        if(milliseconds < 50){ milliseconds = 50; }
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        }catch(InterruptedException e){}
    }

    public void updateData(Stream<ProductSalesBatch> salesBatchSupplier) {
        lastestData = salesBatchSupplier;
    }
}