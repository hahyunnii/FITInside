import React from 'react';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅을 사용하여 페이지 이동

const AdminPage = () => {
    const navigate = useNavigate();

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div style={{ padding: '20px'}}>
            <h2 className="mb-4">관리자 페이지</h2>
            <div className="mt-5 d-flex flex-column justify-content-center align-items-center">
                <button onClick={() => handleNavigation('/admin/coupons')} className="btn btn-light text-dark mb-4" style={{border: '1px solid #ced4da', width: '30%'}}>
                    쿠폰 관리
                </button>
                <button onClick={() => handleNavigation('/admin/categories')} className="btn btn-light text-dark mb-4" style={{border: '1px solid #ced4da', width: '30%'}}>
                    카테고리 관리
                </button>
                <button onClick={() => handleNavigation('/admin/adverts')} className="btn btn-light text-dark mb-4" style={{border: '1px solid #ced4da', width: '30%'}}>
                    메인 화면 광고 관리
                </button>
                <button onClick={() => handleNavigation('/admin/orders')} className="btn btn-light text-dark mb-4" style={{border: '1px solid #ced4da', width: '30%'}}>
                    주문 관리
                </button>
                <button onClick={() => handleNavigation('/admin/products')} className="btn btn-light text-dark mb-4" style={{border: '1px solid #ced4da', width: '30%'}}>
                    상품 관리
                </button>
            </div>
        </div>
    );
};

export default AdminPage;
