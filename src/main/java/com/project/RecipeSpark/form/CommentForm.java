package com.project.RecipeSpark.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

    @NotBlank(message = "Comment content cannot be blank")
    private String content;
}
