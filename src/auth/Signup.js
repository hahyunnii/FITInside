// src/Signup.js
import React, { useState } from 'react';
import axios from 'axios';

const Signup = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [userName, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 비밀번호 확인 로직
        if (password !== confirmPassword) {
            setError('비밀번호가 일치하지 않습니다.');
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/api/auth', {
                email,
                password,
                userName,
                phone
            });

            if (response.status === 200) {
                // 회원가입 성공 시 처리
                setSuccess('회원가입에 성공했습니다! 로그인 페이지로 이동하세요.');
                setError('');
            }
        } catch (error) {
            setError('회원가입 실패: 이미 존재하는 이메일이거나 서버 오류가 발생했습니다.');
            setSuccess('');
        }
    };

    return (
        <div>
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>이메일:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>비밀번호:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>비밀번호 확인:</label>
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>이름:</label>
                    <input
                        type="text"
                        value={userName}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>전화번호:</label>
                    <input
                        type="phone"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        required
                    />
                </div>
                {error && <p style={{color: 'red'}}>{error}</p>}
                {success && <p style={{color: 'green'}}>{success}</p>}
                <button type="submit">회원가입</button>
            </form>
        </div>
    );
};

export default Signup;
