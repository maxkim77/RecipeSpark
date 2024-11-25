# RecipeSpark

## 1. 분석

## 1.1 사용자 요구사항 정의서 (User Requirements Specification)

### **프로젝트 이름:** RecipeSpark  

### **1.1.1 프로젝트 목표**
1. 요리 레시피 공유 및 요리 관련 커뮤니티 활성화.
2. 인공지능을 활용한 맞춤형 레시피 추천 및 변환 기능 제공.
3. 사용자의 재료 및 선호도를 기반으로 한 개인화된 요리 경험 지원.

### **1.1.2 주요 기능**
#### **기본 기능**
- 회원가입, 로그인, 프로필 기능
- 커뮤니티 게시판 기능(레시피 게시판, Q&A 게시판)
- AI 맞춤 레시피 및 변환 기능

#### **부가 기능**
- 스크랩 기능
- 투표 기능 (ex. 게시글 좋아요)
- 댓글 기능

### **1.1.3 사용자 요구사항**
| **ID** | **요구사항**                                                                                      | **우선순위** | **설명**                                                                                          |
|--------|---------------------------------------------------------------------------------------------------|--------------|--------------------------------------------------------------------------------------------------|
| UR-001 | 사용자는 회원가입 및 로그인을 통해 서비스를 이용할 수 있어야 한다.                                      | 필수         | 비회원 사용자는 일부 기능(열람)은 가능하지만, AI 추천 및 변환, 게시글 작성은 회원만 가능하다.                |
| UR-002 | 사용자는 레시피 게시판에서 레시피를 등록, 수정, 삭제할 수 있어야 한다.                                | 필수         | 본인이 작성한 레시피만 수정 및 삭제 가능.                                                         |
| UR-003 | 사용자는 Q&A 게시판에서 질문과 답변을 작성할 수 있어야 한다.                                          | 필수         | 요리 관련 궁금증 해결 및 커뮤니티 형성을 목적으로 한다.                                           |
| UR-004 | 사용자는 AI 레시피 게시판에서 자신의 맞춤형 레시피를 관리하고 열람할 수 있어야 한다.                    | 필수         | AI가 추천한 레시피와 변환된 레시피를 개인 게시판에서 확인.                                         |
| UR-005 | 사용자는 게시글에 좋아요를 누르거나 댓글을 작성할 수 있어야 한다.                                      | 선택         | 게시글에 대한 피드백 및 소통 기능 제공.                                                           |
| UR-006 | 사용자는 레시피를 스크랩하여 개인 저장소에 보관할 수 있어야 한다.                                      | 선택         | 관심 있는 레시피를 저장하고 관리할 수 있음.                                                       |
| UR-007 | 시스템은 모든 커뮤니티 게시글(Q&A, 레시피 게시판)의 열람은 회원/비회원 모두 가능해야 한다.               | 필수         | 접근성을 높이고 커뮤니티 활성화를 지원.                                                           |
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


### **1.2.2 레시피 게시판 CRUD**
| **유스케이스 이름** | 레시피 게시판 CRUD                      |
|---------------------|-----------------------------------------|
| **행위자**          | 로그인 사용자                          |
| **목표**            | 사용자가 레시피를 등록, 수정, 삭제할 수 있음. |
| **사전조건**        | 사용자가 로그인 상태여야 함.              |
| **사후조건**        | 게시판에 등록된 레시피가 데이터베이스에 저장됨. |
| **시나리오**        | 1. 사용자가 레시피 게시판 페이지를 연다.<br>2. 레시피를 등록하거나 기존 레시피를 수정 또는 삭제한다.<br>3. 변경된 내용이 시스템에 저장되고 화면에 반영된다. |
| **예외**            | 1. 비회원이 접근할 경우 오류 메시지 출력.<br>2. 필수 입력 항목이 비어 있을 경우 오류 메시지 출력. |


