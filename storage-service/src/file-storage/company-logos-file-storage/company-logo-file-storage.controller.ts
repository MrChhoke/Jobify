import {
	Controller,
	Delete,
	Get,
	Param,
	Post,
	UploadedFile,
	UseInterceptors,
} from "@nestjs/common";
import { FileInterceptor } from "@nestjs/platform-express";
import {
	DeleteFileStorageResponseDto,
	GetFileStorageDto,
	UploadFileStorageResponseDto,
} from "../file-storage.dto";
import { Express } from "express";
import { FileStorageService } from "../file-storage.service";

@Controller("company-logos")
export class CompanyLogoFileStorageController {
	public constructor(private readonly fileStorageService: FileStorageService) {}

	@Get(":fileUuid")
	public async getCompanyLogoFile(
		@Param("fileUuid") fileUuid: string,
	): Promise<GetFileStorageDto> {
		return await this.fileStorageService.getFile(fileUuid);
	}

	@Delete(":fileUuid")
	public async deleteCompanyLogoFile(
		@Param("fileUuid") fileUuid: string,
	): Promise<DeleteFileStorageResponseDto> {
		return await this.fileStorageService.deleteFile(fileUuid);
	}

	@Post("upload")
	@UseInterceptors(FileInterceptor("file"))
	public async uploadCompanyLogoFile(
		@UploadedFile() file: Express.Multer.File,
	): Promise<UploadFileStorageResponseDto> {
		return await this.fileStorageService.uploadFile(
			file.filename || file.originalname || "company-logo.png",
			file.buffer,
		);
	}
}
