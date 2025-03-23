package Products;

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
}
