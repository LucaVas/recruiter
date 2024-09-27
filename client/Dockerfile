FROM --platform=linux/amd64 node:18.19.0-alpine3.18 AS builder

WORKDIR /app

COPY . /app/client/

WORKDIR /app/client

RUN npm ci

ARG VITE_API_ORIGIN=https://recruiter-api-yklq5atc4q-ew.a.run.app
ARG VITE_API_PATH=/api/v1

RUN npm run build

FROM --platform=linux/amd64 nginx:1.25.3-alpine3.18
RUN rm /usr/share/nginx/html/* -rf
COPY --from=builder app/client/dist /usr/share/nginx/html
ENV NGINX_ENVSUBST_OUTPUT_DIR '/etc/nginx/conf.d'
COPY /nginx/default.conf.template /etc/nginx/templates/default.conf.template

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
