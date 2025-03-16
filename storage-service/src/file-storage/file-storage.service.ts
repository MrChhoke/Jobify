import {
	HttpException,
	HttpStatus,
	Injectable,
	LoggerService,
} from "@nestjs/common";
import {
	DeleteFileStorageResponseDto,
	GetFileStorageDto,
	UploadFileStorageResponseDto,
} from "./file-storage.dto";
import MinioClientService from "./minio-client.service";

@Injectable()
export class FileStorageService {
	protected expiredTimeInDays: number = 3;
	protected expiredTimeInSeconds: number =
		24 * 60 * 60 * this.expiredTimeInDays;

	public constructor(
		protected readonly minioClient: MinioClientService,
		protected readonly bucketName: string,
	) {}

	public async uploadFile(
		fileNameWithExtensionFile: string,
		file: Buffer,
	): Promise<UploadFileStorageResponseDto> {
		const currentDate: Date = new Date();

		const regexSplitFileName: RegExp = /\.(png|jpg|jpeg)$/;

		if (!fileNameWithExtensionFile.match(regexSplitFileName)) {
			throw new Error("Invalid file extension");
		}

		const fileNameSplit: string[] =
			fileNameWithExtensionFile.split(regexSplitFileName);
		const fileName: string = fileNameSplit[0];
		const fileExtension: string = fileNameSplit[1];

		const newFileName: string = `${fileName}-${currentDate.getTime()}.${fileExtension}`;

		await this.minioClient
			.getClient()
			.putObject(this.bucketName, newFileName, file);

		return {
			fileName: newFileName,
		};
	}

	public async getFile(fileName: string): Promise<GetFileStorageDto> {
		const expiredDate: Date = new Date();
		expiredDate.setSeconds(
			expiredDate.getSeconds() + this.expiredTimeInSeconds,
		);

		if (!(await this.fileExistsInBucket(fileName, this.bucketName))) {
			throw new HttpException("File not found", HttpStatus.NOT_FOUND);
		}

		const presignedUrlToFile: string = await this.minioClient
			.getClient()
			.presignedUrl(
				"GET",
				this.bucketName,
				fileName,
				this.expiredTimeInSeconds,
			);

		const pathToFile: string = presignedUrlToFile
			.replace(/^(?:https?:\/\/)?[^\/]+\//, "")

		return {
			fileName: fileName,
			pathToFile: "s3/" + pathToFile,
			expiredDate: expiredDate,
		};
	}

	public async deleteFile(
		fileName: string,
	): Promise<DeleteFileStorageResponseDto> {
		await this.minioClient.getClient().removeObject(this.bucketName, fileName);

		return {
			message: "File deleted successfully",
		};
	}

	public async fileExistsInBucket(
		fileName: string,
		bucketName: string,
	): Promise<boolean> {
		return await this.minioClient
			.getClient()
			.statObject(bucketName, fileName)
			.then(() => true)
			.catch(() => false);
	}
}
