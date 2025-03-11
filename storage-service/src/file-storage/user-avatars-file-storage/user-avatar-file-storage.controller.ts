import {
	Controller,
	Delete,
	Get,
	Param,
	Post,
	UploadedFile,
	UseInterceptors,
} from "@nestjs/common";
import { FileStorageService } from "../file-storage.service";
import { FileInterceptor } from "@nestjs/platform-express";
import {
	DeleteFileStorageResponseDto,
	GetFileStorageDto,
	UploadFileStorageResponseDto,
} from "../file-storage.dto";
import { Express } from "express";

@Controller("user-avatars")
export class UserAvatarFileStorageController {
	public constructor(private readonly fileStorageService: FileStorageService) {}

	@Get(":fileUuid")
	public async getUserAvatarFile(
		@Param("fileUuid") fileUuid: string,
	): Promise<GetFileStorageDto> {
		return await this.fileStorageService.getFile(fileUuid);
	}

	@Delete(":fileUuid")
	public async deleteUserAvatarFile(
		@Param("fileUuid") fileUuid: string,
	): Promise<DeleteFileStorageResponseDto> {
		return await this.fileStorageService.deleteFile(fileUuid);
	}

	@Post("upload")
	@UseInterceptors(FileInterceptor("file"))
	public async uploadUserAvatarFile(
		@UploadedFile() file: Express.Multer.File,
	): Promise<UploadFileStorageResponseDto> {
		return await this.fileStorageService.uploadFile(
			file.filename || file.originalname || "user-avatar.png",
			file.buffer,
		);
	}
}
