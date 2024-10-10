import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';

const CouponMemberModal = ({ isMemberModalOpen, handleCloseMemberModal, couponId }) => {
    const [memberModalData, setMemberModalData] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const fetchMembers = async (id, page) => {
        const response = await fetch(`http://localhost:8080/api/admin/coupons/${id}?page=${page}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
        });

        if (!response.ok) {
            throw new Error('회원 목록을 가져오는 데 실패했습니다.');
        }

        const data = await response.json();
        setMemberModalData(data);
        setTotalPages(data.totalPages);
    };

    useEffect(() => {
        if (isMemberModalOpen && couponId) {
            fetchMembers(couponId, currentPage);
        }
    }, [isMemberModalOpen, couponId, currentPage]);

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

    return (
        <Modal
            isOpen={isMemberModalOpen}
            onRequestClose={handleCloseMemberModal}
            ariaHideApp={false}
            style={{
                content: {
                    maxWidth: `50%`,
                    maxHeight: `70%`,
                    margin: 'auto',
                    padding: '40px',
                    borderRadius: '10px'
                },
                overlay: {
                    backgroundColor: 'rgba(0, 0, 0, 0.1)'
                }
            }}
        >
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <h2 className="text-center mb-4">보유 회원 목록</h2>
                <button onClick={handleCloseMemberModal} style={{
                    background: 'none',
                    border: 'none',
                    fontSize: '24px',
                    cursor: 'pointer',
                    marginTop: '-30px',
                }}>&times;</button>
            </div>

            {/* 모달 내용 */}
            <div style={{flex: 1, overflowY: 'auto'}}> {/* 내용이 많아질 경우 스크롤 가능 */}
                {memberModalData ? (
                    <div>
                        {memberModalData.members && memberModalData.members.length > 0 ? (
                            <ol>
                                {memberModalData.members.map((member, index) => (
                                    <li key={index}>
                                        <p>{member.userName} ({member.email})</p>
                                    </li>
                                ))}
                            </ol>
                        ) : (
                            <strong>보유한 회원이 없습니다.</strong>
                        )}
                    </div>
                ) : (
                    <p>로딩 중...</p>
                )}
            </div>

            {/* 페이징 버튼을 모달 하단에 위치시키기 */}
            <div style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                position: 'absolute',
                bottom: '30px',
                left: '20px',
                right: '20px'
            }}>
                <button
                    className="btn btn-secondary me-2" // 부트스트랩 버튼 스타일 및 오른쪽 여백
                    onClick={handlePreviousPage}
                    disabled={currentPage === 1}
                >
                    이전
                </button>
                <span className="mx-3"> {/* 텍스트 간격을 위한 여백 추가 */}
                    {currentPage} / {totalPages}
    </span>
                <button
                    className="btn btn-primary ms-2" // 부트스트랩 버튼 스타일 및 왼쪽 여백
                    onClick={handleNextPage}
                    disabled={currentPage === totalPages}
                >
                    다음
                </button>
            </div>

        </Modal>
    );
};

export default CouponMemberModal;
