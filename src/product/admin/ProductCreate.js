import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const ProductCreate = () => {
    const [formData, setFormData] = useState({
        categoryId: '',
        productName: '',
        price: '',
        info: '',
        manufacturer: '',
        stock: '',
        condition: '',
        productImgUrls: []
    });

    const [images, setImages] = useState([]);
    const [previewImages, setPreviewImages] = useState([]);
    const navigate = useNavigate();


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleImageChange = (e) => {
        const selectedFiles = Array.from(e.target.files);
        setImages(selectedFiles);

        // 미리보기 이미지 생성
        const previewUrls = selectedFiles.map(file => URL.createObjectURL(file));
        setPreviewImages(previewUrls);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const data = new FormData();
        data.append('categoryId', formData.categoryId);
        data.append('productName', formData.productName);
        data.append('price', formData.price);
        data.append('info', formData.info);
        data.append('manufacturer', formData.manufacturer);
        data.append('stock', formData.stock);
        data.append('condition', formData.condition);

        // 이미지를 FormData에 추가
        images.forEach((image) => {
            data.append('productImgUrls', image);
        });

        try {
            const response = await fetch('http://localhost:8080/api/admin/products', {
                method: 'POST',
                body: data
            });
            if (!response.ok) {
                throw new Error('상품 등록에 실패했습니다.');
            }
            console.log('상품이 성공적으로 등록되었습니다.');
            navigate('/admin/products');
        } catch (error) {
            console.error('에러 발생:', error);
        }
    };

    return (
        <div className="container mt-5">
            <h1 className="display-4 mb-4">상품 등록</h1>
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">카테고리 ID</label>
                    <div className="col-sm-3">
                        <input
                            type="text"
                            name="categoryId"
                            value={formData.categoryId}
                            onChange={handleChange}
                            className="form-control"
                            required
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">상품명</label>
                    <div className="col-sm-3">
                        <input
                            type="text"
                            name="productName"
                            value={formData.productName}
                            onChange={handleChange}
                            className="form-control"
                            required
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">가격</label>
                    <div className="col-sm-3">
                        <input
                            type="number"
                            name="price"
                            value={formData.price}
                            onChange={handleChange}
                            className="form-control"
                            required
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">상세 정보</label>
                    <div className="col-sm-5">
                        <textarea
                            name="info"
                            value={formData.info}
                            onChange={handleChange}
                            rows="2"
                            className="form-control"
                            required
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">제조사</label>
                    <div className="col-sm-3">
                        <input
                            type="text"
                            name="manufacturer"
                            value={formData.manufacturer}
                            onChange={handleChange}
                            className="form-control"
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">재고 수</label>
                    <div className="col-sm-3">
                        <input
                            type="number"
                            name="stock"
                            value={formData.stock}
                            onChange={handleChange}
                            className="form-control"
                            required
                        />
                    </div>
                </div>
                {/* 이미지 업로드 필드 */}
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">이미지 업로드</label>
                    <div className="col-sm-3">
                        <input
                            type="file"
                            name="productImgUrls"
                            onChange={handleImageChange}
                            className="form-control"
                            multiple
                        />
                    </div>
                </div>

                {/* 이미지 미리보기 */}
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">이미지 미리보기</label>
                    <div className="col-sm-10">
                        <div className="row">
                            {previewImages.map((src, index) => (
                                <div key={index} className="col-md-3">
                                    <img
                                        src={src}
                                        alt={`미리보기 ${index + 1}`}
                                        className="img-thumbnail"
                                        style={{ width: '100%', height: 'auto' }}
                                    />
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                <div className="form-group row">
                    <div className="col-sm-10">
                        <button type="submit" className="btn btn-primary">등록</button>
                    </div>
                </div>
            </form>
        </div>
    );
};

export default ProductCreate;
