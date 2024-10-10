import React, { useEffect, useState } from 'react';
import '../coupon.css';
import CouponCreateModal from "./CouponCreateModal";
import CouponMemberModal from "./CouponMemberModal";

const CouponAdmin = () => {
    const [coupons, setCoupons] = useState([]);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [categories, setCategories] = useState([]);
    const [isMemberModalOpen, setIsMemberModalOpen] = useState(false);
    const [selectedCouponId, setSelectedCouponId] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [includeInactiveCoupons, setIncludeInactiveCoupons] = useState(true);
    useEffect(() => {
        fetchCoupons(currentPage, includeInactiveCoupons);
        fetchCategories();
    }, [currentPage, includeInactiveCoupons]);

    const fetchCoupons = async (page, includeInActiveCoupons) => {
        try {
            const response = await fetch(`http://localhost:8080/api/admin/coupons?page=${page}&includeInActiveCoupons=${includeInActiveCoupons}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
            });

            if (!response.ok) {
                throw new Error('네트워크 응답이 정상적이지 않습니다.');
            }

            const data = await response.json();
            setCoupons(data.coupons);
            setTotalPages(data.totalPages); // 총 페이지 수 설정
        } catch (error) {
            console.error('쿠폰 목록을 가져오는 데 실패했습니다.', error);
        }
    };

    const fetchCategories = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/categories', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
            });

            if (!response.ok) {
                throw new Error('카테고리 목록을 가져오는 데 실패했습니다.');
            }

            const data = await response.json();
            setCategories(data);
        } catch (error) {
            console.error('카테고리 목록을 가져오는 데 실패했습니다.', error);
        }
    };

    const handleCreateCoupon = () => {
        setModalIsOpen(true);
    };

    const handleModalClose = () => {
        setModalIsOpen(false);
    };

    const handleMemberButtonClick = (id) => {
        setSelectedCouponId(id);
        setIsMemberModalOpen(true);
    };

    const handleCloseMemberModal = () => {
        setIsMemberModalOpen(false);
        setSelectedCouponId(null);
    };

    const deactivateCoupon = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/admin/coupons/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });

            if (response.ok) {
                alert('쿠폰이 비활성화되었습니다.');
                fetchCoupons(currentPage, includeInactiveCoupons);
            } else {
                const errorData = await response.json();
                alert(`오류 발생: ${errorData.message}`);
            }
        } catch (error) {
            alert(`네트워크 오류 발생: ${error.message}`);
        }
    };

    const handleNextPage = () => {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
        }
    };

    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    const handleIncludeInactiveChange = (e) => {
        setIncludeInactiveCoupons(e.target.value === 'true');
    };

    return (
        <div className="container mt-5">
            <h2 className="text-center mb-4">쿠폰 관리</h2>
            <div className="d-flex flex-row justify-content-between">
                <button className="btn btn-light text-dark mb-4" style={{border: '1px solid #ced4da'}}
                        onClick={handleCreateCoupon}>
                    쿠폰 추가
                </button>

                {/* 유효하지 않은 쿠폰 포함 드롭다운 */}
                <div className="mb-4">
                    <select
                        className="form-select"
                        value={includeInactiveCoupons ? 'true' : 'false'}
                        onChange={(e) => setIncludeInactiveCoupons(e.target.value === 'true')}
                    >
                        <option value="false">활성화 쿠폰만 보기</option>
                        <option value="true">전체 쿠폰 보기</option>
                    </select>
                </div>
            </div>


            <div className="row">
                {coupons.map((coupon) => (
                    <div className="couponWrap" key={coupon.id}>
                        <div
                            className={`coupon couponLeft ${coupon.active ? (coupon.type === 'AMOUNT' ? 'red' : 'blue') : 'black'}`}>
                            <h1 className="m-0 d-flex justify-content-between" style={{color: "white"}}>
                                {coupon.name}
                                <span>{coupon.active ? '사용가능' : '사용불가'}</span>
                            </h1>
                            <div className="title mt-4 mb-2">
                                <strong>{coupon.code}</strong>
                            </div>
                            <div className="name mb-0">
                                <strong>{coupon.categoryName}</strong>
                            </div>
                            <div className="name m-0">
                                ({coupon.type === 'AMOUNT' ? '고정 금액' : '퍼센티지'})&nbsp;
                                <strong>{coupon.type === 'AMOUNT' ? `${new Intl.NumberFormat().format(coupon.value)}원` : `${coupon.percentage}%`}</strong> 할인
                            </div>
                            <div className="name m-0">
                                {coupon.minValue === 0 ? '최소 주문 금액 없음' : new Intl.NumberFormat().format(coupon.minValue) + ' 원 부터 적용 가능'}
                            </div>
                            <div className="name m-0">
                                ~ {coupon.expiredAt} 까지 적용 가능
                            </div>
                        </div>

                        <div className={`coupon couponRight ${coupon.active ? (coupon.type === 'AMOUNT' ? 'red' : 'blue') : 'black'}`}>
                            <h1 className="m-0" style={{ color: "white" }}>관리자</h1>
                            <div className="mt-5 d-flex flex-column justify-content-center align-items-center">
                                <button className="btn btn-light mb-2" onClick={() => handleMemberButtonClick(coupon.id)} style={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    height: '40px',
                                    width: '40px'
                                }}>
                                    <span className="material-icons">group</span>
                                </button>
                                {coupon.active && (
                                    <button
                                        className="btn btn-light"
                                        style={{
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            height: '40px',
                                            width: '40px'
                                        }}
                                        onClick={() => {
                                            const confirmed = window.confirm("비활성화 이후에는 다시 활성화 할 수 없습니다.\n정말 비활성화 하시겠습니까?");
                                            if (confirmed) {
                                                deactivateCoupon(coupon.id);
                                            }
                                        }}
                                    >
                                        <span className="material-icons" style={{ color: 'red' }}>block</span>
                                    </button>
                                )}
                                {selectedCouponId && (
                                    <CouponMemberModal
                                        isMemberModalOpen={isMemberModalOpen}
                                        handleCloseMemberModal={handleCloseMemberModal}
                                        couponId={selectedCouponId}
                                    />
                                )}
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* 페이징 버튼 */}
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', marginTop: '20px', marginBottom: '50px' }}>
                <button className="btn btn-secondary me-2" onClick={handlePreviousPage} disabled={currentPage === 1}>
                    이전
                </button>
                <span>{currentPage} / {totalPages}</span>
                <button className="btn btn-secondary ms-2" onClick={handleNextPage} disabled={currentPage === totalPages}>
                    다음
                </button>
            </div>

            <CouponCreateModal
                isOpen={modalIsOpen}
                onRequestClose={handleModalClose}
                onCreate={() => {
                    fetchCoupons(currentPage, includeInactiveCoupons); // 쿠폰 생성 후 현재 페이지에서 쿠폰 목록 새로고침
                }}
                categories={categories}
            />
        </div>
    );
};

export default CouponAdmin;
