# Etapa 1: Build
FROM node:16-alpine as build-stage
WORKDIR /app

# Copia archivos de package.json y package-lock.json e instala dependencias
COPY package*.json ./
RUN npm install

# Copia el resto del código y genera el build de producción
COPY . .
RUN npm run build --prod

# Etapa 2: Serve con Nginx
FROM nginx:alpine
# Copia la build generada en el stage anterior a la ruta de nginx
COPY --from=build-stage /app/dist/padelvivo /usr/share/nginx/html

# Expone el puerto 80
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
