import React, {useEffect, useState} from 'react';
import {Box, Typography} from '@mui/material';
import {applyVacancyById, exportVacanciesToExcel, getAllVacancies} from '../../services/VacancyService';
import {Vacancy} from '../../models/Vacancy';
import './Vacancies.css';
import staticImage from "../../assets/jobIcon.png";

interface VacanciesProps {
    handleOpenModal: () => void;
    user: { isAuthenticated: boolean; role: string };
}

const Vacancies: React.FC<VacanciesProps> = ({user}) => {
    const [vacancies, setVacancies] = useState<Vacancy[]>([]);
    const [selectedVacancyId, setSelectedVacancyId] = useState<number | null>(null);

    useEffect(() => {
        const fetchVacancies = async () => {
            try {
                const response = await getAllVacancies(0, 15);
                if (response && Array.isArray(response.content)) {
                    setVacancies(response.content);
                } else {
                    console.error('API response does not contain a valid content array:', response);
                }
            } catch (error) {
                console.error('Error fetching vacancies:', error);
            }
        };

        fetchVacancies();
    }, []);

    const handleExportToExcel = async () => {
        try {
            await exportVacanciesToExcel();
            console.log('Export to Excel successful');
        } catch (error) {
            console.error('Error exporting to Excel:', error);
        }
    };

    const handleApplyVacancy = async (id: number) => {
        try {
            await applyVacancyById(id);
            console.log(`Applied to vacancy ID: ${id}`);
        } catch (error) {
            console.error(`Error applying to vacancy ID: ${id}`, error);
        }
    };

    return (
        <Box sx={{marginLeft: '20px'}} className="vacancies-container">
            <div className="filter-column">
                <h2 className="filter-title">Фільтри</h2>
                {/* Add filter options here */}
            </div>
            <div className="vacancies-column">
                <h2 className="vacancies-title">
                    Вакансії <i className="fas fa-fire"></i>
                </h2>
                <div className="vacancies-list">
                    {vacancies.length > 0 ? (
                        vacancies.map(vacancy => (
                            <div
                                key={vacancy.vacancy_id ?? Math.random()}
                                className={`vacancy-tile ${selectedVacancyId === vacancy.vacancy_id ? 'selected' : ''}`}
                                onClick={() => {
                                    console.log('Vacancy:', vacancy);
                                    if (vacancy.vacancy_id !== undefined) {
                                        console.log(`Selected vacancy ID: ${vacancy.vacancy_id}`);
                                        setSelectedVacancyId(vacancy.vacancy_id);
                                    }
                                }}
                            >
                                <img src={staticImage} alt="Vacancy" className="vacancy-image"/>
                                <div className="vacancy-details">
                                    <h3>{vacancy.position}</h3>
                                    <div className="vacancy-salary">
                                        Salary: ${vacancy.salary}
                                    </div>
                                    <p>Technologies: {vacancy.technology_stack ? vacancy.technology_stack.join(', ') : 'N/A'}</p>
                                    <button onClick={() => handleApplyVacancy(vacancy.vacancy_id ?? 0)}>
                                        Відгукнутися
                                    </button>
                                </div>
                            </div>
                        ))
                    ) : (
                        <Typography>No vacancies available</Typography>
                    )}
                </div>
            </div>
            <div className="export-column">
                {user.role === 'ADMIN' && (
                    <div className="export-excel" onClick={handleExportToExcel}>
                        Експортувати в Excel <i className="fas fa-file-excel"></i>
                    </div>
                )}
            </div>
        </Box>
    );
};

export default Vacancies;