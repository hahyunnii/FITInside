import React from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Header from "./fragments/Header";
import Home from "./Home";
import Cart from "./cart/Cart";
import Footer from "./fragments/Footer";
import ProductSection from "./product/ProductSection";
import Login from "./auth/Login";
import Signup from "./auth/Signup";
import MyPage from "./member/MyPage";
import OrderCreate from "./order/OrderCreate";
import OrderDetail from './order/OrderDetail';
import OrderList from "./order/OrderList";
import OrderAdminList from "./order/OrderAdminList";
import CouponAdmin from "./coupon/admin/CouponAdmin";
import CouponList from "./coupon/CouponList";
import WelcomeCouponList from "./coupon/WelcomeCouponList";
import AdminPage from "./AdminPage";
import CategoryAdminMain from "./category/CategoryAdminMain";
import CategoryCreate from "./category/CategoryCreate";
import CategoryUpdate from "./category/CategoryUpdate";
import ProductList from "./product/ProductList";

const App = () => {
    return (
        <Router>
            <div id="root">
                <Header />
                <main className="flex-grow-1">
                    <Routes>
                        <Route path="/" element={<Home />} /> {/* 홈 화면 */}

                        <Route path="/admin" element={<AdminPage/>} /> {/*관리자 화면*/}

                        <Route path="/login" element={<Login />} /> {/* login 화면 */}
                        <Route path="/signup" element={<Signup />} /> {/* login 화면 */}
                        <Route path="/me" element={<MyPage />} /> {/* mypage 화면 */}
                        <Route path="/product/:id" element={<ProductSection />} />
                        <Route path="/cart" element={<Cart />} /> {/* 장바구니 화면 */}
                        <Route path="/order" element={<OrderCreate />} /> {/* 주문서 작성 화면 */}
                        <Route path="/orders/:orderId" element={<OrderDetail />} /> {/* 상세 주문 화면*/}
                        <Route path="/orders" element={<OrderList />} /> {/* 주문 목록 화면 */}
                        <Route path="/admin/orders" element={<OrderAdminList />}/> {/*관리자 주문 목록 화면*/}

                        <Route path="/admin/coupons" element={<CouponAdmin />} /> {/*쿠폰 관리자 화면*/}
                        <Route path="/coupons" element={<CouponList />} /> {/*쿠폰 회원 화면*/}
                        <Route path="/coupons/welcome" element={<WelcomeCouponList />} /> {/*웰컴 쿠폰 화면*/}

                        <Route path="/admin/categories" element={<CategoryAdminMain />} />
                        <Route path="/category-create" element={<CategoryCreate />} />
                        <Route path="/category-update/:categoryId" element={<CategoryUpdate />} />

                        <Route path="/product/:id" element={<ProductSection />} />
                        <Route path="/products/category/:categoryId" element={<ProductList />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
};

export default App;