### **1.2.3 Q&A 게시판 관리**
| **유스케이스 이름** | Q&A 게시판 관리                      |
|---------------------|--------------------------------------|
| **행위자**          | 로그인 사용자 및 비회원               |
| **목표**            | 사용자가 질문을 등록하고 답변을 작성할 수 있음. |
| **사전조건**        | 질문/답변 등록은 로그인 사용자만 가능. |
| **사후조건**        | 게시판에 질문과 답변이 저장되고 열람 가능. |
| **시나리오**        | 1. 사용자가 Q&A 게시판 페이지를 연다.<br>2. 질문을 열람하거나 새 질문을 작성한다.<br>3. 기존 질문에 대해 답변을 추가한다.<br>4. 시스템에 변경 사항이 반영된다. |
| **예외**            | 1. 질문 또는 답변 내용이 비어 있는 경우 오류 메시지 출력.<br>2. 비회원이 질문/답변을 등록하려고 할 경우 로그인 요청. |


### **1.2.4 AI 맞춤 레시피 관리**
| **유스케이스 이름** | AI 맞춤 레시피 관리                   |
|---------------------|---------------------------------------|
| **행위자**          | 로그인 사용자                        |
| **목표**            | AI가 추천한 레시피를 저장 및 변환하여 개인 게시판에 관리 |
| **사전조건**        | 사용자가 로그인 상태여야 함.          |
| **사후조건**        | AI가 생성한 레시피가 개인 AI 게시판에 저장됨. |
| **시나리오**        | 1. 사용자가 AI 추천 페이지를 연다.<br>2. 필요한 재료를 입력하거나 변환 요청을 진행한다.<br>3. 생성된 맞춤형 레시피가 개인 게시판에 저장된다.<br>4. 저장된 레시피는 이후 열람 및 수정 가능. |
| **예외**            | 1. 입력된 데이터가 없을 경우 오류 메시지 출력.<br>2. AI API 호출 실패 시 '일시적인 문제 발생' 메시지 출력. |

---

## 1.3 요구사항 추적표

| **요구사항 ID** | **유스케이스 ID** | **기능 ID**    | **설명**                                  |
|-----------------|-------------------|---------------|------------------------------------------|
| UR-001         | UC-001            | F-001         | 회원가입 및 로그인 기능 제공.             |
| UR-002         | UC-002            | F-002         | 레시피 게시판 CRUD 기능 제공.             |
| UR-003         | UC-003            | F-003         | Q&A 게시판 질문 및 답변 기능 제공.         |
| UR-004         | UC-004            | F-004         | AI 기반 맞춤 레시피 관리 기능 제공.        |
| UR-005         | UC-005            | F-005         | 좋아요 및 댓글 기능 제공.                 |
| UR-006         | UC-006            | F-006         | 레시피 스크랩 기능 제공.                  |
| UR-007         | UC-007            | F-007         | 모든 게시판의 열람 기능 제공.             |
| UR-008         | UC-008            | F-008         | 데이터 보안 및 저장 관리 기능 제공.        |


