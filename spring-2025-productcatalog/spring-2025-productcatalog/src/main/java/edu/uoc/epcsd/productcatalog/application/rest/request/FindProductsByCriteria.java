package edu.uoc.epcsd.productcatalog.application.rest.request;

import edu.uoc.epcsd.productcatalog.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class FindProductsByCriteria {

    private final String name;

    private final Long categoryId;

    // Constructor que accepta un objecte Product i inicialitza els camps
    public Product toProduct() {
        return Product.builder()
                .name(this.name)
                .categoryId(this.categoryId)
                .build();
    }

}
