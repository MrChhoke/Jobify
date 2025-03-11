import { Test, TestingModule } from "@nestjs/testing";
import { INestApplication } from "@nestjs/common";
import { GenericContainer, StartedTestContainer } from "testcontainers";
import request from "supertest";
import * as path from "path";
import { CompanyLogoFileStorageModule } from "./company-logo-file-storage.module";
import * as fs from "node:fs";

describe("CompanyLogoFileStorageController E2E Tests", () => {
	const urlRegex = /https?:\/\/[^\s/$.?#].[^\s]*/gi;
	const dateRegex = /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}Z/;

	let app: INestApplication;
	let minioContainer: StartedTestContainer;
	let testFilePath: string;

	beforeAll(async () => {
		// Read test file
		testFilePath = path.join("src/test/test-files", "apple-logo.jpg");

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
		process.env.MINIO_COMPANY_LOGOS_BUCKET = "company-logos";

		const moduleFixture: TestingModule = await Test.createTestingModule({
			imports: [CompanyLogoFileStorageModule],
		}).compile();

		app = moduleFixture.createNestApplication({});
		await app.init();
	}, 60_000);

	afterAll(async () => {
		await app.close();
		await minioContainer.stop();
	});

	it("successfully uploads a company logo file", async () => {
		const response = await request(app.getHttpServer())
			.post("/company-logos/upload")
			.attach("file", testFilePath)
			.expect(201);

		expect(response.body.fileName).toBeDefined();
		expect(response.body.fileName).toMatch(/apple-logo-\d+\.jpg/);
	});

	it("successfully retrieves a company logo file", async () => {
		const uploadResponse = await request(app.getHttpServer())
			.post("/company-logos/upload")
			.attach("file", testFilePath);

		const response = await request(app.getHttpServer())
			.get(`/company-logos/${uploadResponse.body.fileName}`)
			.expect(200);

		expect(response.body.fileName).toBeDefined();
		expect(response.body.fileName).toMatch(/apple-logo-\d+\.jpg/);
		expect(response.body.url).toBeDefined();
		expect(response.body.url).toMatch(urlRegex);
		expect(response.body.expiredDate).toBeDefined();
		expect(response.body.expiredDate).toMatch(dateRegex);
	});

	it("successfully deletes a company logo file", async () => {
		const uploadResponse = await request(app.getHttpServer())
			.post("/company-logos/upload")
			.attach("file", testFilePath);

		const response = await request(app.getHttpServer())
			.delete(`/company-logos/${uploadResponse.body.fileName}`)
			.expect(200);

		expect(response.body.message).toBeDefined();
		expect(response.body.message).toBe("File deleted successfully");
	});

	it("fails to retrieve a non-existent company logo file", async () => {
		await request(app.getHttpServer())
			.get("/company-logos/non-existent-file.jpg")
			.expect(404);
	});

	it("deleted file is not accessible", async () => {
		const uploadResponse = await request(app.getHttpServer())
			.post("/company-logos/upload")
			.attach("file", testFilePath);

		await request(app.getHttpServer())
			.get(`/company-logos/${uploadResponse.body.fileName}`)
			.expect(200);

		await request(app.getHttpServer())
			.delete(`/company-logos/${uploadResponse.body.fileName}`)
			.expect(200);

		await request(app.getHttpServer())
			.get(`/company-logos/${uploadResponse.body.fileName}`)
			.expect(404);
	});
});
