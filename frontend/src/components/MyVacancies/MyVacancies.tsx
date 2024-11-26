import React, {useEffect, useState} from 'react';
import {Box, Typography} from '@mui/material';
import axios from 'axios';
import './MyVacancies.css';

interface Vacancy {
    id: number;
    position: string;
    salary: number;
    technology_stack: string[];
}

interface MyVacanciesProps {
    handleOpenModal: () => void;
    user: { isAuthenticated: boolean; role: string };
}

const MyVacancies: React.FC<MyVacanciesProps> = ({handleOpenModal, user}) => {
    const [vacancies, setVacancies] = useState<Vacancy[]>([]);

    useEffect(() => {
        const fetchVacancies = async () => {
            try {
                const response = await axios.get('/api/v1/vacancy');
                setVacancies(response.data);
            } catch (error) {
                console.error('Error fetching vacancies:', error);
            }
        };

        fetchVacancies();
    }, []);

    return (
        <Box>
            <Typography variant="h4" gutterBottom>
                Мої вакансії
            </Typography>
            {user.isAuthenticated && user.role === 'RECRUITER' && (
                <button className="add-vacancy-button" onClick={handleOpenModal}>
                    Додати вакансію
                </button>
            )}
            <div className="vacancies-list">
                {vacancies.map((vacancy) => (
                    <div key={vacancy.id} className="vacancy-item">
                        <Typography variant="h6">{vacancy.position}</Typography>
                        <Typography>Salary: {vacancy.salary}</Typography>
                        <Typography>Technologies: {vacancy.technology_stack.join(', ')}</Typography>
                    </div>
                ))}
            </div>
        </Box>
    );
};

export default MyVacancies;