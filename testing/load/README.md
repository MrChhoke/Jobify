# Load Testing with k6

This directory contains scripts for load testing the Jobify backend API using [k6](https://k6.io/), an open-source load testing tool.

## Prerequisites

1. Install k6: https://k6.io

## Configuration

The `config.js` file contains common configuration settings:
- Base URL for the backend API
- Common headers for API requests
- Test user credentials
- Helper functions for authentication

Modify this file to match your environment and test requirements.

## Available Tests

### Authentication Load Test

Tests the authentication endpoints (login) and an authenticated request to the profile endpoint.

```bash
k6 run auth-load-test.js
```

This test simulates:
- Ramp-up to 50 concurrent users over 1 minute
- Steady load of 50 users for 3 minutes
- Ramp-down to 0 users over 1 minute

## Customizing Tests

You can modify the test parameters by editing the `options` object in each test file:

```javascript
export const options = {
  stages: [
    { duration: '1m', target: 50 }, // Adjust duration and target users
    { duration: '3m', target: 50 },
    { duration: '1m', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // Adjust performance thresholds
    http_req_failed: ['rate<0.1'],
  },
};
```

## Working with Empty Database

These tests are designed to work with an empty database:

1. **Auto-Registration**: If the test user doesn't exist, the script will automatically register it
2. **Vacancy Handling**: Tests that require vacancies will check if they exist first and skip if none are found
3. **Error Handling**: All API calls include proper error handling to prevent test failures

The `config.js` file contains the test user credentials and helper functions for authentication. If login fails, the script will automatically try to register the user before continuing with the tests.

## Interpreting Results

k6 provides detailed metrics after each test run, including:
- HTTP request durations
- Request rates
- Error rates
- Virtual user metrics
