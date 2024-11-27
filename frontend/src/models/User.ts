export interface User {
    user_id: number;
    username: string;
    company?: string;
    first_name: string;
    last_name: string;
    role: string;
    token?: string;
}