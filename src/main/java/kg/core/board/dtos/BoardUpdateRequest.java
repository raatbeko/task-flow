package kg.core.board.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardUpdateRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private int position;
}
