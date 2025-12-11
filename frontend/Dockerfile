# ====================
# Stage 1: Build FE
# ====================
FROM node:22-alpine AS build
WORKDIR /app

# Copy only package files first
COPY package.json package-lock.json ./
RUN npm ci

# Copy source
COPY . .

# Build app
RUN npm run build

# ====================
# Stage 2: Serve FE
# ====================
FROM nginx:1.27-alpine

# Xóa default nginx config
RUN rm -rf /etc/nginx/conf.d/*

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy built FE
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
