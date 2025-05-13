// Configuration file for k6 load tests
export const BASE_URL = 'http://127.0.0.1:8080'; // Adjust this to match your backend URL

// Common headers
export const HEADERS = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
};

// Test data for authentication
export const TEST_USER = {
  username: 'loadtest',
  password: 'Password123!',
};

// Helper function to register a test user
export function registerTestUser(http) {
  console.log(`Attempting to register test user: ${TEST_USER.username}`);
  console.log(`Sending registration request to ${BASE_URL}/api/v1/auth/register`);

  const registerRes = http.post(`${BASE_URL}/api/v1/auth/register`, JSON.stringify({
    username: TEST_USER.username,
    password: TEST_USER.password,
    first_name: 'Load',
    last_name: 'Test',
  }), {
    headers: HEADERS,
  });

  if (registerRes.status === 201) {
    console.log(`Successfully registered test user: ${TEST_USER.username}`);
    const body = JSON.parse(registerRes.body);
    return body.token;
  } else {
    console.log(`Failed to register test user: ${registerRes.status} ${registerRes.body}`);
    return null;
  }
}

// Helper function to get authentication token
export function getAuthToken(http) {
  const loginRes = http.post(`${BASE_URL}/api/v1/auth/login`, JSON.stringify({
    username: TEST_USER.username,
    password: TEST_USER.password,
  }), {
    headers: HEADERS,
  });
  
  if (loginRes.status === 200) {
    const body = JSON.parse(loginRes.body);
    return body.token;
  } else {
    console.log(`Failed to get auth token: ${loginRes.status} ${loginRes.body}`);
    return null;
  }
}
