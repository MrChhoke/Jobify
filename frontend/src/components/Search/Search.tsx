import React, {useState} from 'react';
import './Search.css';
import '@fortawesome/fontawesome-free/css/all.min.css';

interface SearchProps {
    onSearch: (term: string) => void;
}

const Search: React.FC<SearchProps> = ({onSearch}) => {
    const [searchTerm, setSearchTerm] = useState('');

    const handleSearch = () => {
        onSearch(searchTerm);
    };

    return (
        <div className="search-container">
            <div className="input-wrapper">
                <i className="fas fa-search search-icon"></i>
                <input
                    type="text"
                    className="search-input"
                    placeholder="Назва вакансії..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <button className="search-button" onClick={handleSearch}>Пошук</button>
            </div>
        </div>
    );
};

export default Search;