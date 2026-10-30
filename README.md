# 여행 계획 REST API

- Render: https://week09-webservice.onrender.com
- Organization: https://github.com/2026-2-WebService/assign09-c01-22300404
- 개인 저장소: https://github.com/Evvvaaaaan/week09_webService

# 프로젝트 소개

- 여행 계획을 등록, 조회, 수정, 삭제하고 MariaDB에 저장한다.
- 여행을 좋아해서 나중에 직접 쓸 만한 서비스를 만들고 싶었다.
- Java 17, Spring Boot 3.5.6, Gradle 8.14, Spring Web, Spring Data JPA, MariaDB를 사용했다.
- IntelliJ에서 개발하고 Postman, DBeaver로 확인했다. Docker로 Render에 배포했다.
- Controller는 요청을 받고 Service는 작업을 처리한다. Repository와 Hibernate를 통해 DB에 반영한다.
- 주요 파일은 TravelApplication, TravelController, TravelService, TravelRepository, Travel, TravelRequest, TravelResponse이다.

# Entity

- 테이블은 travels이며 id 외에 6개 필드가 있다.
- id: Long, 기본키, 자동 증가
- destination: String, 여행지, VARCHAR(100)
- country: String, 국가, VARCHAR(60)
- city: String, 도시, VARCHAR(100)
- departureDate: LocalDate, 출발일, departure_date 컬럼
- durationDays: Integer, 여행일수, duration_days 컬럼
- budget: BigDecimal, 예산, DECIMAL(12,2)
- 모든 입력 필드는 NOT NULL이다.
- @Entity는 Entity 등록, @Table은 테이블 지정, @Id는 기본키, @GeneratedValue는 ID 생성, @Column은 컬럼 조건에 사용했다.
- @Service, @Transactional은 서비스 등록과 트랜잭션 처리에 사용했다.
- @RestController와 요청 매핑으로 API를 연결하고 @RequestBody, @PathVariable로 입력을 받았다.
- TravelRequest는 입력값, TravelResponse는 ID를 포함한 응답값을 전달한다.
- DB 테이블 구조

![DB 테이블 구조](screenshots/step03_travels_table.png)

# SQL 실습

- DBeaver에서 기존 book 테이블에 데이터를 추가했다. 반복 실행해 같은 내용이 2행 저장됐다.

      SHOW TABLES;
      INSERT INTO book (title, author)
      VALUES ('Database Practice', 'Student');
      SELECT * FROM book;

- 기존 테이블 결과

![기존 테이블 결과](screenshots/dbever_connection.png)

- SQL 실습은 movie_sql, JPA는 travels를 사용해 테이블을 구분했다.

      CREATE TABLE movie_sql (
          id BIGINT AUTO_INCREMENT PRIMARY KEY,
          title VARCHAR(100) NOT NULL,
          director VARCHAR(100),
          genre VARCHAR(50),
          rating DOUBLE
      );

      INSERT INTO movie_sql (title, director, genre, rating)
      VALUES ('Inception', 'Nolan', 'SF', 9.1),
             ('Interstellar', 'Nolan', 'SF', 9.3),
             ('Parasite', 'Bong', 'Drama', 9.0);

      SELECT * FROM movie_sql;
      UPDATE movie_sql SET rating = 9.5 WHERE title = 'Interstellar';
      SELECT * FROM movie_sql;
      DELETE FROM movie_sql WHERE title = 'Parasite';
      SELECT * FROM movie_sql;
      SELECT title, rating FROM movie_sql;
      SELECT * FROM movie_sql WHERE genre = 'SF';

- Interstellar 평점을 9.5로 수정하고 Parasite를 삭제했다. 최종 조회에는 SF 영화 2개가 남았다.
- 테이블 생성

![테이블 생성](screenshots/step02_tablecreate.png)

- 수정

![수정](screenshots/step02_update.png)

- 삭제

![삭제](screenshots/step02_delete.png)

- 제목과 평점 조회

![제목과 평점 조회](screenshots/step02_select_1.png)

- SF 조건 조회

![SF 조건 조회](screenshots/step02_select_2.png)

- INSERT로 영화 3개를 추가했다. 현재 step02_insert.png는 수정·삭제 후 조회 화면이다.

# JPA와 API

- TravelRepository는 JpaRepository<Travel, Long>을 상속한다.
- save()로 등록과 수정을 하고 findAll(), findById()로 조회했다. deleteById()로 삭제했다.
- SQL 실습에서는 직접 SQL을 작성했다. JPA에서는 Java 객체와 Repository 메서드를 사용하고 Hibernate가 SQL을 실행한다.
- SQL은 직접 문장을 작성하고, JPA는 메서드로 작업하므로 CRUD 코드를 작성하기 편했다.
- POST /api/travels: 등록, 201과 생성된 객체 반환
- GET /api/travels: 전체 조회, 200과 배열 반환
- GET /api/travels/{id}: 단건 조회, 200과 객체 반환
- PUT /api/travels/{id}: 수정, 200과 수정된 객체 반환
- DELETE /api/travels/{id}: 삭제, 204, 본문 없음
- 없는 ID의 단건 조회, 수정, 삭제는 404를 반환한다.
- POST와 PUT은 Content-Type: application/json을 사용하고 6개 필드를 모두 전달한다.
- 배포 테스트의 POST 요청:

      {
        "destination": "Busan Beach",
        "country": "South Korea",
        "city": "Busan",
        "departureDate": "2027-02-10",
        "durationDays": 2,
        "budget": 200000
      }

