import React, {useEffect, useState} from 'react';
import {Box, Modal, TextField} from '@mui/material';
import {Vacancy} from '../../models/Vacancy';
import {getProfile} from '../../services/ProfileService'; // Import the function to fetch recruiter_id
import './UpdateVacancyModal.css'; // Import the common CSS file

interface UpdateVacancyModalProps {
    open: boolean;
    onClose: () => void;
    vacancy: Vacancy | null;
    onUpdate: (updatedVacancy: Vacancy) => void;
}

const UpdateVacancyModal: React.FC<UpdateVacancyModalProps> = ({open, onClose, vacancy, onUpdate}) => {
    const [formData, setFormData] = useState<Partial<Vacancy>>({});
    const [recruiterId, setRecruiterId] = useState<string | null>(null);

    useEffect(() => {
        if (vacancy) {
            setFormData({
                position: vacancy.position,
                salary: vacancy.salary,
                technology_stack: vacancy.technology_stack,
            });
        }
    }, [vacancy]);

    useEffect(() => {
        const fetchRecruiterId = async () => {
            try {
                const profile = await getProfile();
                setRecruiterId(profile.user_id.toString());
            } catch (error) {
                console.error('Error fetching recruiter ID:', error);
            }
        };

        fetchRecruiterId();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleSubmit = () => {
        if (formData && recruiterId) {
            onUpdate({...formData, recruiter_id: Number(recruiterId)} as Vacancy);
            onClose();
        }
    };

    return (
        <Modal open={open} onClose={onClose}>
            <Box className="vacancy-modal">
                <button className="close-button" onClick={onClose}>&times;</button>
                <h2>Оновити вакансію</h2>
                <TextField
                    label="Позиція"
                    name="position"
                    value={formData.position || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    label="Зарплата"
                    name="salary"
                    value={formData.salary || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    label="Технологічний стек"
                    name="technology_stack"
                    value={formData.technology_stack?.join(', ') || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <button color="primary" onClick={handleSubmit} className="update-vacancy-button-modal">
                    Оновити
                </button>
            </Box>
        </Modal>
    );
};

export default UpdateVacancyModal;