import React from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Header from "./fragments/Header";
import Home from "./Home";
import GuestCart from "./cart/GuestCart";
import Footer from "./fragments/Footer";

const App = () => {
    return (
        <Router>
            <div id="root">
                <Header />
                <main className="flex-grow-1">
                    <Routes>
                        <Route path="/" element={<Home />} /> {/* 홈 화면 */}
                        <Route path="/cart" element={<GuestCart />} /> {/* 장바구니 화면 */}
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
};

export default App;
