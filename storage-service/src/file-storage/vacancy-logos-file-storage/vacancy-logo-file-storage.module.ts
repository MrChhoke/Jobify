import { LoggerService, Module } from "@nestjs/common";
import { FileStorageService } from "../file-storage.service";
import MinioClientService from "../minio-client.service";
import { VacancyLogoFileStorageController } from "./vacancy-logo-file-storage.controller";
import { ConfigModule } from "@nestjs/config";
import minioConfig from "../config/minio.config";

@Module({
	imports: [
		ConfigModule.forRoot({
			load: [minioConfig],
		}),
	],
	controllers: [VacancyLogoFileStorageController],
	providers: [
		{
			provide: FileStorageService,
			useFactory: (minioClientService: MinioClientService) => {
				return new FileStorageService(minioClientService, "vacancy-logos");
			},
			inject: [MinioClientService],
		},
		MinioClientService,
	],
})
export class VacancyLogoFileStorageModule {}
