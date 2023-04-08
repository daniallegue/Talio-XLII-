package server.api.Task;

import commons.Card;
import commons.CardList;
import commons.Result;
import commons.Task;
import commons.utils.HardcodedIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import server.api.Card.CardService;
import server.api.Tag.TagService;
import server.database.CardRepository;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    CardRepository cardRepository;

    @InjectMocks
    TaskController taskController;


    TaskService taskService;
    CardService cardService;

    TagService tagService;


    Task task1;
    Task task2;
    Card card1;
    CardList cardList1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository);
        cardService = new CardService(cardRepository, taskService, tagService);
        taskController = new TaskController(taskService, cardService);

        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        task1 = new Task(idGenerator1.generateID(), "Test Task", false);
        HardcodedIDGenerator idGenerator2 = new HardcodedIDGenerator();
        idGenerator2.setHardcodedID("2");
        task2 = new Task(idGenerator2.generateID(), "Test Task", true);

        cardList1 = new CardList("Test Card List", new ArrayList<>());
        card1 = new Card(cardList1, "Test Card", "pikachu is cute",
                new ArrayList<>(List.of(task1, task2)), new ArrayList<>());
    }
    @Test
    void getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task1);

        doReturn(allTasks).when(taskRepository).findAll();

        Result<List<Task>> result = taskController.getAllTasks();
        assertEquals(Result.SUCCESS.of(allTasks), result);
    }

    @Test
    void getTaskById() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");

        doReturn(Optional.of(task1)).when(taskRepository).findById(idGenerator.generateID());

        Result<Task> result = taskController.getTaskById(idGenerator.generateID());
        assertEquals(Result.SUCCESS.of(task1), result);
    }

    @Test
    void addNewTask() {
        doReturn(task1).when(taskRepository).save(task1);

        Result<Task> result = taskController.addNewTask(task1);
        assertEquals(Result.SUCCESS.of(task1), result);
    }

    @Test
    void deleteTask() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");

        doReturn(Optional.of(task1)).when(taskRepository).findById(idGenerator.generateID());

        taskController.deleteTask(idGenerator.generateID());
        assertEquals(Result.SUCCESS.of(task1), taskService.getTaskById(idGenerator.generateID()));
    }

    @Test
    void updateTask() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");

        doReturn(task2).when(taskRepository).save(task2);

        Result<Task> result = taskController.updateTaskTitle(task2, idGenerator.generateID());
        assertEquals(Result.SUCCESS.of(task2), result);
        assertEquals(result.value.taskTitle, task2.taskTitle);
    }

    @Test
    void checkOrUncheckTask() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("1");

        doReturn(Optional.of(task1)).when(taskRepository).findById(idGenerator.generateID());
        doReturn(task1).when(taskRepository).save(task1);

        Result<Task> result = taskController.checkOrUncheckTask(idGenerator.generateID());
        assertEquals(Result.SUCCESS.of(task1), result);
        assertTrue(result.value.isCompleted);

    }
}