# 무료 호스팅 서비스 배포 가이드

이 문서는 뉴스 요약 프로젝트를 무료 호스팅 서비스에 배포하는 방법을 설명합니다.

## 1. Railway 배포 방법

[Railway](https://railway.app/)는 무료 플랜을 제공하는 호스팅 서비스입니다.

### 설정 방법

1. [Railway](https://railway.app/) 웹사이트에 가입하고 로그인합니다.
2. 새 프로젝트를 생성하고 'Deploy from GitHub repo' 옵션을 선택합니다.
3. GitHub 리포지토리를 연결합니다.
4. 다음 환경 변수를 설정합니다:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `TOKEN_SECRET`
   - `NAVER_CLIENT_ID`
   - `NAVER_CLIENT_SECRET`
   - `OPENAI_API_KEY`
5. 배포 설정:
   - 'Settings' > 'Build & Deploy'에서 빌드 명령어 설정: `./gradlew build`
   - 시작 명령어 설정: `java -jar build/libs/*.jar`

### 제한 사항

- 무료 플랜은 매월 5달러 크레딧을 제공하며, 사용량에 따라 차감됩니다.
- 프로젝트가 일정 시간 동안 사용되지 않으면 자동으로 일시 중지됩니다.

## 2. Render 배포 방법

[Render](https://render.com/)는 무료 웹 서비스 호스팅을 제공합니다.

### 설정 방법

1. [Render](https://render.com/) 웹사이트에 가입하고 로그인합니다.
2. 대시보드에서 'New +'를 클릭하고 'Web Service'를 선택합니다.
3. GitHub 리포지토리를 연결합니다.
4. 다음 설정을 입력합니다:
   - 이름: `NewsSummary_Spring`
   - 환경: `Java`
   - 빌드 명령어: `./gradlew build`
   - 시작 명령어: `java -jar build/libs/*.jar`
5. 환경 변수 섹션에서 필요한 모든 환경 변수를 추가합니다.
6. 'Create Web Service'를 클릭합니다.

### 제한 사항

- 무료 플랜은 매월 750시간의 실행 시간을 제공합니다.
- 15분 동안 사용하지 않으면 서비스가 일시 중지되며, 다음 요청 시 다시 시작됩니다 (콜드 스타트 지연 발생).

## 3. Fly.io 배포 방법

[Fly.io](https://fly.io/)는 무료 플랜을 제공하는 클라우드 플랫폼입니다.

### 설정 방법

1. [Fly.io](https://fly.io/)에 가입하고 Fly CLI를 설치합니다:

   ```bash
   curl -L https://fly.io/install.sh | sh
   ```

2. 로그인합니다:

   ```bash
   flyctl auth login
   ```

3. 프로젝트 디렉토리에서 초기화합니다:

   ```bash
   flyctl launch
   ```

4. 생성된 `fly.toml` 파일을 수정하여 구성합니다.

5. 환경 변수를 설정합니다:

   ```bash
   flyctl secrets set DB_URL="jdbc:postgresql://..."
   flyctl secrets set DB_USERNAME="postgres"
   flyctl secrets set DB_PASSWORD="your_password"
   flyctl secrets set TOKEN_SECRET="your_token_secret"
   flyctl secrets set NAVER_CLIENT_ID="your_naver_client_id"
   flyctl secrets set NAVER_CLIENT_SECRET="your_naver_client_secret"
   flyctl secrets set OPENAI_API_KEY="your_openai_api_key"
   ```

6. 배포합니다:
   ```bash
   flyctl deploy
   ```

### 제한 사항

- 무료 플랜은 최대 3개의 공유 CPU VM과 제한된 메모리를 제공합니다.
- 매월 일정량의 무료 트래픽을 제공합니다.

## 4. Heroku 배포 방법

[Heroku](https://www.heroku.com/)는 유명한 클라우드 플랫폼이지만, 무료 플랜은 종료되었습니다. 다음은 Heroku에 배포하는 기본 방법입니다:

### 설정 방법

1. Heroku 계정에 가입하고 Heroku CLI를 설치합니다.
2. 로그인합니다:

   ```bash
   heroku login
   ```

3. 프로젝트 루트 디렉토리에 `Procfile` 파일을 추가합니다:

   ```
   web: java -jar build/libs/*.jar
   ```

4. 프로젝트 디렉토리에서 Heroku 앱을 생성합니다:

   ```bash
   heroku create news-summary-app
   ```

5. PostgreSQL 애드온을 추가합니다:

   ```bash
   heroku addons:create heroku-postgresql:hobby-dev
   ```

6. 환경 변수를 설정합니다:

   ```bash
   heroku config:set TOKEN_SECRET=your_token_secret
   heroku config:set NAVER_CLIENT_ID=your_naver_client_id
   heroku config:set NAVER_CLIENT_SECRET=your_naver_client_secret
   heroku config:set OPENAI_API_KEY=your_openai_api_key
   ```

7. 배포합니다:
   ```bash
   git push heroku main
   ```

## 5. AWS 무료 티어 사용 방법

AWS는 12개월 동안 일정 사용량에 대해 무료 티어를 제공합니다.

### EC2 설정 방법

1. AWS 계정에 가입합니다.
2. EC2 서비스로 이동하여 인스턴스 시작을 클릭합니다.
3. Amazon Linux 2023 AMI 또는 Ubuntu를 선택합니다.
4. 인스턴스 유형은 t2.micro(무료 티어)를 선택합니다.
5. 키 페어를 생성하여 다운로드합니다.
6. 보안 그룹에서 SSH(22), HTTP(80), HTTPS(443), 애플리케이션 포트(8080) 개방을 설정합니다.
7. 인스턴스를 시작합니다.
8. SSH를 통해 인스턴스에 연결합니다:

   ```bash
   ssh -i your-key.pem ec2-user@your-ec2-public-dns
   ```

9. Java 및 필요한 도구를 설치합니다:

   ```bash
   sudo yum update -y
   sudo yum install java-17-amazon-corretto -y
   sudo yum install git -y
   ```

10. 애플리케이션을 배포합니다:

    ```bash
    git clone https://github.com/your-username/NewsSummary_Spring.git
    cd NewsSummary_Spring
    ./gradlew build
    ```

11. 환경 변수를 설정하고 애플리케이션을 실행합니다:

    ```bash
    export DB_URL=jdbc:postgresql://...
    export DB_USERNAME=postgres
    export DB_PASSWORD=your_password
    export TOKEN_SECRET=your_token_secret
    export NAVER_CLIENT_ID=your_naver_client_id
    export NAVER_CLIENT_SECRET=your_naver_client_secret
    export OPENAI_API_KEY=your_openai_api_key
    java -jar build/libs/*.jar
    ```

12. (선택 사항) systemd 서비스 파일을 생성하여 애플리케이션을 백그라운드에서 실행합니다.

### 제한 사항

- 무료 티어는 12개월 동안만 유효합니다.
- 사용량 제한이 있으므로 주의해야 합니다.
