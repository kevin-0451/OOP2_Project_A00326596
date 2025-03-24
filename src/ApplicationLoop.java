import Enums.Country;
import Products.DiscontinuedProduct;
import Products.PreOrderProduct;
import Sales.CountrySalesRecord;
import Sales.ProductSalesBatch;
import Sales.ProductSalesRecord;
import com.sun.source.tree.Tree;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//DEMO - java.lang is imported by default. But we can import specific classes from it to make calling its methods easier
import static java.lang.System.out;

public final class ApplicationLoop {

    private boolean isRunning = true;
    private final Scanner scanner = new Scanner(System.in);

    //DEMO Lambdas using the Predicate functional interface
    private Predicate<String> isYes = s -> (s.trim().toLowerCase().equals("y"));
    private Predicate<String> isNo = s -> (s.trim().toLowerCase().equals("n"));
    private Consumer<String> output = System.out::println;
    private Stream<ProductSalesBatch> lastestData = null;
    private final String[] options = {
            "Lowest products sold [GroupBy String][Tree Map][Sum Int][Min]"
            , "Highest products sold [GroupBy String][Tree Map][Sum Int][Max]"
            , "Lowest revenue by country"
            , "Highest revenue by country"
            , "Sales from Germany"
            , "Sales from Spain"
            , "First iPhone sales result"
            , "First 10 products in sales batch"
            , "Sorted revenue results by country name (alphabetically)"
            , "Top 5 countries by revenue"
            , "[Partition] Sales of discontinued and pre-order products"
            , "Create Country<->Product map"
            , "Show pre-order products by release date"
            , "Show discontinued products by discontinued date"
            , "Create sales record by country"
            , "Test"
            , "Test"
            , "Test"
            , "Test"


    };


    public ApplicationLoop(){
        initialiseVariables();
    }

    private void initialiseVariables(){

    }

    public void run(){
        String lastInput = "";
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
                    case "" -> {
                        output.accept("Welcome!");
                        output.accept("If you want to quit the application at any point, type in 'exit' to quit.");
                        output.accept("Select an option:");
                        listOptions();
                    }
                    case "exit" -> {
                        isRunning = false;
                    }
                    case "1" -> { //"Lowest products sold [GroupBy String][Tree Map][Sum Int][Min]"
                        if (lastestData != null) {
                            //lastestData.filter(x -> x.getCountry() == Country.AT).forEach(System.out::println);

                            var res = lastestData.collect(Collectors.groupingBy(x -> x.getProduct().getProductName()
                                    , TreeMap::new
                                    , Collectors.summingInt(ProductSalesBatch::getQuantitySold)))
                                    .entrySet()
                                    .stream()
                                    .min(Comparator.comparing(x -> x.getValue()));

                            output.accept(res.get().getKey() + " has lowest sales with " + String.format("%,d",res.get().getValue()));
                                    //.Entry<Product, Integer>
                                    //.forEach((x, y) -> System.out.println(x + ": " + y + " sold."));

                            //output.accept(lastestData.min(Comparator.comparing(ProductSalesBatch::getPrice)).toString());

                        }
                    }
                    case "2" -> { //"Highest products sold [GroupBy String][Tree Map][Sum Int][Max]"
                        if (lastestData != null) {
                            //lastestData.filter(x -> x.getCountry() == Country.AT).forEach(System.out::println);
                            var res = lastestData.collect(Collectors.groupingBy(x -> x.getProduct().getProductName()
                                            , TreeMap::new
                                            , Collectors.summingInt(ProductSalesBatch::getQuantitySold)))
                                    .entrySet()
                                    .stream()
                                    .max(Comparator.comparing(x -> x.getValue()));

                            output.accept(res.get().getKey() + " has highest sales with " + String.format("%,d",res.get().getValue()));
                        }
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
                                .collect(Collectors.groupingBy(x -> x.getProduct().getProductName(), TreeMap::new, Collectors.summingInt(ProductSalesBatch::getQuantitySold)))
                                .forEach((x, y) -> System.out.println(String.format("%,d",y) + " " + x + "(s) sold"));

                    }
                    case "6" -> { //"Sales revenue from Spain [filter by enum][Map to double][sum]"
                        output.accept("Total sales revenue €" + String.format("%,.2f",
                                lastestData.filter(x -> x.getCountry() == Country.ES)
                                        .mapToDouble(ProductSalesBatch::getRevenue)
                                        .sum())
                        );

                                //.collect(Collectors.groupingBy(x -> x.getProduct().getProductName(), TreeMap::new, Collectors.summingDouble(ProductSalesBatch::getRevenue)))
                                //.forEach((x, y) -> System.out.println(String.format("€%,.2f",y) + " revenue from " + x));
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
                        if (lastestData != null) {
                            lastestData.collect(Collectors
                                    .groupingBy(ProductSalesBatch::getCountry
                                            , TreeMap::new
                                            , Collectors.summingDouble(ProductSalesBatch::getPrice)))
                                    .entrySet().stream()
                                    .sorted(Comparator.comparing(x -> x.getKey().name()))
                                    .forEach(m -> System.out.println(m.getKey().toString() + ":"+ String.format(" €%,.2f", m.getValue())));
                        }
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
                    case "11" -> { //Create Pre-order/discontinued product Partition Map [collect][partition by string contains][get TRUE][foreach]
                        lastestData.collect(Collectors.partitioningBy(
                                        x -> x.getProduct().getProductName().toLowerCase().contains("pre-order")
                                                || x.getProduct().getProductName().toLowerCase().contains("discontinued")))
                                .get(Boolean.TRUE)
                                .forEach(System.out::println);
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
                                        .findAny().ifPresent(x-> System.out.println(x.getKey().toString() + ": "
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
                                        + " (in " + Duration.between(LocalDateTime.now(), x.getValue()).toDays() + " days)."));

                    }
                    case "14" -> { //Show discontinued products by discontinued date
                        lastestData.collect(Collectors.partitioningBy(
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
                    case "15" -> { //Create sales record by country and output France's revenue
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
                    case "16" -> {

                    }
                    case "17" -> {

                    }

                    default -> {
                        //lastInput = getNextInput("Input '" + lastInput+ "' not recognised. Please try again");
                        output.accept("Input '" + lastInput+ "' not recognised. Please try again");
                    }
                }
                lastInput = getNextInput("Please type your response");
            }
            catch (Exception e){
                output.accept(e.getMessage());
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
        for (int i = 0; i < options.length; i++) {
            output.accept((i + 1) + ": " + options[i]);
        }

    }

    private String getNextInput(String message){
        out.println(message + ": ");
        var next = scanner.nextLine().trim();
        checkInputForExitCommand(next);

        message = "This will not affect the original message reference due to call by value";
        return next;
    }

    //DEMO Switch pattern matching - Check if the input matches a suitable pattern before trying to parse it
    //DEMO Unchecked exceptions - possibility for a NumberFormatError here, so we should use a try catch to parse the text
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