package Sales;

import Enums.Country;
import Products.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductSalesBatch {
    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Product productObj;
    private int quantitySold;
    private LocalDateTime timestamp;
    private Country country;

    public ProductSalesBatch(Product product, int quantitySold, LocalDateTime timestamp, Country country) {
        this.productObj = product;
        this.quantitySold = quantitySold;
        this.timestamp = timestamp;
        this.country = country;
    }

    public Product getProduct() { return productObj; }
    public double getPrice() { return productObj.getPrice(); }
    public int getQuantitySold() { return quantitySold; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Country getCountry() { return country; }
    public double getRevenue(){ return getPrice() * getQuantitySold(); }

    public String toString() {
        return "ProductSale{" +
                "product: " + productObj.getProductName() +
                ", price: €" + getPrice() +
                ", quantity sold: " + quantitySold +
                ", total revenue: €" + getRevenue() +
                ", timestamp: " + timestamp.format(dtFormatter) +
                ", country: " + country +
                '}';
    }
}
