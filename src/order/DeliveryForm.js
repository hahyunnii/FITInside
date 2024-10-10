import React, { useState } from 'react';

const DeliveryForm = ({ onSubmit }) => {
    const [deliveryAddress, setDeliveryAddress] = useState('');
    const [deliveryReceiver, setDeliveryReceiver] = useState('');
    const [deliveryPhone, setDeliveryPhone] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({ deliveryAddress, deliveryReceiver, deliveryPhone });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>배송 정보 입력</h2>
            <div>
                <label>받는 사람:</label>
                    <input
                        type="text"
                        value={deliveryReceiver}
                        onChange={(e) => setDeliveryReceiver(e.target.value)}
                        required
                    />
            </div>
            <div>
                <label>주소:</label>
                    <input
                        type="text"
                        value={deliveryAddress}
                        onChange={(e) => setDeliveryAddress(e.target.value)}
                        required
                    />
            </div>
            <div>
                <label>연락처:</label>
                    <input
                        type="text"
                        value={deliveryPhone}
                        onChange={(e) => setDeliveryPhone(e.target.value)}
                        required
                    />
            </div>
            <button type="submit">결제하기</button>
        </form>
    );
};

export default DeliveryForm;