FROM postgres:17

# 설정 파일 복사
COPY ./init-postgres.sh /docker-entrypoint-initdb.d/init-postgres.sh

# 파일에 실행 권한 부여
RUN chmod +x /docker-entrypoint-initdb.d/init-postgres.sh
