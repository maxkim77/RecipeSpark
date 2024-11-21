package com.project.RecipeSpark.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import io.github.cdimascio.dotenv.Dotenv;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.service.AIReviewService;
import com.project.RecipeSpark.domain.AIReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

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
        this.apiKey = dotenv.get("OPENAI_API_KEY", System.getenv("OPENAI_API_KEY"));
        this.chatClient = chatClient;
        this.aiReviewService = aiReviewService;
    }



    @PostMapping("/submit-recipe")
    public String submitRecipe(@RequestParam("recipeInput") String recipeInput, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // 현재 사용자 가져오기
        User user = aiReviewService.getUserByUsername(userDetails.getUsername());

        String aiClientResponse;
        try {
            // AI 클라이언트 호출
            aiClientResponse = chatClient.call(
                """
                사용자가 입력한 레시피에 대한 상세한 피드백을 생성하세요.
                레시피는 다음과 같습니다:
                """ + recipeInput + """
                
                1. 레시피의 개선점에 대해 설명하고, 어떤 부분을 추가하거나 변경하면 더 맛있어질지 제안하세요.
                2. 각 재료의 역할과 끓이는 시간 등 조리 과정에서 중요한 팁도 포함해주세요.
                3. 음식의 풍미와 맛을 높일 수 있는 추가 재료나 방법이 있다면 제안해주세요.
                """
            );

            // AI 응답이 비어 있는 경우 기본 메시지 설정
            if (aiClientResponse == null || aiClientResponse.isBlank()) {
                aiClientResponse = "AI 응답이 비어 있습니다. 입력된 레시피에 대한 분석 결과를 생성하지 못했습니다.";
            }
        } catch (Exception e) {
            LOGGER.error("Error while calling ChatClient: {}", e.getMessage());
            aiClientResponse = "AI 리뷰를 생성하지 못했습니다. 기본 메시지를 제공합니다.";
        }

        // AI 리뷰 객체 생성 및 설정
        AIReview aiReview = aiReviewService.generateAIReview(recipeInput, user);
        aiReview.setReviewResult(aiClientResponse);

        // 데이터베이스에 저장
        aiReviewService.saveReview(aiReview);

        // 모델에 리뷰 추가
        model.addAttribute("aiReview", aiReview);

        return "aireview/reviewResultView";
    }



    @GetMapping("/submit-recipe")
    public String getSubmitRecipeForm() {
        return "aireview/submitRecipeForm";
    }

    @GetMapping("/my-reviews")
    public String getMyReviews(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = aiReviewService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("reviews", aiReviewService.getUserReviews(user));
        return "aireview/myReviewsView";
    }

    @PostMapping("/delete-review/{reviewId}")
    public String deleteReview(@PathVariable("reviewId")  Long reviewId, @AuthenticationPrincipal UserDetails userDetails) {
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