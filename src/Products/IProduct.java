package Products;

public sealed interface IProduct permits IDiscontinuedProduct, Product {
    String getProductName();
    double getPrice();

}
