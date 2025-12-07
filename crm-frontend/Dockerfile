# Stage 1: Build Angular App
FROM node:20-alpine AS build
WORKDIR /app
COPY crm-frontend/package*.json ./
RUN npm ci
COPY crm-frontend/ .
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/crm-frontend/browser /usr/share/nginx/html
COPY crm-frontend/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
