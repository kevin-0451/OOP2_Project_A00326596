package Products;
import java.time.LocalDateTime;

public sealed interface IDiscontinuedProduct extends IProduct permits DiscontinuedProduct {
    LocalDateTime getDiscontinuedDate();
}
