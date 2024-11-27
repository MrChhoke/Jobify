import React, {useEffect, useState} from 'react';
import {Box, Typography} from '@mui/material';
import {deleteVacancy, getAllRecruiterVacancies, updateVacancy} from '../../services/VacancyService';
import {Vacancy} from '../../models/Vacancy';
import './MyVacancies.css';
import staticImage from "../../assets/jobIcon.png";
import UpdateVacancyModal from '../UpdateVacancyModal/UpdateVacancyModal';
import {getProfile} from '../../services/ProfileService';

interface MyVacanciesProps {
    handleOpenModal: () => void;
    user: { isAuthenticated: boolean; role: string };
}

const MyVacancies: React.FC<MyVacanciesProps> = ({handleOpenModal, user}) => {
    const [vacancies, setVacancies] = useState<Vacancy[]>([]);
    const [selectedVacancyId, setSelectedVacancyId] = useState<number | null>(null);
    const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
    const [companyName, setCompanyName] = useState<string>('');

    useEffect(() => {
        const fetchVacancies = async () => {
            try {
                const response = await getAllRecruiterVacancies();
                if (response && Array.isArray(response.content)) {
                    setVacancies(response.content);
                } else {
                    console.error('API response does not contain a valid content array:', response);
                }
            } catch (error) {
                console.error('Error fetching vacancies:', error);
            }
        };

        const fetchProfile = async () => {
            try {
                const profile = await getProfile();
                setCompanyName(profile.company || '');
            } catch (error) {
                console.error('Error fetching profile:', error);
            }
        };

        fetchVacancies();
        fetchProfile();
    }, []);

    const handleDeleteVacancy = async () => {
        if (selectedVacancyId !== null) {
            try {
                await deleteVacancy(selectedVacancyId);
                setVacancies(vacancies.filter(vacancy => vacancy.vacancy_id !== selectedVacancyId));
                setSelectedVacancyId(null);
                window.location.reload();
            } catch (error) {
                console.error('Error deleting vacancy:', error);
            }
        }
    };

    const handleUpdateVacancy = async (updatedVacancy: Vacancy) => {
        if (selectedVacancyId !== null) {
            try {
                await updateVacancy(selectedVacancyId, updatedVacancy);
                setVacancies(vacancies.map(vacancy => (vacancy.vacancy_id === selectedVacancyId ? updatedVacancy : vacancy)));
                setSelectedVacancyId(null);
                window.location.reload();
            } catch (error) {
                console.error('Error updating vacancy:', error);
            }
        }
    };

    const openUpdateModal = () => {
        if (selectedVacancyId !== null) {
            setIsUpdateModalOpen(true);
        }
    };

    const closeUpdateModal = () => {
        setIsUpdateModalOpen(false);
    };

    return (
        <Box sx={{marginLeft: '20px'}} className="my-vacancies-box">
            {user.isAuthenticated && user.role === 'RECRUITER' && (
                <>
                    <button className="vacancy-button add-vacancy-button" onClick={handleOpenModal}>
                        <i className="fas fa-plus"></i> Додати вакансію
                    </button>
                    <button
                        className="vacancy-button delete-vacancy-button"
                        onClick={handleDeleteVacancy}
                        disabled={selectedVacancyId === null}
                    >
                        <i className="fas fa-trash"></i> Видалити вакансію
                    </button>
                    <button
                        className="vacancy-button update-vacancy-button"
                        onClick={openUpdateModal}
                        disabled={selectedVacancyId === null}
                    >
                        <i className="fas fa-edit"></i> Оновити вакансію
                    </button>
                </>
            )}
            <div className="vacancies-column">
                <h2 className="vacancies-title">
                    Мої вакансії <i className="fas fa-fire"></i>
                </h2>
                <div className="vacancies-list">
                    {vacancies.length > 0 ? (
                        vacancies.map(vacancy => (
                            <div
                                key={vacancy.vacancy_id ?? Math.random()}
                                className={`vacancy-tile ${selectedVacancyId === vacancy.vacancy_id ? 'selected' : ''}`}
                                onClick={() => {
                                    if (vacancy.vacancy_id !== undefined) {
                                        setSelectedVacancyId(vacancy.vacancy_id);
                                    }
                                }}
                            >
                                <img src={staticImage} alt="Vacancy" className="vacancy-image"/>
                                <div className="vacancy-details">
                                    <h3>{vacancy.position}</h3>
                                    <p>{companyName}</p>
                                    <div className="vacancy-salary">
                                        Salary: ${vacancy.salary}
                                    </div>
                                    <p>Technologies: {vacancy.technology_stack ? vacancy.technology_stack.join(', ') : 'N/A'}</p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <Typography>No vacancies available</Typography>
                    )}
                </div>
            </div>
            <UpdateVacancyModal
                open={isUpdateModalOpen}
                onClose={closeUpdateModal}
                vacancy={vacancies.find(vacancy => vacancy.vacancy_id === selectedVacancyId) || null}
                onUpdate={handleUpdateVacancy}
            />
        </Box>
    );
};

export default MyVacancies;