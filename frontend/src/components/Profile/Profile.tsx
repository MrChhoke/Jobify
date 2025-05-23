import React, {useEffect, useState} from 'react';
import {Box, Button, Modal, Snackbar, TextField, Typography} from '@mui/material';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faUser} from '@fortawesome/free-solid-svg-icons';
import {useDispatch, useSelector} from 'react-redux';
import {RootState} from '../../redux/store';
import {setUser} from '../../redux/slices/userSlice';
import {getProfile, updateProfile} from '../../services/ProfileService';
import {createRecruiter} from '../../services/VacancyService';
import './Profile.css';

const Profile: React.FC = () => {
    const [editMode, setEditMode] = useState(false);
    const [showRecruiterForm, setShowRecruiterForm] = useState(false);
    const [recruiterData, setRecruiterData] = useState({
        first_name: '',
        last_name: '',
        company: '',
        username: '',
        password: ''
    });
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const user = useSelector((state: RootState) => state.user.user);
    const dispatch = useDispatch();

    useEffect(() => {
        if (!user) {
            getProfile().then((profile) => {
                dispatch(setUser(profile));
            }).catch((error) => {
                console.error('Error fetching profile:', error);
            });
        }
    }, [dispatch, user]);

    const handleEditClick = () => {
        setEditMode(true);
    };

    const handleSaveClick = () => {
        if (user) {
            updateProfile(user).then((updatedUser) => {
                dispatch(setUser(updatedUser));
                setEditMode(false);
            }).catch((error) => {
                console.error('Error updating profile:', error);
            });
        }
    };

    const handleRecruiterFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setRecruiterData({
            ...recruiterData,
            [e.target.name]: e.target.value
        });
    };

    const handleCreateRecruiter = () => {
        createRecruiter(recruiterData).then(() => {
            setSnackbarOpen(true);
            setShowRecruiterForm(false);
        }).catch((error) => {
            console.error('Error creating recruiter:', error);
        });
    };

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    const getRoleDisplayName = (role: string) => {
        switch (role) {
            case 'USER':
                return 'Кандидат';
            case 'ADMIN':
                return 'Адміністратор';
            case 'RECRUITER':
                return 'Рекрутер';
            default:
                return role;
        }
    };

    if (!user) {
        return <Typography>Loading...</Typography>;
    }

    return (
        <Box className="profile-container" data-testid="profile-container">
            <Typography variant="h4" className="profile-title" data-testid="profile-title">
                Мій профіль
            </Typography>
            <Box className="profile-content" data-testid="profile-content">
                <Box className="profile-data" data-testid="profile-data">
                    <Box className="profile-field" data-testid="username-field">
                        <Typography variant="h6" className="profile-field-name">
                            Логін:
                        </Typography>
                        <Typography variant="h6" className="profile-field-value" data-testid="username-value">
                            {user.username}
                        </Typography>
                    </Box>
                    <Box className="profile-field" data-testid="firstname-field">
                        <Typography variant="h6" className="profile-field-name">
                            Ім'я:
                        </Typography>
                        {editMode ? (
                            <TextField
                                variant="outlined"
                                value={user.first_name}
                                onChange={(e) => dispatch(setUser({...user, first_name: e.target.value}))}
                                className="profile-field-value"
                                data-testid="firstname-input"
                            />
                        ) : (
                            <Typography variant="h6" className="profile-field-value" data-testid="firstname-value">
                                {user.first_name}
                            </Typography>
                        )}
                    </Box>
                    <Box className="profile-field" data-testid="lastname-field">
                        <Typography variant="h6" className="profile-field-name">
                            Прізвище:
                        </Typography>
                        {editMode ? (
                            <TextField
                                variant="outlined"
                                value={user.last_name}
                                onChange={(e) => dispatch(setUser({...user, last_name: e.target.value}))}
                                className="profile-field-value"
                                data-testid="lastname-input"
                            />
                        ) : (
                            <Typography variant="h6" className="profile-field-value" data-testid="lastname-value">
                                {user.last_name}
                            </Typography>
                        )}
                    </Box>
                    <Box className="profile-field" data-testid="role-field">
                        <Typography variant="h6" className="profile-field-name">
                            Роль:
                        </Typography>
                        <Typography variant="h6" className="profile-field-value" data-testid="role-value">
                            {getRoleDisplayName(user.role)}
                        </Typography>
                    </Box>
                </Box>
                <FontAwesomeIcon icon={faUser} className="profile-avatar" data-testid="profile-avatar"/>
            </Box>
            {editMode ? (
                <Button variant="contained" color="primary" onClick={handleSaveClick} className="profile-button" data-testid="save-button">
                    Зберегти
                </Button>
            ) : (
                <Button variant="contained" color="primary" onClick={handleEditClick} className="profile-button" data-testid="edit-button">
                    Редагувати
                </Button>
            )}
            {user.role === 'ADMIN' && (
                <Button 
                    variant="contained" 
                    color="secondary" 
                    onClick={() => setShowRecruiterForm(true)}
                    className="profile-button"
                    data-testid="add-recruiter-button"
                >
                    Додати рекрутера
                </Button>
            )}
            <Modal
                open={showRecruiterForm}
                onClose={() => setShowRecruiterForm(false)}
                aria-labelledby="recruiter-form-title"
                aria-describedby="recruiter-form-description"
            >
                <Box className="recruiter-form-modal" data-testid="recruiter-form-modal">
                    <Typography id="recruiter-form-title" variant="h6" component="h2" data-testid="recruiter-form-title">
                        Створити рекрутера
                    </Typography>
                    <TextField
                        label="Ім'я"
                        name="first_name"
                        value={recruiterData.first_name}
                        onChange={handleRecruiterFormChange}
                        fullWidth
                        data-testid="recruiter-firstname-input"
                    />
                    <TextField
                        label="Прізвище"
                        name="last_name"
                        value={recruiterData.last_name}
                        onChange={handleRecruiterFormChange}
                        fullWidth
                        data-testid="recruiter-lastname-input"
                    />
                    <TextField
                        label="Компанія"
                        name="company"
                        value={recruiterData.company}
                        onChange={handleRecruiterFormChange}
                        fullWidth
                        data-testid="recruiter-company-input"
                    />
                    <TextField
                        label="Логін"
                        name="username"
                        value={recruiterData.username}
                        onChange={handleRecruiterFormChange}
                        fullWidth
                        data-testid="recruiter-username-input"
                    />
                    <TextField
                        label="Пароль"
                        name="password"
                        type="password"
                        value={recruiterData.password}
                        onChange={handleRecruiterFormChange}
                        fullWidth
                        data-testid="recruiter-password-input"
                    />
                    <Button 
                        variant="contained" 
                        color="primary" 
                        onClick={handleCreateRecruiter} 
                        className="profile-button"
                        data-testid="create-recruiter-button"
                    >
                        Створити рекрутера
                    </Button>
                </Box>
            </Modal>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={6000}
                onClose={handleSnackbarClose}
                message="Recruiter created successfully"
                data-testid="success-snackbar"
            />
        </Box>
    );
};

export default Profile;
