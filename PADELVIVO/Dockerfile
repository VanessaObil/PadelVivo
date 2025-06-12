FROM node:18 AS build-stage
WORKDIR /app

# Copia sólo los archivos de dependencias para aprovechar cache
COPY package*.json ./

RUN npm install

# Copia todo el código fuente
COPY ./ ./

# Build producción (ajusta según tu proyecto)
RUN npm run build -- --prod

# Stage final para servir con nginx
FROM nginx:alpine

COPY --from=build-stage /app/dist/padelvivo /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
