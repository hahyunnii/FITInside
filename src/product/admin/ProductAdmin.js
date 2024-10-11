import React from 'react';
import { useNavigate } from 'react-router-dom';

const ProductAdmin = () => {
    const navigate = useNavigate();

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2 className="mb-4">상품 관리</h2>
            <div className="mt-5 d-flex flex-column justify-content-center align-items-center">
                <button onClick={() => handleNavigation('/admin/products/create')} className="btn btn-light text-dark mb-4" style={{ border: '1px solid #ced4da', width: '30%' }}>
                    상품 등록
                </button>
                <button onClick={() => handleNavigation('/admin/products/update')} className="btn btn-light text-dark mb-4" style={{ border: '1px solid #ced4da', width: '30%' }}>
                    상품 수정
                </button>
                <button onClick={() => handleNavigation('/admin/products/delete')} className="btn btn-light text-dark mb-4" style={{ border: '1px solid #ced4da', width: '30%' }}>
                    상품 삭제
                </button>
            </div>
        </div>
    );
};

export default ProductAdmin;
