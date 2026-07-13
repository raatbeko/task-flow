package kg.core.board.service.impl;

import jakarta.persistence.EntityNotFoundException;
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

    protected BoardServiceImpl(BoardRepository boardRepository, BoardMapper boardMapper, ProjectRepository projectRepository) {
        super(boardRepository);
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
        this.projectRepository = projectRepository;
    }


    @Override
    @Transactional
    public Board create(BoardCreateRequest boardCreateRequest) {
        Project project = projectRepository.findById(boardCreateRequest.projectId())
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        int nextPosition = boardRepository.countByProjectId(project.getId());

        Board board = boardMapper.toEntity(boardCreateRequest);
        board.setProject(project);
        board.setStatus(BoardStatus.ACTIVE);
        board.setPosition(nextPosition);

        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public Board update(Long id, BoardUpdateRequest boardUpdateRequest) {
        Board board = find(id);
        boardMapper.update(boardUpdateRequest, board);
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Board board = find(id);
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
        Board originalBoard = find(id);
        int nextPosition = boardRepository.countByProjectId(originalBoard.getProject().getId());

        Board copyBoard =  new Board();
        copyBoard.setProject(originalBoard.getProject());
        copyBoard.setName(originalBoard.getName());
        copyBoard.setDescription(originalBoard.getDescription());
        copyBoard.setPosition(nextPosition);
        copyBoard.setStatus(BoardStatus.ACTIVE);

        return boardRepository.save(copyBoard);
    }
}
