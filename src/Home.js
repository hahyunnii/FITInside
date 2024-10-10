// import React from 'react';
//
// const Home = () => {
//     return (
//         <h2>홈 화면</h2>
//     );
// };
//
// export default Home;

import React from 'react';
import AdBanner from './mainPage/components/AdBanner';
import CategoryList from './mainPage/components/CategoryList';

const Home = () => {
    return (
        <div>
            <AdBanner />
            <CategoryList />
        </div>
    );
};

export default Home;