import React, {useEffect, useState} from 'react';
import {Box, Button, Modal, TextField} from '@mui/material';
import {Vacancy} from '../../models/Vacancy';

interface UpdateVacancyModalProps {
    open: boolean;
    onClose: () => void;
    vacancy: Vacancy | null;
    onUpdate: (updatedVacancy: Vacancy) => void;
}

const UpdateVacancyModal: React.FC<UpdateVacancyModalProps> = ({open, onClose, vacancy, onUpdate}) => {
    const [formData, setFormData] = useState<Vacancy | null>(null);

    useEffect(() => {
        if (vacancy) {
            setFormData(vacancy);
        }
    }, [vacancy]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (formData) {
            setFormData({
                ...formData,
                [e.target.name]: e.target.value,
            });
        }
    };

    const handleSubmit = () => {
        if (formData) {
            onUpdate(formData);
            onClose();
        }
    };

    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={{padding: 4, backgroundColor: 'white', margin: 'auto', marginTop: '10%', width: '50%'}}>
                <h2>Update Vacancy</h2>
                <TextField
                    label="Position"
                    name="position"
                    value={formData?.position || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    label="Salary"
                    name="salary"
                    value={formData?.salary || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    label="Technology Stack"
                    name="technology_stack"
                    value={formData?.technology_stack.join(', ') || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <Button variant="contained" color="primary" onClick={handleSubmit}>
                    Update
                </Button>
            </Box>
        </Modal>
    );
};

export default UpdateVacancyModal;