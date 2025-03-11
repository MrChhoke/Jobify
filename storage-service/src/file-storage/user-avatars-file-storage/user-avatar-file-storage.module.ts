import { LoggerService, Module } from "@nestjs/common";
import { UserAvatarFileStorageController } from "./user-avatar-file-storage.controller";
import MinioClientService from "../minio-client.service";
import { FileStorageService } from "../file-storage.service";
import { ConfigModule } from "@nestjs/config";
import minioConfig from "../config/minio.config";

@Module({
	imports: [
		ConfigModule.forRoot({
			load: [minioConfig],
		}),
	],
	controllers: [UserAvatarFileStorageController],
	providers: [
		{
			provide: FileStorageService,
			useFactory: (minioClientService: MinioClientService) => {
				return new FileStorageService(minioClientService, "user-avatars");
			},
			inject: [MinioClientService],
		},
		MinioClientService,
	],
})
export class UserAvatarFileStorageModule {}
