import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './BannerAdminMain.css'; // 추가적으로 필요한 CSS 파일

const BannerAdminMain = () => {
    const [banners, setBanners] = useState([]);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        fetchBanners();
    }, []);

    const fetchBanners = () => {
        // 배너 목록 가져오기 (Authorization 헤더 추가)
        axios.get('http://localhost:8080/api/banners', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                const sortedBanners = response.data.sort((a, b) => a.displayOrder - b.displayOrder);
                setBanners(sortedBanners);
            })
            .catch(error => {
                console.error('Error fetching banners:', error);
                setError('배너 목록을 가져오는 중 오류가 발생했습니다.');
            });
    };

    const handleCreateBanner = () => {
        navigate('/admin/banner/create');
    };

    const handleUpdateBanner = (id) => {
        navigate(`/admin/banner/update/${id}`);
    };

    const handleDeleteBanner = (id) => {
        if (!window.confirm('정말로 이 배너를 삭제하시겠습니까?')) return;

        // 배너 삭제 요청 (Authorization 헤더 추가)
        axios.delete(`http://localhost:8080/api/admin/banners/${id}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(() => {
                setBanners(banners.filter(banner => banner.id !== id)); // 상태를 업데이트하여 삭제된 배너를 목록에서 제거
                alert('배너가 성공적으로 삭제되었습니다.');
            })
            .catch(error => {
                console.error('Error deleting banner:', error);
                alert('배너 삭제 중 오류가 발생했습니다.');
            });
    };

    return (
        <div>
            <h1>광고 관리</h1>
            <button onClick={handleCreateBanner}>광고 생성하기</button>

            {error && <p className="error-message">{error}</p>} {/* 에러 메시지 표시 */}

            <table className="banner-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>이미지</th>
                    <th>제목</th>
                    <th>정렬 순서</th>
                    <th>URL</th> {/* URL 열 추가 */}
                    <th>수정</th>
                    <th>삭제</th>
                </tr>
                </thead>
                <tbody>
                {banners.length > 0 ? (
                    banners.map(banner => (
                        <tr key={banner.id}>
                            <td>{banner.id}</td>
                            <td>
                                <img src={banner.imageUrl} alt={banner.title} width="100" />
                            </td>
                            <td>{banner.title}</td>
                            <td>{banner.displayOrder}</td>
                            <td>{banner.targetUrl ? <a href={banner.targetUrl} target="_blank" rel="noopener noreferrer">{banner.targetUrl}</a> : 'No URL'}</td> {/* URL 표시 */}
                            <td>
                                <button onClick={() => handleUpdateBanner(banner.id)}>수정</button>
                            </td>
                            <td>
                                <button onClick={() => handleDeleteBanner(banner.id)}>삭제</button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="7">등록된 배너가 없습니다.</td> {/* 열 개수에 맞게 수정 */}
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default BannerAdminMain;