## 1.4 개발 일정 (WBS)
![image](https://github.com/user-attachments/assets/39ad4ed5-6d3c-42f9-b65f-7a9e74352e6d)


---
## 2. 설계

### 2.1 아키텍처 설계

![SystemArchitecture](https://github.com/maxkim77/RecipeSpark/blob/main/tmpimg/SystemArchitecture.png?raw=true)

---

### 2.2 데이터베이스 설계(ERD)
![DBERD](https://github.com/maxkim77/RecipeSpark/blob/main/tmpimg/DB_ERD.png?raw=true)


---

### 2.3 클래스 설계
![ClassDiagram](https://github.com/maxkim77/RecipeSpark/blob/main/tmpimg/classdiagram.png?raw=true)


  - 클래스 간의 연관 관계, 상속 구조 등을 설계.
  - 도메인 로직과 데이터 구조를 기반으로 코드 구현 준비.

---

### 2.4 컴포넌트 설계
![ComponentDiagram](https://github.com/maxkim77/RecipeSpark/blob/main/tmpimg/componentdiagram.png?raw=true)
  - 각 레이어의 역할과 책임 정의.
  - 레이어 간 호출 관계 설계.
  - 데이터 흐름과 주요 서비스 간 상호작용 정의.

---

### 2.6 인터페이스 설계 (D4)

- 주요 API 엔드포인트 설계 (URL, HTTP 메서드)

### 2.6.1 AIReviewController
| HTTP Method | URL                  | 기능                     | 요청 파라미터                               | 응답 결과                         |
|-------------|----------------------|--------------------------|---------------------------------------------|------------------------------------|
| POST        | `/submit-recipe`     | AI 리뷰 요청 및 저장       | `recipeInput`(String), `language`(String)   | 리뷰 결과 데이터 및 저장 완료 메시지 |
| GET         | `/submit-recipe`     | 레시피 입력 폼 페이지     | 없음                                        | 레시피 입력 폼 뷰                  |
| GET         | `/my-reviews`        | 내 리뷰 목록 조회          | `page`(int), `keyword`(String)             | 리뷰 목록 뷰 및 페이징 처리         |
| POST        | `/delete-review/{id}`| 특정 리뷰 삭제            | `reviewId`(Long)                           | 삭제 완료 후 리뷰 목록 리다이렉트    |


### 2.6.2 AnswerController
| HTTP Method | URL                          | 기능                        | 요청 파라미터                              | 응답 결과                         |
|-------------|------------------------------|-----------------------------|--------------------------------------------|------------------------------------|
| POST        | `/answer/create`             | 답변 생성                    | `questionId`(Integer), `content`(String)  | 답변 생성 후 질문 상세 페이지 리다이렉트 |
| GET         | `/answer/editForm/{answerId}`| 답변 수정 폼 조회            | `answerId`(Integer)                       | 답변 수정 폼 뷰                   |
| POST        | `/answer/modify/{answerId}`  | 답변 수정                    | `answerId`(Integer), `content`(String)    | 수정 완료 후 질문 상세 페이지 리다이렉트 |
| POST        | `/answer/delete/{answerId}`  | 답변 삭제                    | `answerId`(Integer)                       | 삭제 완료 후 질문 상세 페이지 리다이렉트 |


### 2.6.3 CommentController
| HTTP Method | URL                              | 기능                         | 요청 파라미터                              | 응답 결과                         |
|-------------|----------------------------------|------------------------------|--------------------------------------------|------------------------------------|
| POST        | `/recipe/{recipeId}/comment`     | 댓글 생성                     | `recipeId`(Long), `content`(String)       | 댓글 생성 후 레시피 상세 페이지 리다이렉트 |
| GET         | `/recipe/{recipeId}/comment/{id}/edit`| 댓글 수정 폼 조회            | `recipeId`(Long), `commentId`(Long)       | 댓글 수정 폼 뷰                   |
| POST        | `/recipe/{recipeId}/comment/{id}/edit`| 댓글 수정                    | `recipeId`(Long), `commentId`(Long), `content`(String) | 수정 완료 후 레시피 상세 페이지 리다이렉트 |
| POST        | `/recipe/{recipeId}/comment/{id}/delete`| 댓글 삭제                   | `recipeId`(Long), `commentId`(Long)       | 삭제 완료 후 레시피 상세 페이지 리다이렉트 |


### 2.6.4 MainController
| HTTP Method | URL       | 기능                         | 요청 파라미터 | 응답 결과              |
|-------------|-----------|------------------------------|---------------|-------------------------|
| GET         | `/`       | 메인 페이지로 리다이렉트       | 없음          | 메인 페이지 리다이렉트  |
| GET         | `/main`   | 메인 페이지 조회              | 없음          | 메인 페이지 뷰          |
| GET         | `/about`  | 소개 페이지 조회              | 없음          | 소개 페이지 뷰          |


### 2.6.5 QuestionController
| HTTP Method | URL                          | 기능                         | 요청 파라미터                              | 응답 결과                         |
|-------------|------------------------------|------------------------------|--------------------------------------------|------------------------------------|
| GET         | `/question/list`            | 질문 목록 조회                | `page`(int), `kw`(String)                 | 질문 목록 뷰 및 페이징 처리         |
| GET         | `/question/detail/{id}`     | 질문 상세 조회                | `id`(Integer)                             | 질문 상세 뷰                      |
| GET         | `/question/create`          | 질문 생성 폼 조회             | 없음                                      | 질문 생성 폼 뷰                   |
| POST        | `/question/create`          | 질문 생성                     | `title`(String), `content`(String)        | 생성 완료 후 질문 목록 페이지 리다이렉트 |
| GET         | `/question/modify/{id}`     | 질문 수정 폼 조회             | `id`(Integer)                             | 질문 수정 폼 뷰                   |
| POST        | `/question/modify/{id}`     | 질문 수정                     | `id`(Integer), `title`(String), `content`(String) | 수정 완료 후 질문 상세 페이지 리다이렉트 |
| POST        | `/question/delete/{id}`     | 질문 삭제                     | `id`(Integer)                             | 삭제 완료 후 질문 목록 페이지 리다이렉트 |


### 2.6.6 RecipeController
| HTTP Method | URL                       | 기능                         | 요청 파라미터                              | 응답 결과                         |
|-------------|---------------------------|------------------------------|--------------------------------------------|-----------------------------------|
| GET         | `/recipe/list`           | 레시피 목록 조회              | `page`(int), `size`(int), `keyword`(String) | 레시피 목록 뷰 및 페이징 처리       |
| GET         | `/recipe/detail/{id}`    | 레시피 상세 조회              | `id`(Long)                                | 레시피 상세 뷰                    |
| GET         | `/recipe/create`         | 레시피 생성 폼 조회           | 없음                                      | 레시피 생성 폼 뷰                 |
| POST        | `/recipe/create`         | 레시피 생성                   | `title`(String), `content`(String), `image`(MultipartFile) | 생성 완료 후 레시피 목록 페이지 리다이렉트 |
| GET         | `/recipe/edit/{id}`      | 레시피 수정 폼 조회           | `id`(Long)                                | 레시피 수정 폼 뷰                 |
| POST        | `/recipe/edit/{id}`      | 레시피 수정                   | `id`(Long), `title`(String), `content`(String), `image`(MultipartFile) | 수정 완료 후 레시피 목록 페이지 리다이렉트 |
| POST        | `/recipe/delete/{id}`    | 레시피 삭제                   | `id`(Long)                                | 삭제 완료 후 레시피 목록 페이지 리다이렉트 |

---

### 2.7 사용자 인터페이스 설계
![UI/UX](https://github.com/maxkim77/RecipeSpark/blob/main/tmpimg/RecipeSpark.png?raw=true) 
  - 화면 구성 요소(버튼, 입력 폼, 데이터 바인딩 등) 정의.
  - 페이지 간의 이동 흐름 설계.
  - 화면 요소의 위치 및 디자인 가이드 작성.
 

--- 
## 3.구현

## 3.1 개발 환경 및 배포
- Backend: Spring Boot, Java 17
- Database: MySQL
- Frontend: Thymeleaf
- API Integration: OpenAI API (ChatGPT)
- Deployment: AWS EC2, RDS (MySQL)

## 3.2 개발일정
![wbs](https://github.com/maxkim77/RecipeSpark/blob/main/tmpimg/wbs.png?raw=true)

--- 

## 4. 테스트
### 4.1 단위테스트
# 단위 테스트 결과서

## 1. 테스트 개요
이 문서에서는 `AIReviewService`, `QuestionService`, `UserService`, `RecipeService` 등의 서비스에 대해 작성된 단위 테스트 케이스의 결과를 정리합니다. 각 테스트는 주요 서비스 로직을 검증하기 위해 작성되었습니다.

## 2. 테스트 케이스

### 2.1 AIReviewService 테스트

| 테스트 ID | 테스트 대상 | 입력값 | 예상 출력 | 결과 |
| --------- | ----------- | ------ | --------- | ---- |
| ATR001 | `generateAIReview` 메서드 | recipeInput = "Test recipe input", user = testUser | 생성된 `AIReview` 객체, 사용자 및 레시피 입력 포함 | Pass |
| ATR002 | `saveReview` 메서드 | AIReview 객체 | 저장된 `AIReview` 객체 | Pass |
| ATR003 | `getUserReviews` 메서드 | user = testUser | 해당 사용자에 대한 모든 리뷰 반환 | Pass |
| ATR004 | `deleteReview` 메서드 | reviewId = 1L | 해당 리뷰 삭제 | Pass |
| ATR005 | `getUserByUsername` 메서드 | username = "testUser" | 해당 사용자 객체 반환 | Pass |

### 2.2 QuestionService 테스트

| 테스트 ID | 테스트 대상 | 입력값 | 예상 출력 | 결과 |
| --------- | ----------- | ------ | --------- | ---- |
| QSR001 | `getList` 메서드 | keyword = "test", page = 0 | 검색된 질문 리스트 반환 | Pass |
| QSR002 | `getQuestion` 메서드 | questionId = 1 | 해당 ID의 질문 반환 | Pass |
| QSR003 | `getQuestionNotFound` 메서드 | questionId = 1 | `DataNotFoundException` 발생 | Pass |
| QSR004 | `createQuestion` 메서드 | title = "New Question", content = "This is a new question content." | 새로운 질문 생성 | Pass |
| QSR005 | `modifyQuestion` 메서드 | question 객체, newTitle = "Updated Title", newContent = "Updated Content" | 질문 수정 | Pass |
| QSR006 | `deleteQuestion` 메서드 | question 객체 | 질문 삭제 | Pass |

### 2.3 UserService 테스트

| 테스트 ID | 테스트 대상 | 입력값 | 예상 출력 | 결과 |
| --------- | ----------- | ------ | --------- | ---- |
| USR001 | `createUser` 메서드 | username = "testUser", email = "test@example.com", password = "password123" | 새 사용자 생성 | Pass |
| USR002 | `getUser` 메서드 (성공) | username = "testUser" | 해당 사용자 반환 | Pass |
| USR003 | `getUser` 메서드 (실패) | username = "nonExistingUser" | `DataNotFoundException` 발생 | Pass |
| USR004 | `validatePassword` 메서드 (성공) | password1 = "password123", password2 = "password123" | true 반환 | Pass |
| USR005 | `validatePassword` 메서드 (실패) | password1 = "password123", password2 = "differentPassword" | false 반환 | Pass |

### 2.4 RecipeService 테스트

| 테스트 ID | 테스트 대상 | 입력값 | 예상 출력 | 결과 |
| --------- | ----------- | ------ | --------- | ---- |
| RSR001 | `createRecipe` 메서드 | title = "Test Recipe", content = "Test content", user = testUser | 새 레시피 객체 생성 | Pass |
| RSR002 | `getRecipeById` 메서드 | recipeId = 1L | 해당 ID의 레시피 반환 | Pass |
| RSR003 | `testSaveImage_InvalidFileType` 메서드 | 잘못된 MIME 타입 | `RuntimeException` 발생 | Pass |

## 3. 테스트 실행 결과 요약
모든 테스트 케이스가 예상한 대로 성공적으로 수행되었습니다. 주어진 입력값에 대한 적절한 반환 값 및 예외 처리가 확인되었습니다.

## 4. 결론
테스트 결과 서비스 로직이 예상대로 잘 작동하며, 각 메서드는 입력값에 대해 올바른 출력을 반환하거나, 예외를 올바르게 처리하고 있음을 확인했습니다.
---
### 4.2 통합테스트
### 4.3 시스템테스트
### 4.4 인수테스트 결과 
