import React from 'react';
import './Vacancies.css';
import staticImage from '../../assets/jobIcon.png';

const Vacancies: React.FC = () => {
    const vacancies = [
        {
            id: 1,
            title: 'Automation QA with JS',
            company: 'This is an extraordinary opportunity to join a small collaborative team, and work with outstanding peers in a relaxed atmosphere....',
            salary: '$2300'
        },
        {
            id: 2,
            title: 'Data Scientist',
            company: 'This is an extraordinary opportunity to join a small collaborative team, and work with outstanding peers in a relaxed atmosphere....',
            salary: '$1400'
        },
        {
            id: 3,
            title: 'Product Manager',
            company: 'This is an extraordinary opportunity to join a small collaborative team, and work with outstanding peers in a relaxed atmosphere....',
            salary: '$1800'
        },
        {
            id: 4,
            title: 'Frontend Developer',
            company: 'Join our dynamic team to create stunning user interfaces and improve user experience....',
            salary: '$2700'
        },
        {
            id: 5,
            title: 'Backend Developer',
            company: 'We are looking for a skilled backend developer to work on our server-side applications....',
            salary: '$3500'
        },
        {
            id: 6,
            title: 'DevOps Engineer',
            company: 'Help us streamline our development processes and ensure smooth deployment of our applications....',
            salary: '$3600'
        },
        {
            id: 7,
            title: 'UI/UX Designer',
            company: 'Design intuitive and engaging user interfaces for our web and mobile applications....',
            salary: '$2200'
        },
    ];

    return (
        <div className="vacancies-container">
            <div className="filter-column">
                <h2 className="filter-title">Фільтри</h2>
            </div>
            <div className="vacancies-column">
                <h2 className="vacancies-title">
                    Гарячі вакансії <i className="fas fa-fire"></i>
                </h2>
                <div className="vacancies-list">
                    {vacancies.map(vacancy => (
                        <div key={vacancy.id} className="vacancy-tile">
                            <img src={staticImage} alt="Vacancy" className="vacancy-image"/>
                            <div className="vacancy-details">
                                <h3>{vacancy.title}</h3>
                                <p>{vacancy.company}</p>
                            </div>
                            <div className="vacancy-salary">
                                {vacancy.salary}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            <div className="export-column">
                <div className="export-excel">
                    Експортувати в Excel <i className="fas fa-file-excel"></i>
                </div>
            </div>
        </div>
    );
};

export default Vacancies;