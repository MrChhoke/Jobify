import React, {useEffect, useState} from 'react';
import {Box, Button, TextField, Typography} from '@mui/material';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faUser} from '@fortawesome/free-solid-svg-icons';
import {useDispatch, useSelector} from 'react-redux';
import {RootState} from '../../redux/store';
import {setUser} from '../../redux/slices/userSlice';
import {getProfile, updateProfile} from '../../services/ProfileService';
import './Profile.css';

const Profile: React.FC = () => {
    const [editMode, setEditMode] = useState(false);
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
        <Box className="profile-container">
            <Typography variant="h4" className="profile-title">
                Мій профіль
            </Typography>
            <Box className="profile-content">
                <Box className="profile-data">
                    <Box className="profile-field">
                        <Typography variant="h6" className="profile-field-name">
                            Логін:
                        </Typography>
                        <Typography variant="h6" className="profile-field-value">
                            {user.username}
                        </Typography>
                    </Box>
                    <Box className="profile-field">
                        <Typography variant="h6" className="profile-field-name">
                            Ім'я:
                        </Typography>
                        {editMode ? (
                            <TextField
                                variant="outlined"
                                value={user.first_name}
                                onChange={(e) => dispatch(setUser({...user, first_name: e.target.value}))}
                                className="profile-field-value"
                            />
                        ) : (
                            <Typography variant="h6" className="profile-field-value">
                                {user.first_name}
                            </Typography>
                        )}
                    </Box>
                    <Box className="profile-field">
                        <Typography variant="h6" className="profile-field-name">
                            Прізвище:
                        </Typography>
                        {editMode ? (
                            <TextField
                                variant="outlined"
                                value={user.last_name}
                                onChange={(e) => dispatch(setUser({...user, last_name: e.target.value}))}
                                className="profile-field-value"
                            />
                        ) : (
                            <Typography variant="h6" className="profile-field-value">
                                {user.last_name}
                            </Typography>
                        )}
                    </Box>
                    <Box className="profile-field">
                        <Typography variant="h6" className="profile-field-name">
                            Роль:
                        </Typography>
                        <Typography variant="h6" className="profile-field-value">
                            {getRoleDisplayName(user.role)}
                        </Typography>
                    </Box>
                </Box>
                <FontAwesomeIcon icon={faUser} className="profile-avatar"/>
            </Box>
            {editMode ? (
                <Button variant="contained" color="primary" onClick={handleSaveClick} className="profile-button">
                    Зберегти
                </Button>
            ) : (
                <Button variant="contained" color="primary" onClick={handleEditClick} className="profile-button">
                    Редагувати
                </Button>
            )}
        </Box>
    );
};

export default Profile;