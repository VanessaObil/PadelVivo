# Etapa 1: Construcción de la aplicación Angular
FROM node:18 AS build

# Crear el directorio de trabajo
WORKDIR /app

# Copiar los archivos necesarios
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar el resto del código fuente
COPY . .

# Construir la aplicación Angular (modo producción)
RUN npm run build --prod

# Etapa 2: Servir con Nginx
FROM nginx:alpine

# Copiar el build de Angular al directorio de Nginx
COPY --from=build /app/dist/padelvivo /usr/share/nginx/html

# Copiar configuración personalizada de Nginx (opcional)
# COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

# Comando por defecto
CMD ["nginx", "-g", "daemon off;"]
