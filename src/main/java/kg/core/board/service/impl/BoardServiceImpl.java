package kg.core.board.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.core.base.exception.ConflictException;
import kg.core.base.exception.ForbiddenException;
import kg.core.base.service.impl.DefaultCrudService;
import kg.core.board.dtos.BoardPositionRequest;
import kg.core.board.model.Board;
import kg.core.board.model.BoardStatus;
import kg.core.board.repository.BoardRepository;
import kg.core.board.service.BoardService;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.boardColumn.repository.BoardColumnRepository;
import kg.core.boardMember.model.BoardMember;
import kg.core.boardMember.model.BoardRole;
import kg.core.boardMember.repository.BoardMemberRepository;
import kg.core.project.model.Project;
import kg.core.project.model.ProjectStatus;
import kg.core.project.repository.ProjectRepository;
import kg.core.projectMember.model.ProjectMember;
import kg.core.projectMember.model.ProjectRole;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.security.validator.AccessGuard;
import kg.core.task.model.Task;
import kg.core.task.repository.TaskRepository;
import kg.core.user.model.User;
import kg.core.utils.UserProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardServiceImpl extends DefaultCrudService<Board, Long> implements BoardService {

    BoardRepository boardRepository;
    ProjectRepository projectRepository;
    BoardMemberRepository boardMemberRepository;
    UserProvider userProvider;
    ProjectMemberRepository projectMemberRepository;
    AccessGuard accessGuard;
    BoardColumnRepository boardColumnRepository;
    TaskRepository taskRepository;

    protected BoardServiceImpl(BoardRepository boardRepository, ProjectRepository projectRepository,
                               BoardMemberRepository boardMemberRepository, UserProvider userProvider,
                               ProjectMemberRepository projectMemberRepository, AccessGuard accessGuard,
                               BoardColumnRepository boardColumnRepository, TaskRepository taskRepository) {
        super(boardRepository);
        this.boardRepository = boardRepository;
        this.projectRepository = projectRepository;
        this.boardMemberRepository = boardMemberRepository;
        this.userProvider = userProvider;
        this.projectMemberRepository = projectMemberRepository;
        this.accessGuard = accessGuard;
        this.taskRepository = taskRepository;
        this.boardColumnRepository = boardColumnRepository;

    }


    @Override
    @Transactional
    public Board create(Long projectId, Board board) {

        accessGuard.requireProjectRole(projectId, ProjectRole.EDITOR);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            throw new ConflictException("Проект заархивирован, действие недоступно");
        }

        if (board.getStatus() == BoardStatus.ARCHIVED) {
            throw new ConflictException("Доска заархивирована, действие недоступно");
        }


        User currentUser = userProvider.getCurrentUser();

        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId())
                .orElseThrow(() -> new ForbiddenException("У вас нет доступа к этому проекту"));

        int nextPosition = boardRepository.findNextPosition(project.getId());

        board.setProject(project);
        board.setStatus(BoardStatus.ACTIVE);
        board.setPosition(nextPosition);
        Board savedBoard = boardRepository.save(board);

        BoardMember boardMember = new BoardMember();
        boardMember.setBoard(savedBoard);
        boardMember.setProjectMember(projectMember);
        boardMember.setRole(BoardRole.OWNER);
        boardMemberRepository.save(boardMember);

        return savedBoard;

    }

    @Override
    @Transactional
    public void delete(Long id) {
        Board board = find(id);
        Long projectId = board.getProject().getId();
        boardRepository.delete(board);

        List<Board> remaining = boardRepository.findByProjectIdOrderByPositionAsc(projectId);
        for (int i = 0; i < remaining.size(); i++) {
            remaining.get(i).setPosition(i);
        }
        boardRepository.saveAll(remaining);
    }

    @Override
    @Transactional
    public void updatePosition(Long id, BoardPositionRequest request) {
        Board board = find(id);
        Long projectId = board.getProject().getId();

        accessGuard.requireBoardRole(board.getId(), projectId, BoardRole.EDITOR);

        int oldPosition = board.getPosition();
        int newPosition = request.position() != null ? request.position().intValue() : -1;

        if (newPosition == oldPosition) return;

        List<Board> boards = boardRepository.findByProjectIdOrderByPositionAsc(projectId);

        int maxPosition = boards.size() - 1;
        if (newPosition < 0 || newPosition > maxPosition) {
            newPosition = maxPosition;
        }
        if (newPosition == oldPosition) return;

        boards.remove((int) oldPosition);
        boards.add(newPosition, board);

        for (int i = 0; i < boards.size(); i++) {
            boards.get(i).setPosition(i);
        }

        boardRepository.saveAll(boards);
    }

    @Override
    public List<Board> findByProjectId(Long id) {
        return boardRepository.findByProjectIdOrderByPositionAsc(id);
    }

    @Override
    @Transactional
    public void archive(Long id) {
        Board board = find(id);

        Long projectId = board.getProject().getId();
        accessGuard.requireBoardRole(board.getId(), projectId, BoardRole.EDITOR);

        board.setStatus(BoardStatus.ARCHIVED);
        save(board);
    }

    @Override
    @Transactional
    public Board duplicate(Long id) {
        Board originalBoard = find(id);

        Long projectId = originalBoard.getProject().getId();
        accessGuard.requireBoardRole(originalBoard.getId(), projectId, BoardRole.EDITOR);


        int nextPosition = boardRepository.countByProjectId(originalBoard.getProject().getId());

        Board copyBoard = new Board();
        copyBoard.setProject(originalBoard.getProject());
        copyBoard.setName(originalBoard.getName());
        copyBoard.setDescription(originalBoard.getDescription());
        copyBoard.setPosition(nextPosition);
        copyBoard.setStatus(BoardStatus.ACTIVE);
        boardRepository.save(copyBoard);

        List<BoardColumn> originalColumns = boardColumnRepository.findByBoardIdOrderByPositionAsc(originalBoard.getId());

        for (BoardColumn originalColumn : originalColumns) {
            BoardColumn copyColumn = new BoardColumn();
            copyColumn.setBoard(copyBoard);
            copyColumn.setName(originalColumn.getName());
            copyColumn.setPosition(originalColumn.getPosition());
            boardColumnRepository.save(copyColumn);

            List<Task> originalTasks = taskRepository.findByBoardColumnIdOrderByPositionAsc(originalColumn.getId());

            for (Task originalTask : originalTasks) {
                Task copyTask = new Task();
                copyTask.setBoardColumn(copyColumn);
                copyTask.setTitle(originalTask.getTitle());
                copyTask.setDescription(originalTask.getDescription());
                copyTask.setPriority(originalTask.getPriority());
                copyTask.setDueDate(originalTask.getDueDate());
                copyTask.setPosition(originalTask.getPosition());
                copyTask.setAssignees(new HashSet<>(originalTask.getAssignees()));
                copyTask.setTags(new HashSet<>(originalTask.getTags()));
                taskRepository.save(copyTask);
            }


        }
        return copyBoard;
    }

}
