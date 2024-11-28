import React, {useEffect, useState} from 'react';
import {Box, Typography} from '@mui/material';
import {getAllRecruiterApplications, getAllUserApplications} from '../../services/VacancyService';
import {getProfile} from '../../services/ProfileService';
import './MyApplications.css';

interface Vacancy {
    vacancy_id: number;
    position: string;
    salary: number;
}

interface Candidate {
    first_name: string;
    last_name: string;
}

interface Application {
    vacancy: Vacancy;
    candidate: Candidate;
}

const MyApplications: React.FC = () => {
    const [applications, setApplications] = useState<Application[]>([]);
    const [role, setRole] = useState<string | null>(null);

    useEffect(() => {
        const fetchProfileAndApplications = async () => {
            try {
                const profile = await getProfile();
                const userRole = profile.role;
                setRole(userRole);

                let response;
                if (userRole === 'RECRUITER') {
                    response = await getAllRecruiterApplications();
                } else if (userRole === 'USER') {
                    response = await getAllUserApplications();
                }
                setApplications(response);
            } catch (error) {
                console.error('Error fetching profile or applications:', error);
            }
        };

        fetchProfileAndApplications();
    }, []);

    return (
        <Box className="my-applications-container">
            <h2 className="vacancies-title">
                Мої заявки <i className="fas fa-clipboard-list"></i>
            </h2>
            <div className="vacancies-list">
                {applications.length > 0 ? (
                    applications.map((application, index) => (
                        <div key={index} className="application-tile">
                            <i className="fas fa-file-alt" style={{marginRight: '10px'}}></i>
                            <div className="application-details">
                                <Typography variant="h5">{application.vacancy.position}</Typography>
                                <div className="vacancy-salary">
                                    <Typography>Salary: ${application.vacancy.salary}</Typography>
                                </div>
                                <Typography>Candidate: {application.candidate.first_name} {application.candidate.last_name}</Typography>
                            </div>
                        </div>
                    ))
                ) : (
                    <Typography>No applications available</Typography>
                )}
            </div>
        </Box>
    );
};

export default MyApplications;