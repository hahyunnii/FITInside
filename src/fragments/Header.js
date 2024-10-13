// Header.js
import React, {useEffect, useState} from 'react';
import './header.css';
import {useNavigate} from "react-router-dom";

const Header = () => {
    const [cartCount, setCartCount] = useState(0);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate(); // useNavigate 훅 사용

    // 장바구니 개수 확인 및 업데이트 함수
    const updateCartCount = () => {
        const storedCart = JSON.parse(localStorage.getItem('localCart')) || [];
        setCartCount(storedCart.length);
    };

    // 로그인 상태 확인
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            setIsLoggedIn(true);
        } else {
            setIsLoggedIn(false);
        }
        updateCartCount(); // 초기 카운트 업데이트
        window.addEventListener('storage', updateCartCount); // storage 이벤트 리스너 추가
        return () => {
            window.removeEventListener('storage', updateCartCount); // 언마운트 시 리스너 제거
        };
    }, [navigate]);

    // 로그아웃 함수
    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('dbCart');
        localStorage.removeItem('localCart');
        setIsLoggedIn(false);
        updateCartCount(); // 로그아웃 시 카운트 업데이트

        navigate('/'); // 로그인 페이지로 리다이렉트
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container px-4 px-lg-5">
                <a className="navbar-brand" href="/"><img src="/img/fitinside_logo_transparent.png" alt="로고"/> </a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
                        <li className="nav-item"><a className="nav-link active" aria-current="page" href="/">Home</a>
                        </li>

                        {/*<li className="nav-item"><a className="nav-link" href="#!">About</a></li>*/}

                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" id="navbarDropdown" href="/" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">여성</a>
                            <ul className="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li><a className="dropdown-item" href="/">카테고리1</a></li>
                                <li><a className="dropdown-item" href="/">카테고리2</a></li>
                                <li><a className="dropdown-item" href="/">카테고리3</a></li>
                                {/*<li>*/}
                                {/*    <hr className="dropdown-divider"/>*/}
                                {/*</li>*/}
                            </ul>
                        </li>

                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" id="navbarDropdown" href="/" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">남성</a>
                            <ul className="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li><a className="dropdown-item" href="/">카테고리1</a></li>
                                <li><a className="dropdown-item" href="/">카테고리2</a></li>
                                <li><a className="dropdown-item" href="/">카테고리3</a></li>
                            </ul>
                        </li>
                    </ul>
                    <div className="d-flex m-3">
                        {isLoggedIn ? (
                            <div>
                                <a className="btn btn-outline-dark me-3" href="/admin">ADMIN</a>
                                <a className="btn btn-outline-dark" href="/me">
                                    MY
                                </a>
                            </div>
                        ) : (<div></div>)}
                    </div>
                    <div className="d-flex">
                        <a className="btn btn-outline-dark" href="/cart"><i className="bi-cart-fill me-1"></i> Cart
                            <span
                                className="badge bg-dark text-white ms-1 rounded-pill">{cartCount}</span> {/* 장바구니 품목 개수 표시 */}
                        </a>
                    </div>
                    <div className="d-flex m-3">
                        {isLoggedIn ? (
                            <button className="btn btn-outline-dark" onClick={handleLogout}>
                                <i className="bi-cart-fill me-1"></i> 로그아웃
                            </button>
                        ) : (
                            <a className="btn btn-outline-dark" href="/login">
                                <i className="bi-cart-fill me-1"></i> 로그인
                            </a>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Header;
