# 1. 분석

## 1.1 사용자 요구사항 정의서 (User Requirements Specification)

### **프로젝트 이름:** RecipeSpark  

### **1.1.1 프로젝트 목표**
1. 요리 레시피 공유 및 요리 관련 커뮤니티 활성화.
2. 인공지능을 활용한 맞춤형 레시피 추천 및 변환 기능 제공.
3. 사용자의 재료 및 선호도를 기반으로 한 개인화된 요리 경험 지원.

### **1.1.2 주요 사용자**
1. **일반 사용자**: 요리에 관심 있는 사용자로, 레시피를 등록하거나 커뮤니티에 참여함.
2. **요리 초보자**: 요리에 대해 배우고, 필요한 레시피를 찾고 질문하려는 사용자.
3. **요리 전문가**: 자신의 레시피를 공유하거나, 커뮤니티에 답변을 통해 기여하는 사용자.

### **1.1.3 사용자 요구사항**
| **ID** | **요구사항**                                                                                      | **우선순위** | **설명**                                                                                          |
|--------|---------------------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------------------------------------------|
| UR-001 | 사용자는 회원가입 및 로그인을 통해 서비스를 이용할 수 있어야 한다.                                      | 필수         | 비회원 사용자는 일부 기능(열람)은 가능하지만, AI 추천 및 변환, 게시글 작성은 회원만 가능하다.                |
| UR-002 | 사용자는 레시피를 등록, 수정, 삭제할 수 있어야 한다.                                                | 필수         | 본인이 작성한 레시피만 수정 및 삭제 가능.                                                         |
| UR-003 | 사용자는 AI 기반의 맞춤 레시피 추천 기능을 사용할 수 있어야 한다.                                    | 필수         | 사용자가 입력한 재료를 기반으로 AI가 가능한 레시피를 추천한다.                                      |
| UR-004 | 사용자는 AI 기반의 레시피 변환 기능을 통해 요리 인분을 조정할 수 있어야 한다.                         | 필수         | 입력한 인분 수에 따라 재료의 양이 자동으로 조정된다.                                              |
| UR-005 | 사용자는 게시글에 댓글을 작성하고 좋아요를 누를 수 있어야 한다.                                      | 선택         | 게시글에 대한 피드백 및 소통 기능 제공.                                                           |
| UR-006 | 사용자는 커뮤니티 Q&A 게시판에서 질문 및 답변을 작성할 수 있어야 한다.                               | 필수         | 요리 관련 궁금증 해결 및 커뮤니티 형성을 목적으로 한다.                                           |
| UR-007 | 시스템은 모든 게시글의 열람은 회원/비회원 모두 가능해야 한다.                                        | 필수         | 접근성을 높이고 커뮤니티 활성화를 지원.                                                           |
| UR-008 | 시스템은 사용자 데이터와 게시글 정보를 안전하게 저장 및 관리해야 한다.                               | 필수         | MySQL DB와 Spring Security를 통해 데이터 보안 강화.                                               |

---

## 1.2 유스케이스 명세서

### **1.2.1 회원가입**
| **유스케이스 이름** | 회원가입                             |
|---------------------|--------------------------------------|
| **행위자**          | 비회원                              |
| **목표**            | 사용자가 RecipeSpark 서비스에 가입   |
| **사전조건**        | 이메일과 비밀번호를 입력할 수 있어야 함. |
| **사후조건**        | 가입된 사용자 정보가 데이터베이스에 저장됨. |
| **시나리오**        | 1. 비회원이 회원가입 페이지를 연다.<br>2. 이메일, 비밀번호, 닉네임을 입력한다.<br>3. 가입 버튼을 클릭한다.<br>4. 시스템이 입력 정보를 검증하고 가입 처리한다.<br>5. 성공 시 환영 메시지가 출력되고 로그인 페이지로 리다이렉트된다. |
| **예외**            | 1. 이메일이 중복될 경우 오류 메시지 출력.<br>2. 필수 입력 항목이 비어 있을 경우 오류 메시지 출력. |

---

### **1.2.2 AI 맞춤 레시피 추천**
| **유스케이스 이름** | 맞춤 레시피 추천                        |
|---------------------|-----------------------------------------|
| **행위자**          | 로그인 사용자                          |
| **목표**            | 사용자가 입력한 재료를 기반으로 레시피 추천 |
| **사전조건**        | 사용자가 로그인 상태여야 함.              |
| **사후조건**        | 추천된 레시피 리스트가 사용자에게 표시됨. |
| **시나리오**        | 1. 사용자가 AI 추천 페이지를 연다.<br>2. 가지고 있는 재료 리스트를 입력한다.<br>3. 추천 요청 버튼을 클릭한다.<br>4. 시스템이 AI API를 호출하여 추천 가능한 레시피를 가져온다.<br>5. 추천된 레시피가 화면에 표시된다. |
| **예외**            | 1. 입력된 재료가 없을 경우 오류 메시지 출력.<br>2. AI API 호출 실패 시 '일시적인 문제 발생' 메시지 출력. |

---

## 1.3 요구사항 추적표

| **요구사항 ID** | **유스케이스 ID** | **기능 ID**    | **설명**                                  |
|-----------------|-------------------|---------------|------------------------------------------|
| UR-001         | UC-001            | F-001         | 회원가입 및 로그인 기능 제공.             |
| UR-002         | UC-002, UC-003    | F-002, F-003  | 레시피 등록, 수정, 삭제 기능 제공.         |
| UR-003         | UC-004            | F-004         | AI 기반 맞춤 레시피 추천 제공.            |
| UR-004         | UC-005            | F-005         | 레시피 변환 기능 제공.                    |
| UR-005         | UC-006            | F-006         | 좋아요 및 댓글 기능 제공.                 |
| UR-006         | UC-007            | F-007         | Q&A 게시판에서 질문 및 답변 기능 제공.     |
| UR-008         | UC-008            | F-008         | 데이터 보안 및 저장 관리 기능 제공.        |


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
