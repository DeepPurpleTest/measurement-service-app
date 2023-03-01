package ua.ms.entity.dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;
import static ua.ms.util.ApplicationConstants.Validation;

@Builder
@Jacksonized
@Getter
@Setter
@Tag(name = "Factory DTO")
public class FactoryDto {
    private Long id;
    @NotEmpty
    @Length(max = Validation.MAX_FACTORY_NAME_LENGTH,
            message = Validation.FACTORY_NAME_MSG)
    private String name;
}