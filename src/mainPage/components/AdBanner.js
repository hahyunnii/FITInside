import React, { useEffect, useState } from 'react';

const AdBanner = () => {
    const [currentAdIndex, setCurrentAdIndex] = useState(0);
    const ads = [
        // require('../assets/images/광고1.png'),
        require('../assets/images/광고2.png'),
        require('../assets/images/광고3.png'),
        require('../assets/images/광고4.png'),
        require('../assets/images/광고5.png')
    ];

    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentAdIndex((prevIndex) => (prevIndex + 1) % ads.length);
        }, 5000); // 5초마다 변경
        return () => clearInterval(interval);
    }, [ads.length]);

    return (
        <div className="ad-banner">
            <img src={ads[currentAdIndex]} alt="광고" />
        </div>
    );
};

export default AdBanner;