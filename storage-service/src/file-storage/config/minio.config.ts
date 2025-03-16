import { registerAs } from "@nestjs/config/index.js";
import * as process from "node:process";

export default registerAs("minio", () => ({
	endPoint: process.env.S3_ENDPOINT || "localhost",
	port: parseInt(process.env.S3_PORT || "9000"),
	useSSL: process.env.S3_USE_SSL === "true" || false,
	accessKey: process.env.S3_ACCESS_KEY || "minioadmin",
	secretKey: process.env.S3_SECRET_KEY || "minioadmin",
	buckets: ["company-logos", "vacancy-logos", "user-avatars"],
}));
