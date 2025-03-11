import { Client } from "minio";
import { ConfigService } from "@nestjs/config";
import { Injectable, Logger, OnModuleInit } from "@nestjs/common";

@Injectable({})
export class MinioClientService implements OnModuleInit {
	private readonly minioClient: Client;
	private readonly logger = new Logger(MinioClientService.name);

	public constructor(private readonly configService: ConfigService) {
		this.minioClient = new Client({
			endPoint: this.configService.get("minio.endPoint") || "localhost",
			port: this.configService.get("minio.port") || 9000,
			useSSL: this.configService.get("minio.useSSL") || false,
			accessKey: this.configService.get("minio.accessKey") || "minioadmin",
			secretKey: this.configService.get("minio.secretKey") || "minioadmin",
		});
	}

	public async onModuleInit(): Promise<void> {
		await this.initializeBuckets();
	}

	public async createBucket(bucket: string): Promise<void> {
		if (await this.minioClient.bucketExists(bucket)) {
			this.logger.debug(`Bucket ${bucket} already exists`);
			return;
		}

		await this.minioClient.makeBucket(bucket);
		this.logger.debug(`Bucket ${bucket} created`);
	}

	private async initializeBuckets(): Promise<void> {
		const buckets = this.configService.get<string[]>("minio.buckets") || [];

		for (const bucket of buckets) {
			await this.createBucket(bucket);
		}
	}

	public getClient(): Client {
		return this.minioClient;
	}
}

export default MinioClientService;
