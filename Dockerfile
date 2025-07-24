FROM amazoncorretto:21
# 시간대 설정
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
ARG JAR_FILE_PATH=build/libs/piccount-BE-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar
# 환경 변수 파일 복사
COPY .env .env
# Spring Boot 설정 파일 복사
COPY src/main/resources/application.yml application.yml
ENTRYPOINT ["java", "-jar", "app.jar"]