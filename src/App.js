import React from 'react';
import Header from './fragments/Header';
import Footer from './fragments/Footer';
import GuestCart from "./cart/GuestCart";

const App = () => {
    return (
        <div id="root"> {/* id="root" 추가 */}
            <Header />
            <main className="flex-grow-1"> {/* 메인 콘텐츠를 포함하는 div */}
                {/* 여기에 메인 콘텐츠를 추가 */}
                <GuestCart></GuestCart>
            </main>
            <Footer />
        </div>
    );
};

export default App;
