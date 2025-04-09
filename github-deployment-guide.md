# GitHub 배포 가이드

이 문서는 뉴스 요약 프로젝트를 GitHub에 무료로 배포하는 방법을 설명합니다.

## 1. GitHub 리포지토리 생성

1. GitHub 계정에 로그인합니다.
2. 오른쪽 상단의 '+' 버튼을 클릭하고 'New repository'를 선택합니다.
3. 리포지토리 이름을 입력합니다 (예: `NewsSummary_Spring`).
4. 설명을 추가하고 필요한 경우 'Public' 옵션을 선택합니다.
5. README 파일 초기화 옵션은 체크하지 않습니다 (이미 프로젝트에 있음).
6. 'Create repository'를 클릭합니다.

## 2. 로컬 프로젝트 준비

1. 프로젝트 루트 디렉토리에서 Git 초기화 (이미 초기화되어 있다면 생략):

   ```bash
   git init
   ```

2. 변경 사항을 스테이징합니다:

   ```bash
   git add .
   ```

3. 보안 파일이 `.gitignore`에 포함되어 있는지 확인합니다:

   ```bash
   git status
   ```

   - `src/main/resources/application-dev.yml`과 같은 민감한 정보를 포함하는 파일이 표시되지 않아야 합니다.

4. 변경 사항을 커밋합니다:

   ```bash
   git commit -m "Initial commit"
   ```

5. GitHub 리포지토리를 원격 저장소로 추가합니다:

   ```bash
   git remote add origin https://github.com/your-username/NewsSummary_Spring.git
   ```

6. 변경 사항을 푸시합니다:
   ```bash
   git push -u origin main
   ```
   (브랜치 이름이 'master'인 경우: `git push -u origin master`)

## 3. GitHub Actions를 사용한 CI/CD 설정 (선택 사항)

GitHub Actions를 사용하여 자동 빌드 및 테스트를 설정할 수 있습니다:

1. 프로젝트에 `.github/workflows` 디렉토리를 생성합니다:

   ```bash
   mkdir -p .github/workflows
   ```

2. CI 워크플로우 파일을 생성합니다:
   `.github/workflows/ci.yml`

```yaml
name: Java CI with Gradle

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: "17"
          distribution: "temurin"
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build with Gradle
        run: ./gradlew build
        env:
          DB_URL: ${{ secrets.DB_URL }}
          DB_USERNAME: ${{ secrets.DB_USERNAME }}
          DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
          TOKEN_SECRET: ${{ secrets.TOKEN_SECRET }}
          NAVER_CLIENT_ID: ${{ secrets.NAVER_CLIENT_ID }}
          NAVER_CLIENT_SECRET: ${{ secrets.NAVER_CLIENT_SECRET }}
          OPENAI_API_KEY: ${{ secrets.OPENAI_API_KEY }}
```

3. GitHub 리포지토리 설정에서 'Secrets and variables' > 'Actions'로 이동하여 필요한 환경 변수를 추가합니다.

## 4. GitHub Pages를 사용한 API 문서 배포 (선택 사항)

SpringDoc을 사용하여 API 문서를 생성하고 GitHub Pages에 배포할 수 있습니다:

1. `build.gradle`에 SpringDoc 의존성을 추가합니다:

   ```gradle
   implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2'
   ```

2. API 문서를 생성하고 배포하는 워크플로우 파일을 생성합니다:
   `.github/workflows/docs.yml`

```yaml
name: Deploy API Documentation

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: "17"
          distribution: "temurin"
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Generate API Documentation
        run: ./gradlew bootRun --args='--spring.profiles.active=docs' &
        env:
          DB_URL: ${{ secrets.DB_URL }}
          DB_USERNAME: ${{ secrets.DB_USERNAME }}
          DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
          TOKEN_SECRET: ${{ secrets.TOKEN_SECRET }}
          NAVER_CLIENT_ID: ${{ secrets.NAVER_CLIENT_ID }}
          NAVER_CLIENT_SECRET: ${{ secrets.NAVER_CLIENT_SECRET }}
          OPENAI_API_KEY: ${{ secrets.OPENAI_API_KEY }}
      - name: Wait for API documentation
        run: sleep 30
      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./src/main/resources/static/docs
```

## 5. GitHub 프로젝트 관리 활용하기

1. 'Projects' 탭에서 새 프로젝트를 생성하여 작업을 관리합니다.
2. 'Issues' 탭에서 새 이슈를 생성하여 버그 또는 개선 사항을 추적합니다.
3. 'Pull Requests'를 통해 코드 리뷰 및 협업을 진행합니다.

## 6. 보안 관련 추가 설정

1. GitHub의 'Settings' > 'Secrets and variables' > 'Actions'에서 다음 비밀 정보를 설정합니다:

   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `TOKEN_SECRET`
   - `NAVER_CLIENT_ID`
   - `NAVER_CLIENT_SECRET`
   - `OPENAI_API_KEY`

2. GitHub의 'Settings' > 'Security' > 'Code scanning'에서 코드 스캔 도구를 활성화하여 보안 취약점을 자동으로 검사합니다.
