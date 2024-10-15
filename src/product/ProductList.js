import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, Link } from 'react-router-dom';

const ProductList = () => {
    const { categoryId } = useParams(); // URL에서 categoryId를 가져옴
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // 상품 목록을 백엔드에서 가져오는 함수
        const fetchProducts = async () => {
            try {
                setLoading(true);
                const response = await axios.get(`http://localhost:8080/api/products/category/${categoryId}`);
                setProducts(response.data);
            } catch (err) {
                setError('상품 목록을 불러오는 데 실패했습니다.');
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, [categoryId]);

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p>{error}</p>;
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
                                    {/* Product images (displays the first image) */}
                                    {product.productImgUrls && product.productImgUrls.length > 0 ? (
                                        <img className="card-img-top" src={product.productImgUrls[0]} alt={product.productName} />
                                    ) : (
                                        <img className="card-img-top" src="/placeholder-image.jpg" alt="No image available" />
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
                </div>
            </section>
        </>
    );
};

export default ProductList;
