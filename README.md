# 뉴스 요약 프로젝트 (News Summary)

Spring Boot 기반 뉴스 요약 애플리케이션입니다.

## 주요 기능

- 뉴스 검색 및 요약
- 사용자 인증 및 관리
- API 연동 (네이버 검색 API, OpenAI API)

## 기술 스택

- Java 17
- Spring Boot 3.4.4
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT 인증

## 환경 설정 방법

1. 프로젝트 클론:

   ```bash
   git clone https://github.com/your-username/NewsSummary_Spring.git
   cd NewsSummary_Spring
   ```

2. 환경변수 설정:

   - `.env.example` 파일을 참고하여 로컬 환경에 필요한 환경변수를 설정하세요.
   - 개발 환경에서는 `application-dev.yml` 파일을 사용할 수 있습니다.

3. 데이터베이스 설정:
   - PostgreSQL 데이터베이스를 생성하고 설정합니다.
   - 데이터베이스 스키마는 JPA를 통해 자동 생성됩니다.

## 실행 방법

```bash
./gradlew bootRun
```

또는 개발 환경 프로필을 사용하려면:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## 배포 방법

### 환경 변수 설정

1. 배포 환경에 필요한 환경변수를 설정합니다:

   - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
   - `TOKEN_SECRET`
   - `NAVER_CLIENT_ID`, `NAVER_CLIENT_SECRET`
   - `OPENAI_API_KEY`

2. 배포 서버 설정:
   - 포트 설정: `SERVER_PORT`

## 보안 주의사항

- API 키, 데이터베이스 비밀번호 등의 민감한 정보는 직접 소스 코드에 포함하지 마세요.
- 환경변수나 별도의 설정 파일을 통해 민감한 정보를 관리하세요.
- `application-dev.yml`과 같은 개발용 설정 파일은 GitHub에 올리지 마세요.
- `.gitignore` 파일에 보안 파일이 포함되어 있는지 확인하세요.
