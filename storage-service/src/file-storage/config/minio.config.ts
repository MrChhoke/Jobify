import { registerAs } from "@nestjs/config/index.js";
import * as process from "node:process";

export default registerAs("minio", () => ({
	endPoint: process.env.MINIO_ENDPOINT || "localhost",
	port: parseInt(process.env.MINIO_PORT || "9000"),
	useSSL: process.env.MINIO_USE_SSL === "true" || false,
	accessKey: process.env.MINIO_ACCESS_KEY || "minioadmin",
	secretKey: process.env.MINIO_SECRET_KEY || "minioadmin",
	buckets: ["company-logos", "vacancy-logos", "user-avatars"],
}));
