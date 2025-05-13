# Stress Testing with k6

This directory contains scripts for stress testing the Jobify backend API using [k6](https://k6.io/), an open-source load testing tool.

## What is Stress Testing?

Stress testing is designed to determine the stability and reliability of a system under various conditions. Similar to load testing, stress testing evaluates how the system performs under different levels of load, but with a focus on specific endpoints and functionality rather than overall system capacity.

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

### Authentication Stress Test

Tests the authentication endpoints (login) and authenticated requests under extreme load.

```bash
k6 run auth-stress-test.js
```

This test simulates:
- Ramp-up to 50 users over 1 minute
- Steady load of 50 users for 3 minutes
- Ramp-down to 0 users over 1 minute

## Interpreting Results

k6 provides detailed metrics after each test run, including:
- HTTP request durations
- Request rates
- Error rates
- Virtual user metrics

Pay special attention to:
1. **Response Times**: Are they within acceptable limits (under 500ms for 95% of requests)?
2. **Error Rates**: Are they below the threshold (less than 10% of requests)?
3. **Resource Utilization**: Monitor CPU, memory, and network usage on the server during the test.

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

These stress tests are designed to work with an empty database:

1. **Auto-Registration**: If the test user doesn't exist, the script will automatically register it
2. **Vacancy Handling**: Tests that require vacancies will check if they exist first and skip if none are found
3. **Error Handling**: All API calls include proper error handling to prevent test failures

The `config.js` file contains the test user credentials and helper functions for authentication. If login fails, the script will automatically try to register the user before continuing with the tests.

This approach ensures that the stress tests can run successfully even when the database is completely empty, allowing you to focus on testing the system's performance under load rather than worrying about test data setup.
