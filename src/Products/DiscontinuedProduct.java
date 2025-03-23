package Products;

import java.time.LocalDateTime;

public final class DiscontinuedProduct extends Product implements IDiscontinuedProduct {
    private final LocalDateTime discontinuedDate;

    public DiscontinuedProduct(String name, double price, LocalDateTime discontinuedDate) {
        super(name, price);
        this.discontinuedDate = discontinuedDate;
    }

    public LocalDateTime getDiscontinuedDate() {
        return discontinuedDate; //The field is final so this can't be edited
    }

    public String getProductName(){
        return super.getProductName() + " (Discontinued)";
    }
}
