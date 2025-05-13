import http from 'k6/http';
import {sleep, check} from 'k6';
import {HEADERS, BASE_URL, TEST_USER, registerTestUser} from './config.js';

export const options = {
    // Load test configuration
    stages: [
        {duration: '1m', target: 50}, // Ramp up to 50 users over 1 minute
        {duration: '3m', target: 50}, // Stay at 50 users for 3 minutes
        {duration: '1m', target: 0},  // Ramp down to 0 users over 1 minute
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% of requests should be below 500ms
        http_req_failed: ['rate<0.1'],    // Less than 10% of requests should fail
    },
};


export default function () {
    registerTestUser(http);
    // Test login endpoint
    const loginRes = http.post(
        `${BASE_URL}/api/v1/auth/login`,
        JSON.stringify({
            username: TEST_USER.username,
            password: TEST_USER.password,
        }),
        {headers: HEADERS}
    );

    check(loginRes, {
        'login status is 200': (r) => r.status === 200,
        'login has token': (r) => JSON.parse(r.body).token !== undefined,
    });

    if (loginRes.status === 200) {
        const token = JSON.parse(loginRes.body).token;
        const authHeaders = {
            ...HEADERS,
            'Authorization': `Bearer ${token}`,
        };

        // Test profile endpoint (example of authenticated request)
        const profileRes = http.get(
            `${BASE_URL}/api/v1/profile`,
            {headers: authHeaders}
        );

        check(profileRes, {
            'profile status is 200': (r) => r.status === 200,
        });
    }

    // Add a small pause between iterations
    sleep(1);
}