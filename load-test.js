import http from 'k6/http';
import { check } from 'k6';

export const options = {
    maxRedirects: 0,

    scenarios: {
        redirect_load: {
            executor: 'constant-vus',
            vus: 100,
            duration: '30s',
        },
    },
};

export default function () {
    const response = http.get(
        'http://localhost:8080/6J5skeLVzW'
    );

    check(response, {
        'status is 302': (r) => r.status === 302,
    });
}