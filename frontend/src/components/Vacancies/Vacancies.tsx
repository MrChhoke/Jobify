import React, {useEffect, useState} from 'react';
import {Box, Chip, TextField, Typography} from '@mui/material';
import {applyVacancyById, exportVacanciesToExcel, getAllVacancies} from '../../services/VacancyService';
import {Vacancy} from '../../models/Vacancy';
import './Vacancies.css';
import staticImage from "../../assets/jobIcon.png";

interface VacanciesProps {
    handleOpenModal: () => void;
    user: { isAuthenticated: boolean; role: string };
}

const Vacancies: React.FC<VacanciesProps> = ({user}) => {
    const [vacancies, setVacancies] = useState<Vacancy[]>([]);
    const [selectedVacancyId, setSelectedVacancyId] = useState<number | null>(null);
    const [selectedTags, setSelectedTags] = useState<string[]>([]);
    const [allTags, setAllTags] = useState<string[]>([]);
    const [minSalary, setMinSalary] = useState<number | null>(null);
    const [maxSalary, setMaxSalary] = useState<number | null>(null);

    useEffect(() => {
        const fetchVacancies = async () => {
            try {
                const response = await getAllVacancies(0, 15);
                if (response && Array.isArray(response.content)) {
                    setVacancies(response.content);
                    const tags = new Set<string>();
                    response.content.forEach(vacancy => {
                        vacancy.technology_stack?.forEach(tag => tags.add(tag));
                    });
                    setAllTags(Array.from(tags));
                } else {
                    console.error('API response does not contain a valid content array:', response);
                }
            } catch (error) {
                console.error('Error fetching vacancies:', error);
            }
        };

        fetchVacancies();
    }, []);

    const handleTagClick = (tag: string) => {
        setSelectedTags(prevTags =>
            prevTags.includes(tag) ? prevTags.filter(t => t !== tag) : [...prevTags, tag]
        );
    };

    const filteredVacancies = vacancies.filter(vacancy =>
        selectedTags.every(tag => vacancy.technology_stack?.includes(tag)) &&
        (minSalary === null || parseInt(vacancy.salary) >= minSalary) &&
        (maxSalary === null || parseInt(vacancy.salary) <= maxSalary)
    );

    const handleExportToExcel = async () => {
        try {
            await exportVacanciesToExcel();
            console.log('Export to Excel successful');
        } catch (error) {
            console.error('Error exporting to Excel:', error);
        }
    };

    const handleApplyVacancy = async (id: number) => {
        try {
            await applyVacancyById(id);
            console.log(`Applied to vacancy ID: ${id}`);
        } catch (error) {
            console.error(`Error applying to vacancy ID: ${id}`, error);
        }
    };

    return (
        <Box sx={{marginLeft: '20px'}} className="vacancies-container">
            <div className="filter-column">
                <h2 className="filter-title">Фільтри</h2>
                <div className="tags-container">
                    {allTags.map(tag => (
                        <Chip
                            key={tag}
                            label={tag}
                            clickable
                            color={selectedTags.includes(tag) ? 'primary' : 'default'}
                            onClick={() => handleTagClick(tag)}
                            sx={{margin: '5px'}}
                        />
                    ))}
                </div>
                <div className="salary-filter-container">
                    <TextField
                        className="salary-filter"
                        label="Мін зарплата"
                        type="number"
                        value={minSalary ?? ''}
                        onChange={(e) => setMinSalary(e.target.value ? parseInt(e.target.value) : null)}
                        margin="normal"
                    />
                    <span>-</span>
                    <TextField
                        className="salary-filter"
                        label="Макс зарплата"
                        type="number"
                        value={maxSalary ?? ''}
                        onChange={(e) => setMaxSalary(e.target.value ? parseInt(e.target.value) : null)}
                        margin="normal"
                    />
                </div>
            </div>
            <div className="vacancies-column">
                <h2 className="vacancies-title">
                    Вакансії <i className="fas fa-fire"></i>
                </h2>
                <div className="vacancies-list">
                    {filteredVacancies.length > 0 ? (
                        filteredVacancies.map(vacancy => (
                            <div
                                key={vacancy.vacancy_id ?? Math.random()}
                                className={`vacancy-tile ${selectedVacancyId === vacancy.vacancy_id ? 'selected' : ''}`}
                                onClick={() => {
                                    console.log('Vacancy:', vacancy);
                                    if (vacancy.vacancy_id !== undefined) {
                                        console.log(`Selected vacancy ID: ${vacancy.vacancy_id}`);
                                        setSelectedVacancyId(vacancy.vacancy_id);
                                    }
                                }}
                            >
                                <img src={staticImage} alt="Vacancy" className="vacancy-image"/>
                                <div className="vacancy-details">
                                    <h3>{vacancy.position}</h3>
                                    <div className="vacancy-salary">
                                        Salary: ${vacancy.salary}
                                    </div>
                                    <p>Technologies: {vacancy.technology_stack ? vacancy.technology_stack.join(', ') : 'N/A'}</p>
                                    <button onClick={() => handleApplyVacancy(vacancy.vacancy_id ?? 0)}>
                                        Відгукнутися
                                    </button>
                                </div>
                            </div>
                        ))
                    ) : (
                        <Typography>No vacancies available</Typography>
                    )}
                </div>
            </div>
            <div className="export-column">
                {user.role === 'ADMIN' && (
                    <div className="export-excel" onClick={handleExportToExcel}>
                        Експортувати в Excel <i className="fas fa-file-excel"></i>
                    </div>
                )}
            </div>
        </Box>
    );
};

export default Vacancies;