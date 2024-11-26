// frontend/src/services/ProfileService.ts
import axios from 'axios';
import {User} from '../models/User';

const API_URL = 'http://localhost:8080/api/v1';

export const getProfile = async (): Promise<User> => {
    const token = localStorage.getItem('token');
    return axios.get(`${API_URL}/profile`, {
        headers: {
            Authorization: `Bearer ${token}`
        },
    })
        .then(response => response.data)
        .catch(error => {
        });
};

export const updateProfile = async (user: User): Promise<User> => {
    const token = localStorage.getItem('token');
    const {first_name, last_name} = user;
    return axios.put(`${API_URL}/profile/update`, {first_name, last_name}, {
        headers: {
            Authorization: `Bearer ${token}`
        },
    })
        .then(response => response.data)
        .catch(error => {
            // Handle error
        });
};