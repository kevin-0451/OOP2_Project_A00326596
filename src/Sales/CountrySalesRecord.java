package Sales;

import Enums.Country;

public record CountrySalesRecord(Country country, double revenue) {
    public String toString() {
        return this.country.toString() + " revenue is " + String.format("€%,.2f", this.revenue);
    }
}
