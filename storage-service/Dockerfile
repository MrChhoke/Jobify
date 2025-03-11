FROM node
WORKDIR /app
COPY . /app/.
RUN npm install && npm install express
RUN npm i -D @types/multer
CMD ["npm", "start"]