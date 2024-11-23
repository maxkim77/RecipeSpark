package com.project.RecipeSpark.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.Scrap;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.ScrapRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    /**
     * 스크랩 추가
     */
    @Transactional
    public Scrap addScrap(User user, Recipe recipe) {
        if (scrapRepository.existsByUserAndRecipe(user, recipe)) {
            throw new RuntimeException("이미 스크랩된 레시피입니다.");
        }

        Scrap scrap = new Scrap();
        scrap.setUser(user);
        scrap.setRecipe(recipe);
        return scrapRepository.save(scrap);
    }

    /**
     * 스크랩 삭제
     */
    @Transactional
    public void removeScrap(User user, Recipe recipe) {
        Scrap scrap = scrapRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new RuntimeException("해당 스크랩이 존재하지 않습니다."));

        scrapRepository.delete(scrap);
    }

    /**
     * 사용자가 해당 레시피를 스크랩했는지 여부 확인
     */
    public boolean isRecipeScrappedByUser(User user, Recipe recipe) {
        return scrapRepository.existsByUserAndRecipe(user, recipe);
    }

    /**
     * 사용자의 모든 스크랩 목록 조회
     */
    public List<Scrap> getScrapsByUser(User user) {
        return scrapRepository.findByUser(user);
    }
}
