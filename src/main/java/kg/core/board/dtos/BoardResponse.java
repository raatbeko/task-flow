package kg.core.board.dtos;

import kg.core.board.model.BoardStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardResponse {

    Long id;

    String name;

    String description;

    int position;

    BoardStatus boardStatus;

    Long projectId;
}
