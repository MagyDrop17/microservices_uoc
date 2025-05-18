package edu.uoc.epcsd.productcatalog.application.rest.request;

import edu.uoc.epcsd.productcatalog.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class FindCategoriesByCriteria {

    private final String name;

    private final String description;

    private final Long parentId;

    // Constructor que accepta un objecte Category i inicialitza els camps
    public Category toCategory() {
        return Category.builder()
                .name(this.name)
                .description(this.description)
                .parentId(this.parentId)
                .build();
    }

}
