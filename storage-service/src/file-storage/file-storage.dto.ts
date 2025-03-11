export class UploadFileStorageResponseDto {
	public readonly fileName: string;
}

export class DeleteFileStorageResponseDto {
	public readonly message: string;
}

export class GetFileStorageDto {
	public readonly fileName: string;
	public readonly url: string;
	public readonly expiredDate: Date;
}
