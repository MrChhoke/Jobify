module.exports = {
	preset: "ts-jest",
	testEnvironment: "node",
	rootDir: "./",
	moduleFileExtensions: ["ts", "js"],
	testRegex: ".spec.ts$",
	transform: {
		"^.+\\.ts$": "ts-jest",
	},
};
