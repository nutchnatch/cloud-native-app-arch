#https://medium.com/better-programming/customize-your-mysql-database-in-docker-723ffd59d8fb
# Derived from official mysql image (our base image)
FROM mysql:latest
# Add a database
ENV MYSQL_DATABASE db
# Add the content of the sql-scripts/ directory to your image
# All scripts in docker-entrypoint-initdb.d/ are automatically
# executed during container startup
COPY ./schema.sql /docker-entrypoint-initdb.d/