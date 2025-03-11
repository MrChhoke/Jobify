import {
	Controller,
	Delete,
	Get,
	Param,
	Post,
	UploadedFile,
	UseInterceptors,
} from "@nestjs/common";
import {
	DeleteFileStorageResponseDto,
	GetFileStorageDto,
	UploadFileStorageResponseDto,
} from "../file-storage.dto";
import { Express } from "express";
import { FileInterceptor } from "@nestjs/platform-express";
import { FileStorageService } from "../file-storage.service";

@Controller("vacancy-logos")
export class VacancyLogoFileStorageController {
	public constructor(private readonly fileStorageService: FileStorageService) {}

	@Get(":fileUuid")
	public async getVacancyLogoFile(
		@Param("fileUuid") fileUuid: string,
	): Promise<GetFileStorageDto> {
		return await this.fileStorageService.getFile(fileUuid);
	}

	@Delete(":fileUuid")
	public async deleteVacancyLogoFile(
		@Param("fileUuid") fileUuid: string,
	): Promise<DeleteFileStorageResponseDto> {
		return await this.fileStorageService.deleteFile(fileUuid);
	}

	@Post("upload")
	@UseInterceptors(FileInterceptor("file"))
	public async uploadVacancyLogoFile(
		@UploadedFile() file: Express.Multer.File,
	): Promise<UploadFileStorageResponseDto> {
		return await this.fileStorageService.uploadFile(
			file.filename || file.originalname || "vacancy-logo.png",
			file.buffer,
		);
	}
}
