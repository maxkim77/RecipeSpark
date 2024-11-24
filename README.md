# RecipeSpark(Java Spring Boot 기반 모놀리식 구조 프로젝트)

## 1. 서비스 개요
- 요즘 넷플릭스에서 인기있는 '흑백요리사'에서 영감을 받아 레시피를 공유하고 스크랩할 수 있는 웹사이트를 구축
- 요리에 관심 있는 다양한 레시피 공유와 질문 및 답변을 통해 요리에 대한 커뮤니케이션 활성화
- AI를 활용한 맞춤 레시피 추천과 변환 기능을 제공하는 웹 서비스

### 권한 관리
- **레시피 게시판:** 모든 사용자 접근 가능 (공개)
- **Q&A 게시판:** 모든 사용자 접근 가능 (공개)
- **AI 맞춤 레시피 및 변환:** 로그인된 사용자만 접근 가능 (비공개)

---

## 2. 권한 설계
- **레시피 게시판:** 모든 사용자 열람 가능 (공개)  
- **Q&A 게시판:** 모든 사용자 열람 가능 (공개)  
- **AI 레시피 추천 및 변환:** 로그인된 사용자만 사용 가능  

---

## 3. 기능 상세 설계

### 3.1. 레시피 게시판 (공개)
- **CRUD 기능 제공:** 레시피 등록, 조회, 수정, 삭제  
- 모든 사용자가 등록된 레시피를 열람 가능  

#### API 설계:

| HTTP Method | URL             | 기능            | 권한         |
|-------------|-----------------|----------------|--------------|
| GET         | /recipe         | 레시피 목록 조회 | 공개          |
| POST        | /recipe         | 레시피 등록      | 로그인 필요   |
| GET         | /recipe/{id}    | 레시피 상세 조회 | 공개          |
| PUT         | /recipe/{id}    | 레시피 수정      | 본인만 수정 가능 |
| DELETE      | /recipe/{id}    | 레시피 삭제      | 본인만 삭제 가능 |

**Controller 코드 예시:**
```java
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        recipe.setUsername(authentication.getName());
        return recipeService.createRecipe(recipe);
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe, Authentication authentication) {
        return recipeService.updateRecipe(id, recipe, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id, Authentication authentication) {
        recipeService.deleteRecipe(id, authentication.getName());
    }
}
3.2. Q&A 게시판 (공개)
요리에 관한 질문과 답변 기능 제공
모든 사용자가 자유롭게 질문 및 답변 작성 가능
API 설계:
HTTP Method	URL	기능	권한
GET	/qa	질문 목록 조회	공개
POST	/qa	질문 등록	로그인 필요
GET	/qa/{id}	질문 상세 조회	공개
POST	/qa/{id}/answer	답변 등록	로그인 필요
3.3. AI 맞춤 레시피 추천 및 변환 기능 (비공개)
맞춤 레시피 추천: 사용자가 가진 재료를 입력하면 AI가 가능한 레시피를 추천
레시피 변환: 사용자가 원하는 인분에 맞춰 레시피를 자동으로 변환
API 설계:
HTTP Method	URL	기능	권한
POST	/ai/recommend	맞춤 레시피 추천	로그인 필요
POST	/ai/convert	레시피 변환	로그인 필요
Controller 코드 예시:

java
코드 복사
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIRecipeController {

    private final AIRecipeService aiRecipeService;

    @PostMapping("/recommend")
    public List<Recipe> recommendRecipes(@RequestBody List<String> ingredients) {
        return aiRecipeService.recommendRecipes(ingredients);
    }

    @PostMapping("/convert")
    public Recipe convertRecipe(@RequestBody Recipe recipe, @RequestParam int servings) {
        return aiRecipeService.convertRecipe(recipe, servings);
    }
}
4. Spring Security 설정
SecurityConfig.java:

java
코드 복사
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/about", "/qa/**", "/recipe/**").permitAll()  // 공개 URL
            .antMatchers("/ai/**").authenticated()  // 인증 필요
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .permitAll()
            .and()
            .logout()
            .logoutSuccessUrl("/")
            .permitAll();
    }
}
5. 데이터베이스 모델 설계 (ERD)
mermaid
코드 복사
erDiagram
    User {
      int id PK
      varchar username
      varchar password
      varchar email
      datetime created_at
    }
    Recipe {
      int id PK
      varchar title
      text instructions
      varchar username FK
      datetime created_at
    }
    QA {
      int id PK
      varchar question
      text answer
      varchar username FK
      datetime created_at
    }

    User ||--o{ Recipe : "writes"
    User ||--o{ QA : "asks"
6. 화면 구성 (UI 설계)
메인 화면: 최신 레시피와 질문 목록 표시
레시피 게시판: 모든 사용자가 접근 가능, 레시피 조회 및 관리
Q&A 게시판: 질문과 답변 작성 및 열람 가능
AI 기능 페이지: 맞춤 레시피 추천 및 변환
7. 개발 환경 및 배포
Backend: Spring Boot, Java 17
Database: MySQL
Frontend: Thymeleaf
API Integration: OpenAI API (ChatGPT)
Deployment: AWS EC2, RDS (MySQL)
8. 개발 일정 (WBS)
mermaid
코드 복사
gantt
    title RecipeSpark 개발 일정
    dateFormat  YYYY-MM-DD
    section 기획
    요구사항 수집            :active, des1, 2024-10-28, 2d
    시스템 설계              :des2, after des1, 3d
    section 개발
    로그인/회원가입 기능      :dev1, 2024-11-01, 3d
    레시피 게시판 개발        :dev2, after dev1, 5d
    Q&A 게시판 개발          :dev3, after dev2, 4d
    AI 기능 개발             :dev4, after dev3, 5d
    section 테스트 및 배포
    통합 테스트              :test1, after dev4, 3d
    배포 및 최종 점검        :test2, after test1, 2d
9. 결론
RecipeSpark는 요리 애호가들이 서로의 레시피를 공유하며 성장할 수 있도록 돕는 플랫폼입니다.

Q&A 게시판을 통해 요리에 대한 궁금증을 해결합니다.
AI 기능을 통해 맞춤 레시피 추천과 변환을 제공합니다.
모놀리식 아키텍처로 구현하여 초기 개발과 유지보수가 용이하며, 향후 확장성도 고려했습니다.
