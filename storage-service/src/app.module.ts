import { Module } from "@nestjs/common";
import { CompanyLogoFileStorageModule } from "./file-storage/company-logos-file-storage/company-logo-file-storage.module";
import { VacancyLogoFileStorageModule } from "./file-storage/vacancy-logos-file-storage/vacancy-logo-file-storage.module";
import { UserAvatarFileStorageModule } from "./file-storage/user-avatars-file-storage/user-avatar-file-storage.module";

@Module({
	imports: [
		CompanyLogoFileStorageModule,
		VacancyLogoFileStorageModule,
		UserAvatarFileStorageModule,
	],
})
export class AppModule {}
