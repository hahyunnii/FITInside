import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const ProductAdmin = () => {
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

    // 상품 목록 가져오기
    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/products?page=0&size=9');
            const data = await response.json();
            console.log('받아온 데이터:', data); // 데이터 구조 확인

            // 페이지네이션된 데이터의 content 배열만 사용
            if (data && Array.isArray(data.content)) {
                setProducts(data.content); // 상품 목록 저장
            } else {
                console.error('받아온 데이터가 올바른 형식이 아닙니다:', data);
                setProducts([]); // 데이터가 비정상인 경우 빈 배열 설정
            }
        } catch (error) {
            console.error('상품 목록을 불러오는 중 오류 발생:', error);
            setProducts([]); // 오류 발생 시 빈 배열로 설정
        }
    };


    // Navigate to create, update, delete routes
    const handleCreateProduct = () => {
        navigate('/admin/products/create');
    };

    const handleUpdateProduct = (id) => {
        navigate(`/admin/products/update/${id}`);
    };

    const handleDeleteProduct = (id) => {
        navigate(`/admin/products/delete/${id}`);
    };

    return (
        <div className="container mt-5">
            <h2>상품 관리</h2>
            <button onClick={handleCreateProduct} className="btn btn-primary mb-3">상품 등록</button>
            <table className="table table-bordered">
                <thead>
                <tr>
                    <th>수정</th>
                    <th>삭제</th>
                    <th>카테고리 아이디</th>
                    <th>상품 아이디</th>
                    <th>상품 이름</th>
                    <th>상품 이미지</th>
                    <th>상품 가격</th>
                    <th>상품 정보</th>
                    <th>상품 재고</th>
                    <th>제조사</th>
                    <th>생성일</th>
                    <th>수정일</th>
                </tr>
                </thead>
                <tbody>
                {products.length > 0 ? (
                    products.map((product) => (
                        <tr key={product.id}>
                            <td>
                                <button
                                    onClick={() => handleUpdateProduct(product.id)}
                                    className="btn btn-warning"
                                >
                                    ✏️
                                </button>
                            </td>
                            <td>
                                <button
                                    onClick={() => handleDeleteProduct(product.id)}
                                    className="btn btn-danger"
                                >
                                    ❌
                                </button>
                            </td>
                            <td>{product.categoryId}</td>
                            <td>{product.id}</td>
                            <td>{product.productName}</td>
                            <td>
                                {product.productImgUrls && product.productImgUrls.length > 0
                                    ? product.productImgUrls.map((url, index) => (
                                        <img key={index} src={url} alt={`Product ${product.id}`} width="50" />
                                    ))
                                    : '이미지 없음'}
                            </td>
                            <td>{product.price}</td>
                            <td>{product.info}</td>
                            <td>{product.stock}</td>
                            <td>{product.manufacturer}</td>
                            <td>{product.createdAt}</td>
                            <td>{product.updatedAt}</td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="12" className="text-center">상품이 없습니다.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default ProductAdmin;
