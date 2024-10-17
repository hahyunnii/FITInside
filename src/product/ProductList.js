import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, Link } from 'react-router-dom';
import './ProductList.css'; // Import CSS file for custom styles

const ProductList = () => {
    const { categoryId } = useParams(); // URL에서 categoryId를 가져옴
    const [products, setProducts] = useState([]); // 초기 값을 빈 배열로 설정
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(0); // 페이지네이션을 위한 페이지 상태
    const [size, setSize] = useState(9); // 페이지당 아이템 수
    const [sortField, setSortField] = useState('createdAt'); // 정렬 필드
    const [sortDir, setSortDir] = useState('desc'); // 정렬 방향
    const [keyword, setKeyword] = useState(''); // 검색어

    useEffect(() => {
        // 상품 목록을 백엔드에서 가져오는 함수
        const fetchProducts = async () => {
            try {
                setLoading(true);
                const response = await axios.get(`http://localhost:8080/api/products/category/${categoryId}`, {
                    params: {
                        page,
                        size,
                        sortField,
                        sortDir,
                        keyword
                    }
                });

                // 응답이 배열인지 확인하고, 배열이 아닐 경우 빈 배열로 처리
                const productData = Array.isArray(response.data) ? response.data : response.data.content || [];

                setProducts(productData);
            } catch (err) {
                setError('상품 목록을 불러오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, [categoryId, page, size, sortField, sortDir, keyword]);

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    if (!Array.isArray(products)) {
        return <p>상품 목록이 없습니다.</p>;
    }

    return (
        <>
            {/* Header */}
            <header className="bg-dark py-5">
                <div className="container px-4 px-lg-5 my-5">
                    <div className="text-center text-white">
                        <h1 className="display-4 fw-bolder">Shop in style</h1>
                        <p className="lead fw-normal text-white-50 mb-0">With this shop homepage template</p>
                    </div>
                </div>
            </header>

            {/* 검색과 정렬 옵션 */}
            <div className="container mt-4">
                <div className="row">
                    <div className="col-md-6">
                        <input
                            type="text"
                            placeholder="검색어를 입력하세요"
                            value={keyword}
                            onChange={(e) => setKeyword(e.target.value)}
                            className="form-control"
                        />
                    </div>
                    <div className="col-md-3">
                        <select className="form-select" value={sortField} onChange={(e) => setSortField(e.target.value)}>
                            <option value="createdAt">생성일</option>
                            <option value="price">가격</option>
                            <option value="productName">상품명</option>
                        </select>
                    </div>
                    <div className="col-md-3">
                        <select className="form-select" value={sortDir} onChange={(e) => setSortDir(e.target.value)}>
                            <option value="asc">오름차순</option>
                            <option value="desc">내림차순</option>
                        </select>
                    </div>
                </div>
            </div>

            {/* Section */}
            <section className="py-5">
                <div className="container px-4 px-lg-5 mt-5">
                    <div className="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
                        {products.map((product) => (
                            <div className="col mb-5" key={product.id}>
                                <div className="card h-100">
                                    {/* Sale badge */}
                                    {product.onSale && (
                                        <div className="badge bg-dark text-white position-absolute" style={{ top: '0.5rem', right: '0.5rem' }}>
                                            Sale
                                        </div>
                                    )}
                                    {/* Product image */}
                                    {product.productImgUrls && product.productImgUrls.length > 0 ? (
                                        <img className="card-img-top product-image" src={product.productImgUrls[0]} alt={product.productName} />
                                    ) : (
                                        <img className="card-img-top product-image" src="/placeholder-image.jpg" alt="No image available" />
                                    )}
                                    {/* Product details */}
                                    <div className="card-body p-4">
                                        <div className="text-center">
                                            {/* Product name */}
                                            <h5 className="fw-bolder">{product.productName}</h5>
                                            {/* Product price */}
                                            <div>
                                                {product.oldPrice && (
                                                    <span className="text-muted text-decoration-line-through">
                                                        ${product.oldPrice}
                                                    </span>
                                                )}
                                                <span> ${product.price}</span>
                                            </div>
                                        </div>
                                    </div>
                                    {/* Product actions */}
                                    <div className="card-footer p-4 pt-0 border-top-0 bg-transparent">
                                        <div className="text-center">
                                            <Link className="btn btn-outline-dark mt-auto" to={`/product/${product.id}`}>
                                                View Details
                                            </Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* Pagination */}
                    <div className="d-flex justify-content-center">
                        <button
                            className="btn btn-outline-primary"
                            onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
                            disabled={page === 0}>
                            이전
                        </button>
                        <button
                            className="btn btn-outline-primary ms-2"
                            onClick={() => setPage((prev) => prev + 1)}>
                            다음
                        </button>
                    </div>
                </div>
            </section>
        </>
    );
};

export default ProductList;
