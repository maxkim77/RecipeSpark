package com.project.RecipeSpark.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.RecipeSpark.domain.AIReview;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.service.AIReviewService;

import io.github.cdimascio.dotenv.Dotenv;

@Controller
public class AIReviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AIReviewController.class);
    private final ChatClient chatClient;
    private String apiKey;
    private final AIReviewService aiReviewService;

    @Autowired
    public AIReviewController(ChatClient chatClient, AIReviewService aiReviewService) {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("OPENAI_API_KEY");
        this.chatClient = chatClient;
        this.aiReviewService = aiReviewService;
    }

    @PostMapping("/submit-recipe")
    public String submitRecipe(
        @RequestParam("recipeInput") String recipeInput,
        @RequestParam(value = "language", defaultValue = "en") String language, // language parameter
        Model model,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = aiReviewService.getUserByUsername(userDetails.getUsername());

        String aiClientResponse;
        try {
            // Determine prompt based on language
            String prompt = language.equalsIgnoreCase("ko")
                ? """
                  사용자가 입력한 레시피에 대한 상세한 피드백을 생성하세요.
                  레시피는 다음과 같습니다:
                  """ + recipeInput + """
                  
                  1. 레시피의 개선점에 대해 설명하고, 어떤 부분을 추가하거나 변경하면 더 맛있어질지 제안하세요.
                  2. 각 재료의 역할과 끓이는 시간 등 조리 과정에서 중요한 팁도 포함해주세요.
                  3. 음식의 풍미와 맛을 높일 수 있는 추가 재료나 방법이 있다면 제안해주세요.
                  """
                : """
                  Provide detailed feedback on the recipe provided by the user.
                  Here is the recipe:
                  """ + recipeInput + """
                  
                  1. Explain improvements to the recipe and suggest changes or additions for better taste.
                  2. Include important tips on each ingredient's role and cooking times.
                  3. Suggest additional ingredients or methods to enhance the flavor and taste.
                  """;

            // Call AI client
            aiClientResponse = chatClient.call(prompt);

            // Default response if AI response is empty
            if (aiClientResponse == null || aiClientResponse.isBlank()) {
                aiClientResponse = language.equalsIgnoreCase("ko")
                    ? "AI 응답이 비어 있습니다. 입력된 레시피에 대한 분석 결과를 생성하지 못했습니다."
                    : "AI response is empty. Failed to generate analysis for the provided recipe.";
            }
        } catch (Exception e) {
            LOGGER.error("Error while calling ChatClient: {}", e.getMessage());
            aiClientResponse = language.equalsIgnoreCase("ko")
                ? "AI 리뷰를 생성하지 못했습니다. 기본 메시지를 제공합니다."
                : "Failed to generate AI review. Providing default message.";
        }

        // Create and save AI review
        AIReview aiReview = aiReviewService.generateAIReview(recipeInput, user);
        aiReview.setReviewResult(aiClientResponse);
        aiReviewService.saveReview(aiReview);

        // Add AI review to the model
        model.addAttribute("aiReview", aiReview);

        return "aireview/reviewResultView";
    }

    @GetMapping("/submit-recipe")
    public String getSubmitRecipeForm() {
        return "aireview/submitRecipeForm";
    }

    @GetMapping("/my-reviews")
    public String getMyReviews(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "keyword", required = false) String keyword,
        Model model,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = aiReviewService.getUserByUsername(userDetails.getUsername());
        int size = 3;

        Page<AIReview> reviewPage = aiReviewService.getUserReviews(user, keyword, page, size);

        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "aireview/myReviewsView";
    }

    @PostMapping("/delete-review/{reviewId}")
    public String deleteReview(
        @PathVariable("reviewId") Long reviewId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = aiReviewService.getUserByUsername(userDetails.getUsername());
        AIReview review = aiReviewService.getUserReviews(user).stream()
            .filter(r -> r.getReviewId().equals(reviewId))
            .findFirst()
            .orElse(null);
        if (review != null) {
            aiReviewService.deleteReview(reviewId);
        }
        return "redirect:/my-reviews";
    }
}
