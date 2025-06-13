# Etapa 1: Construcción de la aplicación Angular
FROM node:18 AS build

WORKDIR /app

# Copiar solo los archivos necesarios para instalar dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar todo el código fuente
COPY . .

# Construir la aplicación (modo producción)
RUN npm run build --prod

# Etapa 2: Servir con Nginx
FROM nginx:alpine

# Copiar archivos compilados desde la etapa de build
COPY --from=build /app/dist/padelvivo /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
