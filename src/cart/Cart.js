import React, {useState, useEffect} from 'react';
import {Link} from 'react-router-dom'; // Link import
import {getCart, removeFromCart, clearCart, fetchProduct, useCartCount, updateCartQuantity} from './cartStorage';
import './cart.css';

const Cart = () => {
    const [cart, setCart] = useState([]);
    const [productDetails, setProductDetails] = useState({});
    const [selectedItems, setSelectedItems] = useState(new Set()); // 선택된 아이템 관리

    useEffect(() => {
        setCart(getCart());
    }, []);

    useEffect(() => {
        const fetchAllProducts = async () => {
            const details = {};
            for (const item of cart) {
                const productData = await fetchProduct(item.id);
                if (productData) {
                    details[item.id] = productData;
                }
            }
            setProductDetails(details);
        };

        if (cart.length > 0) {
            fetchAllProducts();
        }
    }, [cart]);

    const handleRemoveFromCart = (id) => {
        removeFromCart(id);
        setCart(getCart());
        selectedItems.delete(id); // 삭제 시 선택된 아이템에서 제거
        setSelectedItems(new Set(selectedItems)); // 상태 업데이트
    };

    const handleClearCart = () => {
        clearCart();
        setCart([]);
        setProductDetails({});
        setSelectedItems(new Set()); // 장바구니 초기화 시 선택된 아이템 초기화
    };

    const handleSelectAll = () => {
        if (selectedItems.size === cart.length) {
            setSelectedItems(new Set()); // 모든 아이템 선택 해제
        } else {
            const allIds = new Set(cart.map(item => item.id)); // 모든 아이템 ID 추가
            setSelectedItems(allIds);
        }
    };

    const handleRemoveSelected = () => {
        selectedItems.forEach(id => {
            removeFromCart(id);
        });
        setCart(getCart());
        setSelectedItems(new Set()); // 선택 삭제 후 선택된 아이템 초기화
    };

    const handleQuantityChange = (id, newQuantity) => {
        // cartStorage.js의 updateCartQuantity 함수 호출
        updateCartQuantity(id, newQuantity);
        // 로컬 스토리지에서 장바구니 업데이트 후 상태를 다시 가져옵니다.
        setCart(getCart());
    };

    const cartCount = useCartCount(); // cart 상태를 인수로 전달

    const getTotalPrice = () => {
        return cart.reduce((total, item) => {
            const price = productDetails[item.id]?.price || 0;
            return total + price * item.quantity;
        }, 0);
    };

    return (
        <div className="cart-container">
            <h2>쇼핑백</h2>
            <br/>
            <div className="both-container">
                <div className="left-container">
                    <div className="d-flex justify-content-between">
                        <div>
                            <button className="btn btn-light text-dark me-2" style={{border: '1px solid #ced4da'}}
                                    onClick={handleSelectAll}>
                                {selectedItems.size === cart.length ? '전체선택 해제' : '전체선택'}
                            </button>
                            <button className="btn btn-light text-dark me-2" style={{border: '1px solid #ced4da'}}
                                    onClick={handleRemoveSelected} disabled={selectedItems.size === 0}>
                                선택삭제
                            </button>
                            <button className="btn btn-danger" style={{border: '1px solid #dc3545'}}
                                    onClick={handleClearCart}>
                                전체삭제
                            </button>
                        </div>
                    </div>
                    <hr/>

                    {cart.length === 0 ? ( // 장바구니가 비어있을 때 메시지 출력
                        <p className="no-content">장바구니에 상품이 없습니다.</p>
                    ) : (
                        <table className="table">
                            <thead>
                            <tr>
                                <th></th>
                                <th>상품정보</th>
                                <th>상품금액</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {cart.map(item => (
                                <tr key={item.id}>
                                    <td>
                                        <input
                                            type="checkbox"
                                            checked={selectedItems.has(item.id)}
                                            onChange={() => {
                                                const newSelectedItems = new Set(selectedItems);
                                                if (newSelectedItems.has(item.id)) {
                                                    newSelectedItems.delete(item.id);
                                                } else {
                                                    newSelectedItems.add(item.id);
                                                }
                                                setSelectedItems(newSelectedItems);
                                            }}
                                        />
                                    </td>
                                    <td>
                                        {productDetails[item.id] ? (
                                            <div>
                                                <p style={{margin: `0`}}>{productDetails[item.id].manufacturer}</p>
                                                <p style={{fontWeight: 'bold'}}>
                                                    <Link to={`/product/${item.id}`}
                                                          style={{textDecoration: 'none', color: 'inherit'}}>
                                                        {productDetails[item.id].productName}
                                                    </Link>
                                                </p>                                                <label
                                                className="d-flex align-items-center"> {/* d-flex로 가로 정렬, align-items-center로 수직 중앙 정렬 */}
                                                수량
                                                <select
                                                    className="form-select ms-2" // ms-2로 오른쪽에 여백 추가
                                                    style={{width: 'auto'}} // 추가적인 스타일 적용
                                                    value={item.quantity}
                                                    onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value, 10))}
                                                >
                                                    {/* 1부터 20까지의 수량 선택 */}
                                                    {[...Array(20)].map((_, index) => (
                                                        <option key={index + 1} value={index + 1}>
                                                            {index + 1}
                                                        </option>
                                                    ))}
                                                </select>
                                            </label>

                                            </div>
                                        ) : (
                                            <p>상품 정보를 불러올 수 없습니다...</p>
                                        )}
                                    </td>
                                    <td>
                                        {productDetails[item.id] ? (
                                            <p>
                                                {(productDetails[item.id].price * item.quantity).toLocaleString()} 원
                                            </p>
                                        ) : (
                                            <p>상품 정보를 불러올 수 없습니다...</p>
                                        )}
                                    </td>
                                    <td>
                                        <button className="btn btn-light text-dark me-2"
                                                style={{border: '1px solid #ced4da'}}
                                                onClick={() => handleRemoveFromCart(item.id)}>
                                            삭제
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>

                <div className="right-container">
                    <div className="order-container">
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <p>상품 금액 ({cartCount})</p>
                            <p>{getTotalPrice().toLocaleString()} 원</p>
                        </div>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <p>배송비</p>
                            {getTotalPrice() < 20000 ? (
                                <p>2,500 원</p>
                            ) : (
                                <span>
                                    <p style={{textDecoration: 'line-through'}}>2,500 원</p>
                                    <p style={{marginLeft: '8px'}}>무료</p>
                                </span>
                            )}

                        </div>
                        <hr/>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <strong>결제예정금액</strong>
                            {getTotalPrice() < 20000 ? (
                                <strong style={{color: '#B22222'}}>{(getTotalPrice()+2500).toLocaleString()} 원</strong>
                            ) : (
                                <strong style={{color: '#B22222'}}>{getTotalPrice().toLocaleString()} 원</strong>
                            )}
                        </div>
                        <div style={{display: 'flex', justifyContent: 'center'}}>
                            <div className="discount-container">
                                <strong>0원</strong>
                                <strong style={{color: '#B22222'}}>(0%) </strong>
                                &nbsp;<p style={{margin: 0}}>할인 받았어요!</p>
                            </div>
                        </div>
                    </div>

                    <div style={{display: 'flex', justifyContent: 'center', margin: `10px 0`}}>
                        <a className="btn btn-light text-dark me-2 p-3" style={{border: '1px solid #ced4da'}} href="/">계속
                            쇼핑하기</a>
                        <a className="btn btn-custom p-3" href="/">전체 주문하기</a>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Cart;
