import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const MyPage = () => {
    const [userInfo, setUserInfo] = useState(null); // 사용자 정보를 저장할 상태
    const [error, setError] = useState(''); // 에러 상태

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const token = localStorage.getItem('token'); // 로컬 스토리지에서 토큰 가져오기

                const response = await axios.get('http://localhost:8080/api/user/me', {
                    headers: {
                        Authorization: `Bearer ${token}`, // Authorization 헤더에 토큰 추가
                    },
                });

                setUserInfo(response.data); // 사용자 정보를 상태에 저장
            } catch (err) {
                setError('사용자 정보를 가져오는 데 실패했습니다.');
            }
        };

        fetchUserData(); // 사용자 데이터 가져오기
    }, []); // 컴포넌트가 처음 렌더링될 때 한 번만 실행

    const navigate = useNavigate(); // useNavigate 훅 사용

    const handleCouponManagementClick = () => {
        navigate('/coupons'); // 쿠폰 관리 페이지로 이동
    };

    return (
        <div className="container mt-5">
            <h2 className="text-center mb-4">내 정보</h2>
            {error && <p className="text-danger text-center">{error}</p>}
            {userInfo ? (
                <div className="card p-4">
                    <div className="mb-3" style={{border: '1px solid #ddd', borderRadius: '8px', padding: '10px'}}>
                        <h5>이메일</h5>
                        <p>{userInfo.email}</p>
                    </div>
                    <div className="mb-3" style={{border: '1px solid #ddd', borderRadius: '8px', padding: '10px'}}>
                        <h5>이름</h5>
                        <p>{userInfo.userName}</p>
                    </div>
                    <div className="mb-3" style={{border: '1px solid #ddd', borderRadius: '8px', padding: '10px'}}>
                        <h5>전화번호</h5>
                        <p>{userInfo.phone}</p>
                    </div>

                    {/*내 쿠폰 관리*/}
                    <div className="mb-3" style={{border: '1px solid #ddd', borderRadius: '8px', padding: '10px'}}>
                        <h5>쿠폰</h5>
                        <button className="btn btn-light text-dark mb-4" style={{border: '1px solid #ced4da'}}
                            onClick={handleCouponManagementClick}>
                            쿠폰 관리하기
                        </button>
                    </div>
                </div>
            ) : (
                <p className="text-center">로딩 중...</p>
            )}
        </div>
    );
};

export default MyPage;