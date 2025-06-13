# Etapa 1: Build Angular app
FROM node:18 AS build

WORKDIR /app

# Instala dependencias
COPY package*.json ./
COPY angular.json ./
COPY tsconfig*.json ./
COPY . .

RUN npm install
RUN npm run build -- --configuration production

# Etapa 2: Servir con Nginx
FROM nginx:alpine

# Copia archivos compilados
COPY --from=build /app/dist/padelvivo /usr/share/nginx/html

# Exponer puerto
EXPOSE 80

# Arranca Nginx
CMD ["nginx", "-g", "daemon off;"]
