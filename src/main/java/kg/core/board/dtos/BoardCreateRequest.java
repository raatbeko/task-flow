package kg.core.board.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardCreateRequest {

    @NotNull
    Long projectId;

    @NotBlank
    String name;

    String description;
}
