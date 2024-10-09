import React from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Header from "./fragments/Header";
import Home from "./Home";
import Cart from "./cart/Cart";
import Footer from "./fragments/Footer";
import ProductSection from "./ProductSection";
import Login from "./auth/Login";
import Signup from "./auth/Signup";
import MyPage from "./member/MyPage";

const App = () => {
    return (
        <Router>
            <div id="root">
                <Header />
                <main className="flex-grow-1">
                    <Routes>
                        <Route path="/" element={<Home />} /> {/* 홈 화면 */}
                        <Route path="/login" element={<Login />} /> {/* login 화면 */}
                        <Route path="/signup" element={<Signup />} /> {/* login 화면 */}
                        <Route path="/me" element={<MyPage />} /> {/* mypage 화면 */}
                        <Route path="/product/:id" element={<ProductSection />} />
                        <Route path="/cart" element={<Cart />} /> {/* 장바구니 화면 */}
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
};

export default App;