- POST 응답은 201이고 ID 6이 생성됐다.

      {
        "id": 6,
        "destination": "Busan Beach",
        "country": "South Korea",
        "city": "Busan",
        "departureDate": "2027-02-10",
        "durationDays": 2,
        "budget": 200000
      }

- GET으로 같은 값을 확인했다. PUT으로 여행일수를 4, 예산을 400000으로 바꾸고 재조회했다. 삭제 후에는 404가 나왔다.
- ID 6은 삭제된 테스트 데이터다. 새 테스트에는 POST 응답의 ID를 사용해야 한다.
- 로컬 등록 API

![로컬 등록 API](screenshots/step06_post_api.png.png)

- 로컬 등록 DB

![로컬 등록 DB](screenshots/step06_post_db.png.png)

- 로컬 조회 전체

![로컬 조회 전체](screenshots/step06_get_all.png)

- 로컬 조회 단건

![로컬 조회 단건](screenshots/step06_get_one.png)

- 로컬 수정 API

![로컬 수정 API](screenshots/step06_put_api.png)

- 로컬 수정 재조회

![로컬 수정 재조회](screenshots/step06_get_updated.png)

- 로컬 수정 DB

![로컬 수정 DB](screenshots/step06_put_db.png)

- 로컬 삭제 API

![로컬 삭제 API](screenshots/step06_delete_api.png)

- 로컬 삭제 404

![로컬 삭제 404](screenshots/step06_get_deleted.png)

- 로컬 삭제 DB

![로컬 삭제 DB](screenshots/step06_delete_db.png)

- Hibernate SQL INSERT

![Hibernate SQL INSERT](screenshots/step06_hibernate_insert.png)

- Hibernate SQL SELECT

![Hibernate SQL SELECT](screenshots/step06_hibernate_select.png)

- Hibernate SQL UPDATE

![Hibernate SQL UPDATE](screenshots/step06_hibernate_update.png.png)

- Hibernate SQL DELETE

![Hibernate SQL DELETE](screenshots/step06_hibernate_delete.png.png)

- SQL 로그의 ?는 값이 들어가는 자리다. 실제 값은 API와 DB 조회로 확인했다.

# 실행과 Render 배포

- application.properties에는 실제 계정정보 대신 DB_URL, DB_USERNAME, DB_PASSWORD를 사용했다.
- IntelliJ 실행 구성과 Render Environment에 각각 환경변수를 등록했다. 터미널 실행에는 별도로 전달해야 한다.
- ddl-auto=update로 테이블을 반영하고 show-sql=true로 SQL을 출력했다. 서버 포트는 PORT가 없으면 8080이다.
- 로컬 실행 명령: ./gradlew bootRun --no-daemon
- 테스트와 빌드 명령: ./gradlew test bootJar --no-daemon
- 남은 H2 테스트 2개는 애플리케이션 시작과 Entity 저장·재조회를 확인하며 통과했다.
- Dockerfile은 테스트와 JAR 빌드 후 Java 17에서 실행하도록 작성했다.
- GitHub에 올린 프로젝트를 Render에 연결했다.
- Docker 방식을 선택하고 DB 환경변수를 등록한 뒤 배포했다. 배포 주소로 CRUD를 확인했다.
- 배포 후 Postman으로 API를 호출하고 DBeaver로 MariaDB를 조회했다.
- 등록 201 API

![등록 201 API](screenshots/step07_post_api.png)

- 등록 201 DB 1행

![등록 201 DB 1행](screenshots/step07_post_db.png)

- 조회 200 전체

![조회 200 전체](screenshots/step07_get_all.png)

- 조회 200 단건

![조회 200 단건](screenshots/step07_get_one.png)

- 수정 200 API

![수정 200 API](screenshots/step07_put_api.png)

- 수정 200 DB

![수정 200 DB](screenshots/step07_put_db.png)

- 삭제 204 API

![삭제 204 API](screenshots/step07_delete_api.png)

- 삭제 204 조회 404

![삭제 204 조회 404](screenshots/step07_get_deleted.png)

- 삭제 204 DB 0행

![삭제 204 DB 0행](screenshots/step07_delete_db.png)

- ID 6의 여행일수를 2에서 4로, 예산을 200000에서 400000으로 수정했다. 삭제 후 API는 404, DB는 0행을 확인했다.
- 위 배포 캡처는 직접 실행한 Postman과 DBeaver 화면이다.
- [이전 AI 자동 검증 기록: ID 5](verification/render-crud-evidence.json)

# Weekly Report

- Key Learning
- 실제 서버와 DB를 연결하는 과정이 흥미로웠다.
- 데이터를 입력하거나 수정할 때 조심해야겠다고 느꼈다.
- 연결을 위해 설정해야 할 것이 생각보다 많았다.

- Problem & Solution
- Render에 DB 환경변수를 넣지 않아 연결 오류가 발생했다. 환경변수 3개를 등록하고 재배포해 해결했다.

- Code Review

      private Travel findTravel(Long id) {
          return repository.findById(id)
                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Travel not found"));
      }

- findById()로 여행을 조회하고, 없으면 orElseThrow()로 404 오류를 발생시킨다.

- AI Usage
- AI 활용: 오류 분석, 관련 개념 및 코드 예제 활용
- AI 사용률: 30%

- Reflection
- DB 연결과 트랜잭션을 더 공부하고, 데이터를 안전하게 수정하는 방법을 익히고 싶다.
