// frontend/src/components/RegisterModal/RegisterModal.tsx
import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {setUser} from '../../redux/slices/userSlice';
import {register} from '../../services/AuthService';
import {User} from '../../models/User';
import './RegisterModal.css';

interface RegisterModalProps {
    open: boolean;
    onClose: () => void;
}

const RegisterModal: React.FC<RegisterModalProps> = ({open, onClose}) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [error, setError] = useState('');
    const dispatch = useDispatch();

    const handleRegister = async () => {
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        try {
            const user: User = await register(login, password, firstName, lastName);
            dispatch(setUser(user));
            onClose();
            window.location.reload();
        } catch (err) {
            setError('Registration failed. Please try again.');
        }
    };

    if (!open) return null;

    return (
        <div className="register-modal-overlay">
            <div className="register-modal">
                <button className="close-button" onClick={onClose}>&times;</button>
                <h2>Реєстрація</h2>
                <div className="register-modal-box">
                    <input
                        type="text"
                        placeholder="Логін"
                        value={login}
                        onChange={(e) => setLogin(e.target.value)}
                    />
                    <input
                        type="password"
                        placeholder="Пароль"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <input
                        type="password"
                        placeholder="Підтвердіть пароль"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />
                    <input
                        type="text"
                        placeholder="Ім'я"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                    />
                    <input
                        type="text"
                        placeholder="Прізвище"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                    />
                    {error && <p style={{color: 'red'}}>{error}</p>}
                </div>
                <div className="register-modal-actions">
                    <button className="register-button" onClick={handleRegister}>Зареєструватися</button>
                </div>
                <a href="#" onClick={onClose} className="forgot-password">Забули пароль?</a>
            </div>
        </div>
    );
};

export default RegisterModal;