// import React, { useEffect, useState } from 'react';
// import './AdBanner.css';
//
// const AdBanner = () => {
//     const [currentAdIndex, setCurrentAdIndex] = useState(0);
//     const ads = [
//         // require('../assets/images/광고1.png'),
//         require('../assets/images/광고2.png'),
//         require('../assets/images/광고3.png'),
//         require('../assets/images/광고4.png'),
//         require('../assets/images/광고5.png')
//     ];
//
//     useEffect(() => {
//         const interval = setInterval(() => {
//             setCurrentAdIndex((prevIndex) => (prevIndex + 1) % ads.length);
//         }, 5000); // 5초마다 변경
//         return () => clearInterval(interval);
//     }, [ads.length]);
//
//     return (
//         <div className="ad-banner">
//             <img src={ads[currentAdIndex]} alt="광고" />
//         </div>
//     );
// };
//
// export default AdBanner;

//-----------
// import React, { useEffect, useState } from 'react';
// import './AdBanner.css';
//
// const AdBanner = ({ userRole }) => {
//     const [currentAdIndex, setCurrentAdIndex] = useState(0);
//     const ads = [
//         // require('../assets/images/광고1.png'),
//         require('../assets/images/광고2.png'),
//         require('../assets/images/광고3.png'),
//         require('../assets/images/광고4.png'),
//         require('../assets/images/광고5.png')
//     ];
//
//     useEffect(() => {
//         const interval = setInterval(() => {
//             setCurrentAdIndex((prevIndex) => (prevIndex + 1) % ads.length);
//         }, 5000);
//         return () => clearInterval(interval);
//     }, [ads.length]);
//
//     return (
//         <div className="ad-banner">
//             <img src={ads[currentAdIndex]} alt="광고" />
//             {userRole === 'admin' && (
//                 <button className="add-ad-button">광고 추가</button>
//             )}
//         </div>
//     );
// };
//
// export default AdBanner;
//----------------------------

// import React, { useEffect, useState } from 'react';
// import './AdBanner.css';
//
// const AdBanner = ({ userRole }) => {
//     const [currentAdIndex, setCurrentAdIndex] = useState(0);
//     const ads = [
//         // require('../assets/images/광고1.png'),
//         require('../assets/images/광고2.png'),
//         require('../assets/images/광고3.png'),
//         require('../assets/images/광고4.png'),
//         require('../assets/images/광고5.png')
//     ];
//
//     useEffect(() => {
//         const interval = setInterval(() => {
//             setCurrentAdIndex((prevIndex) => (prevIndex + 1) % ads.length);
//         }, 5000);
//         return () => clearInterval(interval);
//     }, [ads.length]);
//
//     return (
//         <div className="ad-banner">
//             <img src={ads[currentAdIndex]} alt="광고" />
//             {userRole === 'ROLE_ADMIN' && (
//                 <button className="add-ad-button">광고 수정</button>
//             )}
//         </div>
//     );
// };
//
// export default AdBanner;

//----------------------

import React, { useEffect, useState } from 'react';
import './AdBanner.css';

const AdBanner = ({ userRole }) => {
    const [currentAdIndex, setCurrentAdIndex] = useState(0);
    const ads = [
        // require('../assets/images/광고1.png'),
        require('../assets/images/광고2.png'),
        require('../assets/images/광고3.png'),
        require('../assets/images/광고4.png'),
        require('../assets/images/광고5.png')
    ];

    // 5초마다 자동으로 광고 이미지를 전환
    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentAdIndex((prevIndex) => (prevIndex + 1) % ads.length);
        }, 5000);
        return () => clearInterval(interval);
    }, [ads.length]);

    // 다음 광고로 이동하는 함수
    const handleNextAd = () => {
        setCurrentAdIndex((prevIndex) => (prevIndex + 1) % ads.length);
    };

    // 이전 광고로 이동하는 함수
    const handlePrevAd = () => {
        setCurrentAdIndex((prevIndex) => (prevIndex - 1 + ads.length) % ads.length);
    };

    return (
        <div className="ad-banner">
            <img src={ads[currentAdIndex]} alt="광고" />
            <button className="prev-ad-button" onClick={handlePrevAd}>‹</button>
            <button className="next-ad-button" onClick={handleNextAd}>›</button>
            {userRole === 'ROLE_ADMIN' && (
                <button className="add-ad-button">광고 수정</button>
            )}
        </div>
    );
};

export default AdBanner;
