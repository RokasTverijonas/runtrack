import { createContext, useContext, useEffect, useState } from 'react';
import { login as loginApi, register as registerApi, getCurrentUser } from '../api/auth';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(() => Boolean(localStorage.getItem('token')));

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            return;
        }
        getCurrentUser()
        .then(setUser)
        .catch(() => localStorage.removeItem('token'))
        .finally(() => setIsLoading(false));
    }, []);

    const login = async (email, password) => {
        const { token } = await loginApi(email, password);
        localStorage.setItem('token', token);

        try {
            const userData = await getCurrentUser();
            setUser(userData);
        } catch (error) {
            localStorage.removeItem('token');
            throw error;
        }
    };

    const register = async (name, email, password) => {
        const { token } = await registerApi(name, email, password);
        localStorage.setItem('token', token);

        try {
            const userData = await getCurrentUser();
            setUser(userData);
        } catch (error) {
            localStorage.removeItem('token');
            throw error;
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, isLoading, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

// This hook belongs with its provider; it is safe to keep both in this module.
// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => useContext(AuthContext)
