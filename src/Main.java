import Sales.ProductSalesBatch;
import Sales.SalesDataGenerator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        SalesDataGenerator generator = new SalesDataGenerator();

        ApplicationLoop applicationLoop = new ApplicationLoop();

        executor.scheduleAtFixedRate(() -> {
            Supplier<Stream<ProductSalesBatch>> salesBatchSupplier = () -> generator.generateSales().stream();

            applicationLoop.updateData(salesBatchSupplier.get());
            /*System.out.println("\nAnalysis:");
            SalesAnalysis.findMaxPriceSale(salesBatchSupplier.get()).ifPresent(sale -> System.out.println("Max price sale: " + sale));
            SalesAnalysis.findMinPriceSale(salesBatchSupplier.get()).ifPresent(sale -> System.out.println("Min price sale: " + sale));
            System.out.println("Sorted by price descending:");
            SalesAnalysis.sortByPriceDescending(salesBatchSupplier.get()).forEach(System.out::println);

            System.out.println("Sales in GB:");
            SalesAnalysis.filterByCountry(salesBatchSupplier.get(), Country.GB).forEach(System.out::println);
            System.out.println("Any high-value sale above $400? " + SalesAnalysis.anyHighValueSale(salesBatchSupplier.get(), 400));

            System.out.println("--------------------------------------\n");
            */
        }, 0, 250, TimeUnit.MILLISECONDS);

        applicationLoop.run();

    }
}
