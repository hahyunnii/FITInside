import React, { useState } from 'react';

const ProductCreate = () => {
    const [formData, setFormData] = useState({
        productId: '',
        name: '',
        unitPrice: '',
        description: '',
        manufacturer: '',
        category: '',
        unitsInStock: '',
        condition: '',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // 서버에 데이터 전송 로직 작성
        console.log('등록할 상품 데이터:', formData);
    };

    return (
        <div className="container mt-5">
            <h1 className="display-4 mb-4">상품 등록</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">상품 코드</label>
                    <div className="col-sm-3">
                        <input
                            type="text"
                            name="productId"
                            value={formData.productId}
                            onChange={handleChange}
                            className="form-control"
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">상품명</label>
                    <div className="col-sm-3">
                        <input
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            className="form-control"
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">가격</label>
                    <div className="col-sm-3">
                        <input
                            type="text"
                            name="unitPrice"
                            value={formData.unitPrice}
                            onChange={handleChange}
                            className="form-control"
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">상세 정보</label>
                    <div className="col-sm-5">
                        <textarea
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            rows="2"
                            className="form-control"
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
                    <label className="col-sm-2 col-form-label">분류</label>
                    <div className="col-sm-3">
                        <input
                            type="text"
                            name="category"
                            value={formData.category}
                            onChange={handleChange}
                            className="form-control"
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">재고 수</label>
                    <div className="col-sm-3">
                        <input
                            type="text"
                            name="unitsInStock"
                            value={formData.unitsInStock}
                            onChange={handleChange}
                            className="form-control"
                        />
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label">상태</label>
                    <div className="col-sm-5">
                        <div className="form-check form-check-inline">
                            <input
                                className="form-check-input"
                                type="radio"
                                name="condition"
                                value="New"
                                checked={formData.condition === 'New'}
                                onChange={handleChange}
                            />
                            <label className="form-check-label">신규 제품</label>
                        </div>
                        <div className="form-check form-check-inline">
                            <input
                                className="form-check-input"
                                type="radio"
                                name="condition"
                                value="Old"
                                checked={formData.condition === 'Old'}
                                onChange={handleChange}
                            />
                            <label className="form-check-label">중고 제품</label>
                        </div>
                        <div className="form-check form-check-inline">
                            <input
                                className="form-check-input"
                                type="radio"
                                name="condition"
                                value="Refurbished"
                                checked={formData.condition === 'Refurbished'}
                                onChange={handleChange}
                            />
                            <label className="form-check-label">재생 제품</label>
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
