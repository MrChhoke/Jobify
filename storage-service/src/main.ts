import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module.js";
import * as dotenv from "dotenv";

(async () => {
	dotenv.config();
	const app = await NestFactory.create(AppModule);

	const port = process.env.SERVER_PORT;
	if (!port) {
		throw new Error("SERVER_PORT is not defined in .env file");
	}

	await app.listen(port);
})();
