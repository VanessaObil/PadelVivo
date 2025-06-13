# Etapa 1: Construcción
FROM node:18 AS build

WORKDIR /app

# Copiar archivos necesarios
COPY package*.json ./
RUN npm install

COPY . .

# Construir la app en modo producción
RUN npm run build -- --prod

# Etapa 2: Servir con Nginx
FROM nginx:alpine

# Copiar archivos compilados
COPY --from=build /app/dist/padelvivo /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
