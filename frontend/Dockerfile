# Build stage
FROM node:24-slim AS build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install ALL dependencies (important for Angular CLI)
RUN npm ci

# Copy source code
COPY . .

# Build Angular app
RUN npm run build


# Production stage
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Remove default nginx static assets
RUN rm -rf ./*

# Copy the build output from the 'build' stage
# Note: Check your 'dist' folder structure; it might be dist/<project-name>/browser
COPY --from=build /app/dist/frontend/browser/ .

# Copy custom nginx config for Angular routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
