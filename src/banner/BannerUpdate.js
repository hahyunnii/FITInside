
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import './BannerCreate.css'; // CSS 파일 추가

const BannerUpdate = () => {
    const [title, setTitle] = useState(''); // 배너 제목
    const [displayOrder, setDisplayOrder] = useState(''); // 배너 표시 순서
    const [image, setImage] = useState(null); // 배너 이미지
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const { id } = useParams();
    const navigate = useNavigate();

    // 배너 정보 가져오기
    useEffect(() => {
        axios.get(`http://localhost:8080/api/banners/${id}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                const banner = response.data;
                setTitle(banner.title);
                setDisplayOrder(banner.displayOrder);
                setImage(banner.imageUrl || null);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error fetching banner data:', error);
                setError('배너 정보를 불러오는 중 오류가 발생했습니다.');
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
                setLoading(false);
            });
    }, [id]);

    const handleImageChange = (e) => {
        setImage(e.target.files[0]); // 파일을 선택한 경우 이미지로 설정
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('title', title);
        formData.append('displayOrder', displayOrder ? Number(displayOrder) : null);

        // 새로운 이미지가 선택된 경우에만 파일 추가, 아니면 기존 이미지 URL 추가
        if (image instanceof File) {
            formData.append('image', image);
        } else if (image) {
            formData.append('imageUrl', image); // 기존 이미지 URL을 전송
        }

        axios.put(`http://localhost:8080/api/admin/banners/${id}`, formData, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(response => {
                console.log('Banner updated:', response.data);
                alert('배너가 성공적으로 수정되었습니다.');
                navigate('/admin/banners');
            })
            .catch(error => {
                console.error('Error updating banner:', error);
                alert('배너 수정 중 오류가 발생했습니다.');
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
            });
    };

    if (loading) {
        return <p>로딩 중...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <div className="form-container">
            <h2>배너 수정</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>제목:</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>표시 순서:</label>
                    <input
                        type="number"
                        value={displayOrder}
                        onChange={(e) => setDisplayOrder(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>배너 이미지:</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleImageChange}
                    />
                    {image && !(image instanceof File) && (
                        <img src={image} alt="current banner" width="100" />
                    )}
                </div>
                <button type="submit" className="submit-button">배너 수정</button>
            </form>
        </div>
    );
};

export default BannerUpdate;

