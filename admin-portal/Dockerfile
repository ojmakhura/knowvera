FROM nginx:1.31-otel

RUN rm -rf /usr/share/nginx/html/*
COPY dist/admin-portal/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

CMD ["nginx", "-g", "daemon off;"]