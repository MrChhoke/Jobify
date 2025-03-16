FROM nginx:1.19.6-alpine
WORKDIR /etc/nginx
COPY nginx.template /etc/nginx/nginx.template
CMD envsubst '$S3_HOST $S3_PORT $STORAGE_SERVICE_HOST $STORAGE_SERVICE_PORT' < /etc/nginx/nginx.template > /etc/nginx/nginx.conf && exec nginx -g 'daemon off;'
