import React from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';

import { Header, Footer } from "./fragments";
import Home from "./Home";
import { Cart } from "./cart";
import { ProductSection, ProductList } from "./product";
import { Login, Signup, GoogleTokenCheck } from "./auth";
import { MyPage } from "./member";
import { OrderCreate, OrderDetail, OrderList, OrderAdminList } from "./order";
import { CouponAdmin, CouponList, WelcomeCouponList } from "./coupon";
import AdminPage from "./AdminPage";
import { CategoryAdminMain, CategoryCreate, CategoryUpdate } from "./category";
import { ProductAdmin, ProductCreate, ProductUpdate, ProductDelete } from './product/admin';


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
                        <Route path="/tokenCheck" element={<GoogleTokenCheck />} /> {/* Google로그인 후 토큰 저장 */}
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
                        <Route path="/category-update/:id" element={<CategoryUpdate />} />

                        <Route path="/product/:id" element={<ProductSection />} />
                        <Route path="/products/category/:categoryId" element={<ProductList />} />
                        <Route path="/admin/products" element={<ProductAdmin />} /> {/* 관리자 상품 관리 화면 */}
                        <Route path="/admin/products/create" element={<ProductCreate />} />
                        <Route path="/admin/products/update" element={<ProductUpdate />} />
                        <Route path="/admin/products/delete" element={<ProductDelete />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
};

export default App;
