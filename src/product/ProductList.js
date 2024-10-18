import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, Link } from 'react-router-dom';
import './ProductList.css'; // Import CSS file for custom styles

const ProductList = () => {
    const { categoryId } = useParams(); // URL에서 categoryId를 가져옴
    const [products, setProducts] = useState([]); // 초기 값을 빈 배열로 설정
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(0); // 현재 페이지 상태
    const [size, setSize] = useState(9); // 페이지당 아이템 수
    const [sortField, setSortField] = useState('createdAt'); // 정렬 필드
    const [sortDir, setSortDir] = useState('desc'); // 정렬 방향
    const [keyword, setKeyword] = useState(''); // 검색어
    const [totalPages, setTotalPages] = useState(1); // 전체 페이지 수 상태
    const pagesPerGroup = 5; // 한 번에 표시할 페이지 번호 개수

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

                // 응답에서 데이터와 전체 페이지 수 추출
                const productData = Array.isArray(response.data.content) ? response.data.content : [];
                const totalPages = response.data.totalPages || 1; // totalPages가 없는 경우 기본값 1

                setProducts(productData);
                setTotalPages(totalPages);
            } catch (err) {
                setError('상품 목록을 불러오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, [categoryId, page, size, sortField, sortDir, keyword]);

    // 페이지 클릭 핸들러
    const handlePageClick = (pageNumber) => {
        setPage(pageNumber);
    };

    // 중앙에 페이지 버튼이 오도록 시작 페이지와 끝 페이지 계산
    const middleIndex = Math.floor(pagesPerGroup / 2); // 중앙 인덱스
    let startPage = Math.max(page - middleIndex, 0);
    let endPage = Math.min(startPage + pagesPerGroup, totalPages);

    // 총 페이지 수보다 startPage와 endPage 범위가 크면 startPage를 다시 조정
    if (endPage - startPage < pagesPerGroup) {
        startPage = Math.max(0, endPage - pagesPerGroup);
    }

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
                    <nav aria-label="Page navigation example">
                        <ul className="pagination justify-content-center">
                            {/* Previous 페이지 그룹 버튼 */}
                            <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
                                <button
                                    className="page-link"
                                    onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
                                    aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </button>
                            </li>

                            {/* 페이지 번호 버튼 */}
                            {Array.from({ length: endPage - startPage }, (_, index) => {
                                const pageNumber = startPage + index;
                                return (
                                    <li key={pageNumber} className={`page-item ${page === pageNumber ? 'active' : ''}`}>
                                        <button
                                            className="page-link"
                                            onClick={() => handlePageClick(pageNumber)}>
                                            {pageNumber + 1}
                                        </button>
                                    </li>
                                );
                            })}

                            {/* Next 페이지 그룹 버튼 */}
                            <li className={`page-item ${page === totalPages - 1 ? 'disabled' : ''}`}>
                                <button
                                    className="page-link"
                                    onClick={() => setPage((prev) => Math.min(prev + 1, totalPages - 1))}
                                    aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </button>
                            </li>
                        </ul>
                    </nav>
                </div>
            </section>
        </>
    );
};

export default ProductList;
