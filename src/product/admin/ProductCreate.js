import React, { useState } from 'react';

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

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleImageChange = (e) => {
        setImages([...e.target.files]);
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
        images.forEach((image, index) => {
            data.append('productImgUrls', image);
        });

        // 서버에 데이터 전송 로직 작성
        try {
            const response = await fetch('/api/products', {
                method: 'POST',
                body: data
            });
            if (!response.ok) {
                throw new Error('상품 등록에 실패했습니다.');
            }
            console.log('상품이 성공적으로 등록되었습니다.');
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
                {/*<div className="form-group row">*/}
                {/*    <label className="col-sm-2 col-form-label">상태</label>*/}
                {/*    <div className="col-sm-5">*/}
                {/*        <div className="form-check form-check-inline">*/}
                {/*            <input*/}
                {/*                className="form-check-input"*/}
                {/*                type="radio"*/}
                {/*                name="condition"*/}
                {/*                value="New"*/}
                {/*                checked={formData.condition === 'New'}*/}
                {/*                onChange={handleChange}*/}
                {/*            />*/}
                {/*            <label className="form-check-label">신규 제품</label>*/}
                {/*        </div>*/}
                {/*        <div className="form-check form-check-inline">*/}
                {/*            <input*/}
                {/*                className="form-check-input"*/}
                {/*                type="radio"*/}
                {/*                name="condition"*/}
                {/*                value="Old"*/}
                {/*                checked={formData.condition === 'Old'}*/}
                {/*                onChange={handleChange}*/}
                {/*            />*/}
                {/*            <label className="form-check-label">중고 제품</label>*/}
                {/*        </div>*/}
                {/*        <div className="form-check form-check-inline">*/}
                {/*            <input*/}
                {/*                className="form-check-input"*/}
                {/*                type="radio"*/}
                {/*                name="condition"*/}
                {/*                value="Refurbished"*/}
                {/*                checked={formData.condition === 'Refurbished'}*/}
                {/*                onChange={handleChange}*/}
                {/*            />*/}
                {/*            <label className="form-check-label">재생 제품</label>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}
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
