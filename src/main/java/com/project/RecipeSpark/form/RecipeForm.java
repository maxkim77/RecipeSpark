package com.project.RecipeSpark.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeForm {

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    @Size(max = 255, message = "제목은 최대 255자까지 입력할 수 있습니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String content;

    private MultipartFile image;
}
