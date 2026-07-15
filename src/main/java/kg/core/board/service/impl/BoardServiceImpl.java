package kg.core.board.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.core.base.service.impl.DefaultCrudService;
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

    protected BoardServiceImpl(BoardRepository boardRepository, ProjectRepository projectRepository) {
        super(boardRepository);
        this.boardRepository = boardRepository;
        this.projectRepository = projectRepository;
    }


    @Override
    @Transactional
    public Board create(Long projectId, Board board) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        int nextPosition = boardRepository.countByProjectId(project.getId());

        board.setProject(project);
        board.setStatus(BoardStatus.ACTIVE);
        board.setPosition(nextPosition);

        return save(board);
    }

    @Override
    @Transactional
    public void delete(Long id){
        Board board = find(id);
        boardRepository.delete(board);
    }
    @Override
    @Transactional
    public void archive(Long id) {
        Board board = find(id);
        board.setStatus(BoardStatus.ARCHIVED);
        save(board);
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
