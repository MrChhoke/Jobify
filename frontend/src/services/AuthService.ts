// frontend/src/services/AuthService.ts
import axios from 'axios';
import {User} from '../models/User';

const API_URL = 'http://localhost:8080/api/v1';

export const login = async (username: string, password: string): Promise<User> => {
    const response = await axios.post(`${API_URL}/auth/login`, {username, password});
    localStorage.setItem('token', response.data.token);
    return response.data.user;
};

export const register = async (username: string, password: string, first_name: string, last_name: string): Promise<User> => {
    const response = await axios.post(`${API_URL}/auth/register`, {username, password, first_name, last_name});
    localStorage.setItem('token', response.data.token);
    return response.data.user;
};

export const isLoggedIn = (): boolean => {
    const token = localStorage.getItem('token');
    return !!token;
};