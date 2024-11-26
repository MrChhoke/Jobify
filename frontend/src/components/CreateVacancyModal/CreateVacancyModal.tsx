import React, {useEffect, useState} from 'react';
import {Box, Chip, IconButton, Modal, TextField} from '@mui/material';
import {Add as AddIcon} from '@mui/icons-material';
import {createVacancy} from '../../services/VacancyService';
import {getProfile} from '../../services/ProfileService';
import './CreateVacancyModal.css';

interface CreateVacancyModalProps {
    open: boolean;
    onClose: () => void;
}

const CreateVacancyModal: React.FC<CreateVacancyModalProps> = ({open, onClose}) => {
    const [position, setPosition] = useState('');
    const [company, setCompany] = useState('');
    const [salary, setSalary] = useState('');
    const [technology, setTechnology] = useState('');
    const [technologyStack, setTechnologyStack] = useState<string[]>([]);
    const [recruiterId, setRecruiterId] = useState<number | null>(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const profile = await getProfile();
                console.log('Profile data:', profile); // Debug log
                setRecruiterId(profile.user_id);
                console.log('Recruiter ID:', profile.user_id); // Debug log
            } catch (error) {
                console.error('Error fetching profile:', error);
            }
        };

        fetchProfile().catch(error => console.error('Error in fetchProfile:', error));
        console.log('useEffect triggered'); // Debug log
    }, []);

    const handleAddTechnology = () => {
        if (technology && !technologyStack.includes(technology)) {
            setTechnologyStack([...technologyStack, technology]);
            setTechnology('');
        }
    };

    const handleDeleteTechnology = (tech: string) => {
        setTechnologyStack(technologyStack.filter(t => t !== tech));
    };

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        if (recruiterId === null) {
            console.error('Recruiter ID is not available');
            return;
        }
        try {
            const newVacancy = {
                position,
                company,
                salary,
                technology_stack: technologyStack,
                recruiter_id: recruiterId,
            };
            console.log('New Vacancy:', newVacancy); // Debug log
            await createVacancy(newVacancy);
            onClose();
        } catch (error) {
            console.error('Error creating vacancy:', error);
        }
    };

    return (
        <Modal open={open} onClose={onClose}>
            <Box className="create-vacancy-modal-box">
                <button className="close-button" onClick={onClose}>&times;</button>
                <form onSubmit={handleSubmit}>
                    <h2>Додати вакансію</h2>
                    <TextField
                        label="Назва позиції"
                        value={position}
                        onChange={(e) => setPosition(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Назва компанії"
                        value={company}
                        onChange={(e) => setCompany(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Зарплата"
                        value={salary}
                        onChange={(e) => setSalary(e.target.value)}
                        fullWidth
                        margin="normal"
                    />
                    <TextField
                        label="Технологічний стек"
                        value={technology}
                        onChange={(e) => setTechnology(e.target.value)}
                        fullWidth
                        margin="normal"
                        InputProps={{
                            endAdornment: (
                                <IconButton onClick={handleAddTechnology}>
                                    <AddIcon/>
                                </IconButton>
                            ),
                        }}
                    />
                    <Box className="technology-stack">
                        {technologyStack.map((tech, index) => (
                            <Chip
                                key={index}
                                label={tech}
                                onDelete={() => handleDeleteTechnology(tech)}
                                className="technology-chip"
                            />
                        ))}
                    </Box>
                    <button type="submit" color="primary" className="create-vacancy-button">
                        Створити
                    </button>
                </form>
            </Box>
        </Modal>
    );
};

export default CreateVacancyModal;