import React, { useState, useEffect } from 'react';

// 상품 수정 및 삭제 컴포넌트
const ProductManagement = () => {
    const [products, setProducts] = useState([]);
    const [editProduct, setEditProduct] = useState(null);
    const [formData, setFormData] = useState({
        isDeleted: false,
        categoryId: '',
        productName: '',
        price: '',
        info: '',
        stock: '',
        manufacturer: '',
        productImgUrls: [], // 기존 이미지 URL 배열
    });
    const [selectedImages, setSelectedImages] = useState([]); // 새로 업로드할 이미지 파일들 저장

    // 상품 목록 가져오기
    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/products');
            const data = await response.json();
            setProducts(data);
        } catch (error) {
            console.error('상품 목록을 불러오는 중 오류 발생:', error);
        }
    };

    // 수정할 상품 선택
    const handleEditClick = (product) => {
        setEditProduct(product);
        setFormData({
            isDeleted: product.isDeleted,
            categoryId: product.categoryId,
            productName: product.productName,
            price: product.price,
            info: product.info || '',
            stock: product.stock,
            manufacturer: product.manufacturer || '',
            productImgUrls: product.productImgUrls, // 기존 이미지 URLs
        });
        setSelectedImages([]); // 새 이미지 초기화
    };

    // 이미지 선택 핸들러
    const handleImageChange = (e) => {
        setSelectedImages([...e.target.files]); // 새로 선택한 이미지 파일들 저장
    };

    // 기존 이미지 삭제 핸들러
    const handleDeleteExistingImage = (imageUrl) => {
        setFormData({
            ...formData,
            productImgUrls: formData.productImgUrls.filter((url) => url !== imageUrl), // 삭제한 이미지 URL 제거
        });
    };

    // 상품 삭제
    const handleDeleteClick = async (productId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/admin/products/${productId}`, { method: 'DELETE' });
            if (response.ok) {
                setProducts(products.filter((product) => product.id !== productId));
            } else {
                console.error('상품 삭제 실패');
            }
        } catch (error) {
            console.error('상품 삭제 중 오류 발생:', error);
        }
    };

    // 상품 수정 폼 제출
    const handleFormSubmit = async (e) => {
        e.preventDefault();
        const data = new FormData();

        // 텍스트 데이터 추가
        data.append('isDeleted', formData.isDeleted);
        data.append('categoryId', formData.categoryId);
        data.append('productName', formData.productName);
        data.append('price', formData.price);
        data.append('info', formData.info);
        data.append('stock', formData.stock);
        data.append('manufacturer', formData.manufacturer);

        // 기존 이미지 URLs 추가
        formData.productImgUrls.forEach((url) => {
            data.append('existingProductImgUrls', url);
        });

        // 새로 업로드할 이미지 파일 추가
        selectedImages.forEach((image) => {
            data.append('newProductImgUrls', image);
        });

        try {
            const response = await fetch(`http://localhost:8080/api/admin/products/${editProduct.id}`, {
                method: 'PUT',
                body: data, // FormData 사용
            });

            if (response.ok) {
                fetchProducts();
                setEditProduct(null); // 수정 완료 후 초기화
            } else {
                console.error('상품 수정 실패');
            }
        } catch (error) {
            console.error('상품 수정 중 오류 발생:', error);
        }
    };

    // 입력 값 변경 핸들러
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    return (
        <div className="container mt-5">
            <h1>상품 관리</h1>

            {/* 상품 목록 테이블 */}
            <table className="table table-bordered">
                <thead>
                <tr>
                    <th>수정</th>
                    <th>삭제</th>
                    <th>삭제 여부</th>
                    <th>상품 아이디</th>
                    <th>카테고리 아이디</th>
                    <th>상품 이름</th>
                    <th>가격</th>
                    <th>상품 정보</th>
                    <th>상품 재고</th>
                    <th>제조사</th>
                    <th>상품 이미지</th>
                    <th>생성일</th>
                    <th>수정일</th>
                </tr>
                </thead>
                <tbody>
                {products.map((product) => (
                    <tr key={product.id}>
                        <td>
                            <button className="btn btn-primary" onClick={() => handleEditClick(product)}>
                                ✏️
                            </button>
                        </td>
                        <td>
                            <button className="btn btn-danger" onClick={() => handleDeleteClick(product.id)}>
                                ❌
                            </button>
                        </td>
                        <td>{product.isDeleted ? '삭제됨' : '정상'}</td>
                        <td>{product.id}</td>
                        <td>{product.categoryId}</td>
                        <td>{product.productName}</td>
                        <td>{product.price}</td>
                        <td>{product.info || '없음'}</td>
                        <td>{product.stock}</td>
                        <td>{product.manufacturer}</td>
                        <td>
                            <div style={{ display: 'flex', flexWrap: 'wrap' }}>
                                {product.productImgUrls.map((image, index) => (
                                    <div key={index} style={{ width: '100px', margin: '5px' }}>
                                        <img
                                            className="img-fluid"
                                            src={image}
                                            alt={`Product image ${index + 1}`}
                                            style={{ objectFit: 'cover', width: '100%', height: '100px' }}
                                        />
                                    </div>
                                ))}
                            </div>
                        </td>
                        <td>{product.createdAt}</td>
                        <td>{product.updatedAt || '없음'}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* 수정 폼 */}
            {editProduct && (
                <div>
                    <h2>{editProduct.productName} 수정</h2>
                    <form onSubmit={handleFormSubmit} encType="multipart/form-data">
                        <div className="form-group">
                            <label>삭제 여부</label>
                            <select
                                name="isDeleted"
                                value={formData.isDeleted}
                                onChange={handleInputChange}
                                className="form-control"
                            >
                                <option value={false}>정상</option>
                                <option value={true}>삭제됨</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label>카테고리 아이디</label>
                            <input
                                type="number"
                                name="categoryId"
                                value={formData.categoryId}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>
                        <div className="form-group">
                            <label>상품명</label>
                            <input
                                type="text"
                                name="productName"
                                value={formData.productName}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>
                        <div className="form-group">
                            <label>가격</label>
                            <input
                                type="number"
                                name="price"
                                value={formData.price}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>
                        <div className="form-group">
                            <label>상품 정보</label>
                            <textarea
                                name="info"
                                value={formData.info}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>
                        <div className="form-group">
                            <label>상품 재고</label>
                            <input
                                type="number"
                                name="stock"
                                value={formData.stock}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>
                        <div className="form-group">
                            <label>제조사</label>
                            <input
                                type="text"
                                name="manufacturer"
                                value={formData.manufacturer}
                                onChange={handleInputChange}
                                className="form-control"
                            />
                        </div>
                        <div className="form-group">
                            <label>기존 상품 이미지</label>
                            <div style={{ display: 'flex', flexWrap: 'wrap' }}>
                                {formData.productImgUrls.map((url, index) => (
                                    <div key={index} style={{ width: '100px', margin: '5px', position: 'relative' }}>
                                        <img
                                            className="img-fluid"
                                            src={url}
                                            alt={`Existing image ${index + 1}`}
                                            style={{ objectFit: 'cover', width: '100%', height: '100px' }}
                                        />
                                        <button
                                            type="button"
                                            style={{
                                                position: 'absolute',
                                                top: '0',
                                                right: '0',
                                                backgroundColor: 'red',
                                                color: 'white',
                                                border: 'none',
                                                padding: '2px 5px',
                                            }}
                                            onClick={() => handleDeleteExistingImage(url)}
                                        >
                                            X
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <div className="form-group">
                            <label>새 상품 이미지 업로드</label>
                            <input
                                type="file"
                                name="productImgUrls"
                                onChange={handleImageChange}
                                className="form-control"
                                multiple
                            />
                        </div>
                        <button type="submit" className="btn btn-success">저장</button>
                        <button onClick={() => setEditProduct(null)} className="btn btn-secondary">취소</button>
                    </form>
                </div>
            )}
        </div>
    );
};

export default ProductManagement;
