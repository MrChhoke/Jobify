import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {Box, createTheme, CssBaseline, ThemeProvider} from '@mui/material';
import './App.css';
import Header from './components/Header/Header';
import Search from './components/Search/Search';
import Vacancies from './components/Vacancies/Vacancies';
import Profile from './components/Profile/Profile';
import CreateVacancyModal from './components/CreateVacancyModal/CreateVacancyModal';
import MyVacancies from './components/MyVacancies/MyVacancies';
import {getProfile} from './services/ProfileService';
import {isLoggedIn} from './services/AuthService';

const theme = createTheme();

function App() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [user, setUser] = useState({isAuthenticated: false, role: ''});

    useEffect(() => {
        if (isLoggedIn()) {
            getProfile().then((profile) => {
                setUser({
                    isAuthenticated: true,
                    role: profile.role
                });
            }).catch((error) => {
                setUser({isAuthenticated: false, role: ''});
            });
        }
    }, []);

    const handleOpenModal = () => {
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <Router>
                <div className="App">
                    <Header/>
                    <Routes>
                        <Route path="/profile" element={<Profile/>}/>
                        <Route path="/my-vacancies" element={<MyVacancies handleOpenModal={handleOpenModal} user={user}/>}/>
                        <Route path="/vacancies" element={
                            <>
                                <Box display="flex" alignItems="center">
                                    <Search/>
                                </Box>
                                <Vacancies/>
                            </>
                        }/>
                        <Route path="/" element={
                            <>
                                <Box display="flex" alignItems="center">
                                    <Search/>
                                </Box>
                                <Vacancies/>
                            </>
                        }/>
                        {/* Add other routes here */}
                    </Routes>
                    <CreateVacancyModal open={isModalOpen} onClose={handleCloseModal}/>
                </div>
            </Router>
        </ThemeProvider>
    );
}

export default App;