package kg.core.board.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.core.auth.service.impl.DefaultAccountContextProvider;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.dtos.BoardCreateRequest;
import kg.core.board.dtos.BoardUpdateRequest;
import kg.core.board.mapper.BoardMapper;
import kg.core.board.model.Board;
import kg.core.board.model.BoardStatus;
import kg.core.board.repository.BoardRepository;
import kg.core.board.service.BoardService;
import kg.core.project.model.Project;
import kg.core.project.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardServiceImpl extends DefaultCrudService<Board, Long> implements BoardService {

    BoardRepository boardRepository;
    ProjectRepository projectRepository;
    BoardMapper boardMapper;

    public BoardServiceImpl(BoardRepository boardRepository, BoardMapper boardMapper, ProjectRepository projectRepository) {
        super(boardRepository);
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
        this.projectRepository = projectRepository;
    }


    @Override
    @Transactional
    public Board createBoard(BoardCreateRequest boardCreateRequest) {
        Project project = projectRepository.findById(boardCreateRequest.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        Board board = boardMapper.toEntity(boardCreateRequest);
        board.setProject(project);
        board.setStatus(BoardStatus.ACTIVE);
        //TODO POSITION LOGIC AFTER PROJECT IMPL ASKAT

        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public Board updateBoard(Long id, BoardUpdateRequest boardUpdateRequest) {
        Board board = find(id);
        boardMapper.update(boardUpdateRequest, board);
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Board board = get(id);
        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public void archive(Long id) {
        Board board = get(id);
        board.setStatus(BoardStatus.ARCHIVED);
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public Board duplicate(Long id) {
        Board originalBoard = get(id);

        //TODO POSITION LOGIC AFTER PROJECT IMPL ASKAT
        Board copyBoard = new Board();
        copyBoard.setProject(originalBoard.getProject());
        copyBoard.setStatus(originalBoard.getStatus());
        copyBoard.setPosition(originalBoard.getPosition());
        copyBoard.setName(originalBoard.getName() + " (копия)");
        copyBoard.setDescription(originalBoard.getDescription());

        return boardRepository.save(copyBoard);
    }
}
