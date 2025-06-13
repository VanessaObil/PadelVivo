FROM node:18 AS build

WORKDIR /app

# Copia todo el contenido del frontend
COPY . .

RUN npm install
RUN npm run build -- --configuration production

FROM nginx:alpine

COPY --from=build /app/dist/padelvivo /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
