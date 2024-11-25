package com.project.RecipeSpark;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.QuestionRepository;
import com.project.RecipeSpark.service.QuestionService;
import com.project.RecipeSpark.DataNotFoundException;

public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetList() {
        // Given
        String keyword = "test";
        int page = 0;

        // When
        when(questionRepository.findAllByKeyword(eq(keyword), any())).thenReturn(Page.empty());

        // Then
        assertThat(questionService.getList(page, keyword)).isNotNull();
        verify(questionRepository, times(1)).findAllByKeyword(eq(keyword), any());
    }

    @Test
    public void testGetQuestion() {
        // Given
        Integer questionId = 1;
        Question question = new Question();
        question.setTitle("Sample Question");

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // When
        Question foundQuestion = questionService.getQuestion(questionId);

        // Then
        assertThat(foundQuestion.getTitle()).isEqualTo("Sample Question");
        verify(questionRepository, times(1)).findById(questionId);
    }

    @Test
    public void testGetQuestionNotFound() {
        // Given
        Integer questionId = 1;
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DataNotFoundException.class, () -> {
            questionService.getQuestion(questionId);
        });
    }

    @Test
    public void testCreateQuestion() {
        // Given
        String title = "New Question";
        String content = "This is a new question content.";

        // When
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        questionService.createQuestion(title, content, mockUser);

        // Then
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    public void testModifyQuestion() {
        // Given
        Question question = new Question();
        question.setTitle("Old Title");
        question.setContent("Old Content");

        String newTitle = "Updated Title";
        String newContent = "Updated Content";

        // When
        questionService.modifyQuestion(question, newTitle, newContent);

        // Then
        assertThat(question.getTitle()).isEqualTo(newTitle);
        assertThat(question.getContent()).isEqualTo(newContent);
        assertThat(question.getModifyDate()).isNotNull();
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    public void testDeleteQuestion() {
        // Given
        Question question = new Question();
        question.setTitle("Question to delete");

        // When
        doNothing().when(questionRepository).delete(question);
        questionService.deleteQuestion(question);

        // Then
        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    public void testFindById() {
        // Given
        Long questionId = 1L;
        Question question = new Question();
        question.setTitle("Sample Question");

        when(questionRepository.findByQuestionId(questionId)).thenReturn(Optional.of(question));

        // When
        Optional<Question> foundQuestion = questionService.findById(questionId);

        // Then
        assertTrue(foundQuestion.isPresent());
        assertThat(foundQuestion.get().getTitle()).isEqualTo("Sample Question");
    }
}
