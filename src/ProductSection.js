import React, { useState, useEffect } from 'react';
import {useNavigate, useParams} from 'react-router-dom'; // useParams import
import { addToCart } from './cart/cartStorage'; // cartStorage에서 addToCart 함수 가져오기

const ProductSection = () => {
    const { id: productId } = useParams();
    const [product, setProduct] = useState(null);
    // const [relatedProducts, setRelatedProducts] = useState([]);
    const [quantity, setQuantity] = useState(1);
    const navigate = useNavigate(); // useNavigate 훅 사용

    useEffect(() => {
        // 제품 데이터 가져오기
        const fetchProduct = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/products/${productId}`);
                if (response.ok) {
                    const data = await response.json();
                    setProduct(data);
                } else {
                    console.error("Failed to fetch product data");
                }
            } catch (error) {
                console.error("Error fetching product data:", error);
            }
        };

        fetchProduct();
    }, [productId]);

    const handleAddToCart = async () => {
        if (product) {
            const cartItem = {
                id: product.id, // 고유 ID : 상품 id
                quantity: quantity, // 선택한 수량
            };

            // addToCart 호출 결과 체크
            const result = await addToCart(cartItem); // await를 사용하여 비동기 처리
            if (result) {
                alert(`${product.productName}이(가) 장바구니에 추가되었습니다.`);
            }

        }

        const moveToCart = window.confirm(`장바구니로 이동하시겠습니까?`);
        if (moveToCart) {
            navigate('/cart'); // /cart로 이동
        }
    };


    if (!product) {
        return <p>Loading...</p>; // 로딩 상태 표시
    }

    return (
        // Product section
        <section className="py-5">
            <div className="container px-4 px-lg-5 my-5">
                <div className="row gx-4 gx-lg-5 align-items-center">
                    <div className="col-md-6">
                        <img className="card-img-top" src="https://dummyimage.com/450x300/dee2e6/6c757d.jpg" alt="..."/>
                    </div>
                    <div className="col-md-6">
                        <div className="small mb-1">상품 이름: {product.productName}</div>
                        <div className="fs-5 mb-5">
                            <span>{product.price}원</span>
                        </div>
                        <div className="d-flex">
                            <input
                                className="form-control text-center me-3"
                                id="inputQuantity"
                                type="number"
                                value={quantity}
                                onChange={(e) => setQuantity(Math.max(1, Math.min(20, e.target.value)))}
                                style={{maxWidth: '3rem'}}
                            />
                            <button className="btn btn-outline-dark flex-shrink-0" type="button"
                                    onClick={handleAddToCart}>
                                <i className="bi-cart-fill me-1"></i>
                                Add to cart
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/*/!* Related items section *!/*/}
            {/*<section className="py-5 bg-light">*/}
            {/*    <div className="container px-4 px-lg-5 mt-5">*/}
            {/*        <h2 className="fw-bolder mb-4">Related products</h2>*/}
            {/*        <div className="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">*/}
            {/*            {relatedProducts.map((relatedProduct) => (*/}
            {/*                <div className="col mb-5" key={relatedProduct.id}>*/}
            {/*                    <div className="card h-100">*/}
            {/*                        {relatedProduct.isSale && <div className="badge bg-dark text-white position-absolute" style={{ top: '0.5rem', right: '0.5rem' }}>Sale</div>}*/}
            {/*                        <img className="card-img-top" src={relatedProduct.imageUrl} alt={relatedProduct.name} />*/}
            {/*                        <div className="card-body p-4">*/}
            {/*                            <div className="text-center">*/}
            {/*                                <h5 className="fw-bolder">{relatedProduct.name}</h5>*/}
            {/*                                <span className={relatedProduct.originalPrice ? "text-muted text-decoration-line-through" : ""}>*/}
            {/*                                    {relatedProduct.originalPrice ? `$${relatedProduct.originalPrice}` : ""}*/}
            {/*                                </span>*/}
            {/*                                {relatedProduct.price && <span> ${relatedProduct.price}</span>}*/}
            {/*                            </div>*/}
            {/*                        </div>*/}
            {/*                        <div className="card-footer p-4 pt-0 border-top-0 bg-transparent">*/}
            {/*                            <div className="text-center">*/}
            {/*                                <a className="btn btn-outline-dark mt-auto" href="#">View options</a>*/}
            {/*                            </div>*/}
            {/*                        </div>*/}
            {/*                    </div>*/}
            {/*                </div>*/}
            {/*            ))}*/}
            {/*        </div>*/}
            {/*    </div>*/}
            {/*</section>*/}
        </section>
    );
};

export default ProductSection;
