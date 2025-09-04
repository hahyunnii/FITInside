// Footer.js
import React from 'react';
import './footer.css';

const Footer = () => {
    return (
        <footer className="py-4 bg-dark">
            <div className="container">
                <p className="mb-1 text-white">해외배송 상품, 구매대행 상품, 항공권/호텔예약 및 여행편의 서비스의 경우, FITinside는 통신판매중개자이며, 통신판매의 당사자가 아닙니다.</p>
                <p className="mb-1 text-white">FITinside는 해당 해외배송 상품 또는 구매대행 상품, 항공권/호텔예약 및 여행편의 서비스의 거래 정보 및 거래 등에 대하여 책임을 지지 않습니다.</p>
                <p className="mb-1 text-white">COPYRIGHT &copy; Cloud4 Team2 FITinside</p>
            </div>
        </footer>
    );
};

export default Footer;
