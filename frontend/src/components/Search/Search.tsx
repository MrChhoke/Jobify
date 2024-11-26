import React from 'react';
import './Search.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

const Search: React.FC = () => {
    return (
        <div className="search-container">
            <div className="input-wrapper">
                <i className="fas fa-search search-icon"></i>
                <input type="text" className="search-input" placeholder="Назва вакансії..."/>
                <button className="search-button">Пошук</button>
            </div>
        </div>
    );
};

export default Search;