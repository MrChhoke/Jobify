# Build stage
FROM node:18-alpine as builder
WORKDIR /app
COPY . /app
RUN npm install && npm run build

# Production stage
FROM node:18-alpine
WORKDIR /app
# Copy the build artifacts from the builder stage
COPY --from=builder /app/build /app
# Install a static file server
RUN npm install -g serve
# Expose the port the app will run on
EXPOSE 3000
# Use serve to serve the static files
CMD ["serve", "-s", ".", "-l", "3000"]
