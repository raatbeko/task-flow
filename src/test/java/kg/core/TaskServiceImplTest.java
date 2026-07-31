package kg.core;

import kg.core.board.model.Board;
import kg.core.boardColumn.model.BoardColumn;
import kg.core.project.model.Project;
import kg.core.projectMember.repository.ProjectMemberRepository;
import kg.core.tag.model.Tag;
import kg.core.tag.repository.TagRepository;
import kg.core.task.dtos.UpdateDto;
import kg.core.task.model.Task;
import kg.core.task.repository.TaskRepository;
import kg.core.task.service.impl.TaskServiceImpl;
import kg.core.user.model.User;
import kg.core.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {


    @Mock
    TaskRepository repository;

    @Mock
    TagRepository tagRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    TaskServiceImpl service;



    @Test
    void findAllByBoardColumnId_shouldReturnTasksOrderedByPosition() {
        Long boardColumnId = 1L;
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> expectedTasks = List.of(task1, task2);

        when(repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId))
           .thenReturn(expectedTasks);

        List<Task> result = service.findAllByBoardColumnId(boardColumnId);

        assertThat(result).isEqualTo(expectedTasks);
        assertThat(result).hasSize(2);
        verify(repository).findByBoardColumnIdOrderByPositionAsc(boardColumnId);
    }


    @Test
    void updatePurposeTags() {

        Long taskId = 1L;
        Long projectId = 99L;
        Long tagId = 10L;

        Project project = new Project();
        project.setId(projectId);

        Board board = new Board();
        board.setProject(project);

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setBoard(board);

        Task task = new Task();
        task.setBoardColumn(boardColumn);
        task.setTags(new HashSet<>());

        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setProject(project);

        UpdateDto request = new UpdateDto(null, new Long[]{tagId}, null);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        service.updatePurposeTags(taskId, request);

        assertThat(task.getTags()).contains(tag);
        verify(repository).save(task);
    }


    @Test
    void updatePurposeUsers_shouldAddUsersAndSaveTask() {

        Long taskId = 1L;
        Long projectId = 99L;
        Long userId = 20L;

        Project project = new Project();
        project.setId(projectId);

        Board board = new Board();
        board.setProject(project);

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setBoard(board);

        Task task = new Task();
        task.setBoardColumn(boardColumn);
        task.setAssignees(new HashSet<>());

        User user = new User();
        user.setId(userId);

        UpdateDto request = new UpdateDto(null, null, new Long[]{userId});

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(projectMemberRepository.existsByProjectIdAndUserId(projectId, userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        service.updatePurposeUsers(taskId, request);

        assertThat(task.getAssignees()).contains(user);
        verify(repository).save(task);
    }


    @Test
    void replacePurposeTags() {

        Long taskId = 1L;
        Long projectId = 99L;
        Long oldTagId = 5L;
        Long newTagId = 10L;

        Project project = new Project();
        project.setId(projectId);

        Board board = new Board();
        board.setProject(project);

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setBoard(board);

        Tag oldTag = new Tag();
        oldTag.setId(oldTagId);
        oldTag.setProject(project);

        Task task = new Task();
        task.setBoardColumn(boardColumn);
        task.setTags(new HashSet<>(Set.of(oldTag)));

        Tag newTag = new Tag();
        newTag.setId(newTagId);
        newTag.setProject(project);

        UpdateDto request = new UpdateDto(null, new Long[]{newTagId}, null);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(tagRepository.findById(newTagId)).thenReturn(Optional.of(newTag));

        service.replacePurposeTags(taskId, request);

        assertThat(task.getTags()).containsExactly(newTag).doesNotContain(oldTag);
    }




    @Test
    void save_newTask() {

        Long boardColumnId = 1L;
        int existingCount = 3;

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setId(boardColumnId);

        Task task = new Task();
        task.setBoardColumn(boardColumn);

        when(repository.countByBoardColumnId(boardColumnId)).thenReturn(existingCount);
        when(repository.save(task)).thenReturn(task);

        service.save(task);

        assertThat(task.getPosition()).isEqualTo(existingCount);
        verify(repository).save(task);
    }

    @Test
    void save_existingTask() {

        Task task = new Task();
        task.setId(5L);
        task.setPosition(2);

        when(repository.save(task)).thenReturn(task);

        service.save(task);

        assertThat(task.getPosition()).isEqualTo(2);
        verify(repository, never()).countByBoardColumnId(any());
        verify(repository).save(task);
    }

    @Test
    void delete() {

        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        service.delete(taskId);
        verify(repository).delete(task);
    }



    @Test
    void updatePosition_movesTaskToNewPosition() {

        Long taskId = 1L;
        Long boardColumnId = 10L;

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setId(boardColumnId);

        Task task = new Task();
        task.setId(taskId);
        task.setBoardColumn(boardColumn);
        task.setPosition(0);

        Task task2 = new Task();
        task2.setPosition(1);

        Task task3 = new Task();
        task3.setPosition(2);

        List<Task> columnTasks = new ArrayList<>(List.of(task, task2, task3));

        UpdateDto request = new UpdateDto(2L, null, null);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId)).thenReturn(columnTasks);

        service.updatePosition(taskId, request);

        assertThat(task.getPosition()).isEqualTo(2);
        verify(repository).saveAll(anyList());
    }

    @Test
    void updatePosition_samePosition_doesNothing() {

        Long taskId = 1L;
        Long boardColumnId = 10L;

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setId(boardColumnId);

        Task task = new Task();
        task.setId(taskId);
        task.setBoardColumn(boardColumn);
        task.setPosition(1);

        UpdateDto request = new UpdateDto(1L, null, null);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        service.updatePosition(taskId, request);

        verify(repository, never()).findByBoardColumnIdOrderByPositionAsc(any());
        verify(repository, never()).saveAll(anyList());
    }

    @Test
    void updatePosition_outOfRange_clampsToMax() {

        Long taskId = 1L;
        Long boardColumnId = 10L;

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setId(boardColumnId);

        Task task = new Task();
        task.setId(taskId);
        task.setBoardColumn(boardColumn);
        task.setPosition(0);

        Task task2 = new Task();
        task2.setPosition(1);

        List<Task> columnTasks = new ArrayList<>(List.of(task, task2));

        UpdateDto request = new UpdateDto(99L, null, null);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));
        when(repository.findByBoardColumnIdOrderByPositionAsc(boardColumnId)).thenReturn(columnTasks);

        service.updatePosition(taskId, request);

        assertThat(task.getPosition()).isEqualTo(1);
        verify(repository).saveAll(anyList());
    }



}

