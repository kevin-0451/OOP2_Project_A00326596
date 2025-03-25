package Sales;

import Enums.Country;
import Products.DiscontinuedProduct;
import Products.PreOrderProduct;
import Products.Product;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.time.LocalDateTime;


public class SalesDataGenerator {
    private final Random random = new Random(1); //Create an instance of random to generate numbers
    private ArrayList<Product> products = null; //A collection of products that'll be randomly selected to populate the sales data
    private InnerProductsRepository innerProductsRepository;
    private final List<ProductSalesBatch> salesData;
    private boolean testingMode = true;

    public SalesDataGenerator(){
        innerProductsRepository = new InnerProductsRepository();
        products = innerProductsRepository.getProducts();
        salesData = generateFinalSales(); //Create a final/read-only version of the sales data a single time, so that we always work from the same data
    }

    private List<ProductSalesBatch> generateFinalSales(){
        if (products == null || products.size() == 0) {
            products = innerProductsRepository.getProducts();
        }

        int batchSize = 25 + random.nextInt(125);

        List<ProductSalesBatch> sales = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            var tempProduct = products.get(random.nextInt(products.size()));
            sales.add(new ProductSalesBatch(
                    tempProduct,
                    tempProduct instanceof DiscontinuedProduct ? 1 + random.nextInt(3) : 1 + random.nextInt(100),
                    //ternary operator to alter the quantity of sales if the product is discontinued

                    LocalDateTime.now().minusMinutes(5).minusSeconds(30 + random.nextInt(10)), //Sales should have happened some time in the past. Telemetry data isn't instantaneous
                    //Also an excuse to use some time.minus methods

                    Country.getRandom() //Get a random Country enum
            ));
        }
        return sales;
    }

    public List<ProductSalesBatch> generateSales() {
        if(testingMode) {
            return salesData;
        }
        else {
            return generateFinalSales();
        }
    }

    private class InnerProductsRepository{
        private ArrayList<Product> products = new ArrayList<Product>();

        private ArrayList<Product> getProducts(){
            return products;
        }

        private InnerProductsRepository() {
            products.add(new Product("Apple iPhone 16 Pro", 1116.95));
            products.add(new Product("Apple iPhone 16e", 680.95));
            products.add(new Product("Apple iPhone 16", 862.95));
            products.add(new Product("Samsung Galaxy S24", 608.99));
            products.add(new Product("Apple iPhone 15", 761.95));
            products.add(new Product("Samsung Galaxy A56", 486.99));
            products.add(new Product("Samsung Galaxy A16", 179.99));
            products.add(new Product("Samsung Galaxy A35", 279.99));
            products.add(new Product("Samsung Galaxy A54", 299.99));
            products.add(new Product("Motorola Edge 50 Pro", 404.99));
            products.add(new Product("Sony Xperia 10 VI", 329.99));
            products.add(new Product("Samsung Galaxy A36", 384.99));
            products.add(new Product("Motorola Edge 50 Neo", 339.99));
            products.add(new DiscontinuedProduct("Apple iPhone SE 2020", 159.50, LocalDateTime.of(2021, 12, 1, 12,0) ));
            products.add(new DiscontinuedProduct("Apple iPhone SE 2019", 119.50, LocalDateTime.of(2020, 12, 1, 12,0) ));
            products.add(new PreOrderProduct("Apple iPhone 17 Pro", 1499.99, LocalDateTime.of(LocalDate.now().plusYears(1).getYear(), 11, 1, 12,0).atZone(ZoneId.of("America/New_York"))));
            products.add(new PreOrderProduct("Apple iPhone 17", 1199.99, LocalDateTime.of(LocalDate.now().plusYears(1).getYear(), 11, 7, 12,0).atZone(ZoneId.of("America/New_York"))));
            products.add(new PreOrderProduct("Apple iPhone 17s", 899.99, LocalDateTime.of(LocalDate.now().plusYears(1).getYear(), 11, 30, 12,0).atZone(ZoneId.of("America/New_York"))));

        }
    }
}
