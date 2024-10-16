import React, { useState, useEffect, forwardRef, useImperativeHandle } from 'react';
import './deliveryForm.css';
import PostcodeSearch from "./PostcodeSearch";

const DeliveryForm = forwardRef(({ initialValues = {} }, ref) => {
    const [postalCode, setPostalCode] = useState(initialValues.postalCode || '');
    const [deliveryAddress, setDeliveryAddress] = useState(initialValues.deliveryAddress || '');
    const [detailedAddress, setDetailedAddress] = useState(initialValues.detailedAddress || '');
    const [deliveryReceiver, setDeliveryReceiver] = useState(initialValues.deliveryReceiver || '');
    const [phoneFirst, setPhoneFirst] = useState('010');
    const [phoneMiddle, setPhoneMiddle] = useState('');
    const [phoneLast, setPhoneLast] = useState('');

    useEffect(() => {
        // 초기값을 설정하기 위한 useEffect: 컴포넌트가 처음 렌더링될 때만 초기값을 설정
        if (initialValues) {
            setPostalCode(initialValues.postalCode || '');
            setDeliveryAddress(initialValues.deliveryAddress || '');
            setDetailedAddress(initialValues.detailedAddress || '');
            setDeliveryReceiver(initialValues.deliveryReceiver || '');

            // 전화번호를 '-'로 분리하여 각 필드에 할당
            if (initialValues.deliveryPhone) {
                const phoneParts = initialValues.deliveryPhone.split('-');
                if (phoneParts.length === 3) {
                    setPhoneFirst(phoneParts[0] || '010');
                    setPhoneMiddle(phoneParts[1] || '');
                    setPhoneLast(phoneParts[2] || '');
                }
            }
        }
    }, []); // 빈 배열로 두어 컴포넌트가 처음 렌더링될 때만 실행되도록 합니다.

    // 부모 컴포넌트에서 호출
    useImperativeHandle(ref, () => ({
        getFormData: () => {
            const deliveryPhone = `${phoneFirst}-${phoneMiddle}-${phoneLast}`;
            console.log({
                postalCode,
                deliveryAddress,
                detailedAddress,
                deliveryReceiver,
                deliveryPhone
            });
            return { postalCode, deliveryAddress, detailedAddress, deliveryReceiver, deliveryPhone };
        }
    }));

    return (
        <div className="delivery-form">
            <h4>배송 정보</h4>
            <table className="delivery-table">
                <tbody>
                    <tr>
                        <td className="label-cell"><label>받는 분</label></td>
                        <td className="input-cell">
                            <input
                                type="text"
                                value={deliveryReceiver}
                                onChange={(e) => setDeliveryReceiver(e.target.value)}
                                required
                                placeholder="받는 분의 이름을 입력해주세요"
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="label-cell"><label>연락처</label></td>
                        <td className="input-cell phone-number-container">
                            <select
                                value={phoneFirst}
                                onChange={(e) => setPhoneFirst(e.target.value)}
                            >
                                <option value="010">010</option>
                                <option value="011">011</option>
                                <option value="016">016</option>
                                <option value="017">017</option>
                            </select>
                            <input
                                type="text"
                                value={phoneMiddle}
                                onChange={(e) => setPhoneMiddle(e.target.value)}
                                required
                                maxLength={4}
                            />
                            <input
                                type="text"
                                value={phoneLast}
                                onChange={(e) => setPhoneLast(e.target.value)}
                                required
                                maxLength={4}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="label-cell"><label>우편번호</label></td>
                        <td className="input-cell">
                            <input
                                type="text"
                                value={postalCode}
                                onChange={(e) => setPostalCode(e.target.value)}
                                readOnly
                                required
                            />
                            <PostcodeSearch setPostalCode={setPostalCode} setDeliveryAddress={setDeliveryAddress} />
                        </td>
                    </tr>
                    <tr>
                        <td className="label-cell"><label>주소</label></td>
                        <td className="input-cell">
                            <input
                                type="text"
                                value={deliveryAddress}
                                onChange={(e) => setDeliveryAddress(e.target.value)}
                                readOnly
                                required
                            />
                        </td>
                    </tr>
                    <tr>
                        <td className="label-cell"><label>상세주소</label></td>
                        <td className="input-cell">
                            <input
                                type="text"
                                value={detailedAddress}
                                onChange={(e) => setDetailedAddress(e.target.value)}
                            />
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
});

export default DeliveryForm;
