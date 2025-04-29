// frontend/src/components/LoginModal/LoginModal.tsx
import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {setUser} from '../../redux/slices/userSlice';
import {login} from '../../services/AuthService';
import {User} from '../../models/User';
import './LoginModal.css';

interface LoginModalProps {
    open: boolean;
    onClose: () => void;
}

const LoginModal: React.FC<LoginModalProps> = ({open, onClose}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const dispatch = useDispatch();

    const handleLogin = async () => {
        try {
            const user: User = await login(username, password);
            dispatch(setUser(user));
            onClose();
            window.location.reload(); // Refresh the page after login
        } catch (err) {
            setError('Login failed. Please try again.');
        }
    };

    if (!open) return null;

    return (
        <div className="login-modal-overlay">
            <div className="login-modal">
                <button className="close-button" onClick={onClose}>&times;</button>
                <h2>Вхід</h2>
                <div className="login-modal-box">
                    <input
                        type="text"
                        placeholder="Логін"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        data-testid="username-input"
                    />
                    <input
                        type="password"
                        placeholder="Пароль"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        data-testid="password-input"
                    />
                    {error && <p style={{color: 'red'}} data-testid="error-message">{error}</p>}
                </div>
                <div className="login-modal-actions">
                    <button className="login-button" onClick={handleLogin} data-testid="login-button">Увійти</button>
                </div>
                <a href="#" onClick={onClose} className="forgot-password" data-testid="forgot-password-link">Забули пароль?</a>
            </div>
        </div>
    );
};

export default LoginModal;
