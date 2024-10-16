import React from 'react';

const PostcodeSearch = ({ setPostalCode, setDeliveryAddress }) => {
    const handlePostCode = () => {
        new window.daum.Postcode({
            oncomplete: function (data) {
                setPostalCode(data.zonecode);
                setDeliveryAddress(data.address);
            }
        }).open();
    };

    return <button className="btn-custom" onClick={handlePostCode}>우편번호 찾기</button>;
};

export default PostcodeSearch;