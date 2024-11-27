// frontend/src/services/VacancyService.ts
import axios from 'axios';
import {Vacancy} from '../models/Vacancy';

const API_URL = 'http://localhost:8080/api/v1';

export const getVacancyById = async (id: number): Promise<Vacancy> => {
    const response = await axios.get(`${API_URL}/vacancy/${id}`);
    return response.data;
};

export const createVacancy = async (vacancy: Vacancy): Promise<Vacancy> => {
    const token = localStorage.getItem('token');
    const response = await axios.post(`${API_URL}/vacancy`, vacancy, {
        headers: {
            Authorization: `Bearer ${token}`
        },
    });
    return response.data;
};

export const updateVacancy = async (id: number, vacancy: Vacancy): Promise<Vacancy> => {
    const token = localStorage.getItem('token');
    const response = await axios.put(`${API_URL}/vacancy/${id}`, vacancy, {
        headers: {
            Authorization: `Bearer ${token}`
        },
    });
    return response.data;
};

export const deleteVacancy = async (id: number): Promise<void> => {
    const token = localStorage.getItem('token');
    await axios.delete(`${API_URL}/vacancy/${id}`, {
        headers: {
            Authorization: `Bearer ${token}`
        },
    });
};

export const getAllRecruiterVacancies = async (): Promise<{
    content: Vacancy[];
    totalPages: number;
    totalElements: number
}> => {
    const token = localStorage.getItem('token');
    const response = await axios.get(`${API_URL}/recruiter/vacancies`, {
        headers: {
            Authorization: `Bearer ${token}`
        },
    });
    return response.data;
};

export const getAllVacancies = async (page: number, size: number): Promise<{
    content: Vacancy[];
    totalPages: number;
    totalElements: number
}> => {
    const response = await axios.post(`${API_URL}/vacancy/_list`, {
        page,
        size
    });
    return response.data;
};
