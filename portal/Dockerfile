FROM nginx:1.27.2-alpine

RUN rm -rf /usr/share/nginx/html/*
COPY dist/cfpso/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

CMD ["nginx", "-g", "daemon off;"]