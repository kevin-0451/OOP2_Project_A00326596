package Products;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public final class PreOrderProduct extends Product {
    private ZonedDateTime releaseDate;

    public PreOrderProduct(String name, double price, ZonedDateTime releaseDate) {
        super(name, price);
        this.releaseDate = releaseDate;
    }

    public ZonedDateTime getReleaseDate() {
        return releaseDate.minusDays(0); //Perform a pointless minus operation to always return a copy
        //This is to contrast with the discontinued product which has the date set as final
        //It's possible for the release date of a product to change if it hasn't been released.
        //But the discontinued date should be unchangeable.
    }

    public String getProductName(){
        return super.getProductName() + " (Pre-order)";
    }
}
