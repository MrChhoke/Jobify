import { LoggerService, Module } from "@nestjs/common";
import { CompanyLogoFileStorageController } from "./company-logo-file-storage.controller";
import { MinioClientService } from "../minio-client.service";
import { ConfigModule } from "@nestjs/config";
import minioConfig from "../config/minio.config";
import { FileStorageService } from "../file-storage.service";

@Module({
	imports: [
		ConfigModule.forRoot({
			load: [minioConfig],
		}),
	],
	controllers: [CompanyLogoFileStorageController],
	providers: [
		MinioClientService,
		{
			provide: FileStorageService,
			useFactory: (minioClientService: MinioClientService) => {
				return new FileStorageService(minioClientService, "company-logos");
			},
			inject: [MinioClientService],
		},
	],
})
export class CompanyLogoFileStorageModule {}
