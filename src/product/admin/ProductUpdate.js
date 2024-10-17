import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const ProductUpdate = () => {
    const { id } = useParams(); // URL에서 상품 ID를 가져옴
    const navigate = useNavigate();

    // 상품 정보 상태
    const [product, setProduct] = useState({
        categoryId: '',
        productName: '',
        price: '',
        info: '',
        stock: '',
        manufacturer: '',
        productImgUrls: [],
    });
    const [newImages, setNewImages] = useState([]); // 새로운 이미지 파일
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true); // 로딩 상태

    // 상품 정보 불러오기
    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/products/${id}`);
                // console.log('불러온 상품 데이터:', response.data); // 데이터를 콘솔에 출력
                setProduct(response.data); // 기존 상품 정보 설정
                setLoading(false);
            } catch (err) {
                console.error('상품 정보를 불러오는 중 오류 발생:', err);
                setError('상품 정보를 불러오는 중 오류가 발생했습니다.');
                setLoading(false);
            }
        };
        fetchProduct();
    }, [id]);

    // 입력 값 변경 처리
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setProduct((prevProduct) => ({
            ...prevProduct,
            [name]: value,
        }));
    };

    // 이미지 파일 선택 처리
    const handleFileChange = (e) => {
        setNewImages(e.target.files);
    };

    // 상품 수정 요청 처리
    const handleSubmit = async (e) => {
        e.preventDefault();

        // 상품 수정 데이터 준비
        const formData = new FormData();
        formData.append('categoryId', product.categoryId);
        formData.append('productName', product.productName);
        formData.append('price', product.price);
        formData.append('info', product.info);
        formData.append('stock', product.stock);
        formData.append('manufacturer', product.manufacturer);

        // 새로운 이미지 파일 추가
        for (let i = 0; i < newImages.length; i++) {
            formData.append('productImgUrls', newImages[i]);
        }

        try {
            await axios.put(`http://localhost:8080/api/admin/products/${id}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            navigate('/admin/products'); // 수정 완료 후 상품 목록 페이지로 이동
        } catch (err) {
            console.error('상품 수정 중 오류 발생:', err);
            setError('상품 수정에 실패했습니다.');
        }
    };

    if (loading) {
        return <p>로딩 중...</p>;
    }

    if (error) {
        return <p className="text-danger">{error}</p>;
    }

    return (
        <div className="container mt-5">
            <h2>상품 수정</h2>
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <div className="form-group">
                    <label>카테고리 ID</label>
                    <input
                        type="number"
                        className="form-control"
                        name="categoryId"
                        value={product.categoryId}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>상품명</label>
                    <input
                        type="text"
                        className="form-control"
                        name="productName"
                        value={product.productName}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>가격</label>
                    <input
                        type="number"
                        className="form-control"
                        name="price"
                        value={product.price}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>상품 설명</label>
                    <textarea
                        className="form-control"
                        name="info"
                        value={product.info}
                        onChange={handleInputChange}
                        required
                    ></textarea>
                </div>
                <div className="form-group">
                    <label>재고</label>
                    <input
                        type="number"
                        className="form-control"
                        name="stock"
                        value={product.stock}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>제조사</label>
                    <input
                        type="text"
                        className="form-control"
                        name="manufacturer"
                        value={product.manufacturer}
                        onChange={handleInputChange}
                    />
                </div>
                <div className="form-group">
                    <label>이미지 추가</label>
                    <input
                        type="file"
                        className="form-control"
                        name="productImgUrls"
                        onChange={handleFileChange}
                        multiple
                    />
                </div>
                <button type="submit" className="btn btn-primary mt-3">
                    수정하기
                </button>
            </form>
        </div>
    );
};

export default ProductUpdate;
