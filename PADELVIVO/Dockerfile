# Etapa 1: Construir con Node.js
FROM node:18 AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

RUN npm run build -- --prod

# Etapa 2: Servir con Nginx
FROM nginx:alpine

COPY --from=build /app/dist/padelvivo /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
