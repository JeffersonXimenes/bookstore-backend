package br.com.bookstore.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Represents an object of type, subgenus and genus")
public class BaseLabel {

    @Schema(description = "Object ID", example = "1")
    private Long id;

    @Schema(description = "Value description")
    private String description;

    public BaseLabel(String description) {
        this.description = description;
    }
}
