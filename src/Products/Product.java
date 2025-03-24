package Products;

import java.util.Objects;

//Product is a Sealed class that permits DiscontinuedProduct and PreOrderProduct
public sealed class Product implements IProduct permits DiscontinuedProduct, PreOrderProduct {
    private final String name;
    private final double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getProductName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String toString(){
        return name + " €" + price;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Product){
            if(this.name.equals(((Product)obj).name) && this.price == ((Product)obj).price){
                return true;
            };
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(name, price);
    }
}
