import { INestApplication } from "@nestjs/common";
import { GenericContainer, StartedTestContainer } from "testcontainers";
import { Test, TestingModule } from "@nestjs/testing";
import { VacancyLogoFileStorageModule } from "./vacancy-logo-file-storage.module";
import * as fs from "node:fs";
import * as path from "node:path";
import request from "supertest";

describe("VacancyLogoFileStorageController E2E", () => {
	const urlRegex = /s3\/vacancy-logos\/[^\/\s]+\.(?:png|jpg|jpeg|gif|webp)(?:\?.*)?/gi;
	const dateRegex = /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}Z/;

	let app: INestApplication;
	let minioContainer: StartedTestContainer;
	let testFilePath: string;

	beforeAll(async () => {
		// Read test file
		testFilePath = path.join("src/test/test-files", "vacancy-logo.png");

		if (!fs.existsSync(testFilePath)) {
			throw new Error("Test file not found");
		}

		// Start MinIO container
		minioContainer = await new GenericContainer("minio/minio")
			.withExposedPorts(9000)
			.withEnvironment({
				MINIO_ACCESS_KEY: "minioadmin",
				MINIO_SECRET_KEY: "minioadmin",
			})
			.withCommand(["server", "/data"])
			.start();

		// Set environment variables
		process.env.MINIO_ENDPOINT = minioContainer.getHost();
		process.env.MINIO_PORT = minioContainer.getMappedPort(9000).toString();
		process.env.MINIO_ACCESS_KEY = "minioadmin";
		process.env.MINIO_SECRET_KEY = "minioadmin";
		process.env.MINIO_VACANCY_LOGOS_BUCKET = "vacancy-logos";

		const moduleFixture: TestingModule = await Test.createTestingModule({
			imports: [VacancyLogoFileStorageModule],
		}).compile();

		app = moduleFixture.createNestApplication();
		await app.init();
	}, 60_000);

	afterAll(async () => {
		await app.close();
		await minioContainer.stop();
	});

	it("should upload a vacancy logo file", async () => {
		const response = await request(app.getHttpServer())
			.post("/vacancy-logos/upload")
			.attach("file", testFilePath)
			.expect(201);
		expect(response.body.fileName).toMatch(/vacancy-logo-\d{13}\.png/);
	});

	it("should get a vacancy logo file", async () => {
		const uploadResponse = await request(app.getHttpServer())
			.post("/vacancy-logos/upload")
			.attach("file", testFilePath)
			.expect(201);
		const response = await request(app.getHttpServer())
			.get(`/vacancy-logos/${uploadResponse.body.fileName}`)
			.expect(200);
		expect(response.body).toEqual({
			pathToFile: expect.stringMatching(urlRegex),
			fileName: uploadResponse.body.fileName,
			expiredDate: expect.stringMatching(dateRegex),
		});
	});

	it("should delete a vacancy logo file", async () => {
		const uploadResponse = await request(app.getHttpServer())
			.post("/vacancy-logos/upload")
			.attach("file", testFilePath)
			.expect(201);

		const response = await request(app.getHttpServer())
			.delete(`/vacancy-logos/${uploadResponse.body.fileName}`)
			.expect(200);
		expect(response.body).toEqual({
			message: "File deleted successfully",
		});
	});

	it("fails to retrieve a non-existent vacancy logo file", async () => {
		await request(app.getHttpServer())
			.get("/vacancy-logos/non-existent-file.png")
			.expect(404);
	});
});
