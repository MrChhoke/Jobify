import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {Link, useNavigate} from 'react-router-dom';
import {AppBar, Button, Menu, MenuItem, Toolbar, Typography} from '@mui/material';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faCaretDown, faUser} from '@fortawesome/free-solid-svg-icons';
import {isLoggedIn} from '../../services/AuthService';
import {getProfile} from '../../services/ProfileService';
import {clearUser, setUser} from '../../redux/slices/userSlice';
import jobifyLogo from '../../assets/jobifyLogo.png';
import LoginModal from '../LoginModal/LoginModal';
import RegisterModal from '../RegisterModal/RegisterModal';
import './Header.css';
import {RootState} from '../../redux/store';

const Header: React.FC = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const [isLoginModalOpen, setLoginModalOpen] = useState(false);
    const [isRegisterModalOpen, setRegisterModalOpen] = useState(false);
    const user = useSelector((state: RootState) => state.user.user);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        if (isLoggedIn() && !user) {
            getProfile().then((profile: any) => {
                dispatch(setUser(profile));
            }).catch((error: any) => {
                // Handle error
            });
        }
    }, [dispatch, user]);

    const handleMenuOpen = (event: React.MouseEvent<SVGSVGElement, MouseEvent>) => {
        setAnchorEl(event.currentTarget as unknown as HTMLElement);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    const handleProfileClick = () => {
        navigate('/profile');
        handleMenuClose();
    };

    const handleMyVacanciesClick = () => {
        navigate('/my-vacancies');
        handleMenuClose();
    };

    const handleMyApplicationsClick = () => {
        navigate('/my-applications');
        handleMenuClose();
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        dispatch(clearUser());
        handleMenuClose();
        window.location.reload();
    };

    const handleLoginModalOpen = () => {
        setLoginModalOpen(true);
    };

    const handleRegisterModalOpen = () => {
        setRegisterModalOpen(true);
    };

    const handleLoginModalClose = () => {
        setLoginModalOpen(false);
    };

    const handleRegisterModalClose = () => {
        setRegisterModalOpen(false);
    };

    return (
        <AppBar position="static">
            <div className="header-top">
                <Typography className="small-text">
                    Слава Україні! Героям Слава!
                </Typography>
            </div>
            <Toolbar className="custom-toolbar">
                <Link to="/vacancies">
                    <img src={jobifyLogo} alt="Jobify Logo" className="logo"/>
                </Link>
                <Typography variant="h6" className="title">
                    <Link to="/vacancies" className="header-link">Вакансії</Link>
                </Typography>
                <Typography variant="h6" className="title">
                    <Link to="/news" className="header-link">Новини</Link>
                </Typography>
                {user ? (
                    <div className="user-info">
                        <FontAwesomeIcon icon={faUser} className="user-avatar"/>
                        <span className="user-name">{user.first_name} {user.last_name}</span>
                        <FontAwesomeIcon icon={faCaretDown} className="dropdown-icon" onClick={handleMenuOpen}/>
                        <Menu
                            anchorEl={anchorEl}
                            open={Boolean(anchorEl)}
                            onClose={handleMenuClose}
                            classes={{paper: 'custom-menu-paper'}}
                        >
                            <MenuItem onClick={handleProfileClick}>Профіль</MenuItem>
                            {user.role === 'RECRUITER' && (
                                <MenuItem onClick={handleMyVacanciesClick}>Мої вакансії</MenuItem>
                            )}
                            {user.role === 'RECRUITER' && (
                                <MenuItem onClick={handleMyApplicationsClick}>Мої заявки</MenuItem>
                            )}
                            <MenuItem onClick={handleLogout}>Вийти</MenuItem>
                        </Menu>
                    </div>
                ) : (
                    <div className="toggle-container">
                        <Button className="toggle-button" onClick={handleLoginModalOpen}>Вхід</Button>
                        <Button className="toggle-button" onClick={handleRegisterModalOpen}>Реєстрація</Button>
                    </div>
                )}
            </Toolbar>
            <LoginModal open={isLoginModalOpen} onClose={handleLoginModalClose}/>
            <RegisterModal open={isRegisterModalOpen} onClose={handleRegisterModalClose}/>
        </AppBar>
    );
};

export default Header;