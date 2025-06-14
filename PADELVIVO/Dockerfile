# Fase 1: Construcción con Node.js y Angular CLI
FROM node:18-alpine as build-stage
WORKDIR /app

# Copiar dependencias
COPY package*.json ./
RUN npm install

# Instalar Angular CLI globalmente
RUN npm install -g @angular/cli

# Copiar resto del código y construir
COPY . .
RUN ng build --configuration=production

# Fase 2: Servidor Nginx
FROM nginx:alpine
COPY --from=build-stage /app/dist/padelvivo/browser /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
