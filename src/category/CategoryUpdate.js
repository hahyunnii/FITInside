import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import './categoryCreate.css';

const CategoryUpdate = () => {
    const [name, setName] = useState('');
    const [displayOrder, setDisplayOrder] = useState('');
    const [parentId, setParentId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const { id } = useParams(); // URL에서 카테고리 ID 가져오기
    const navigate = useNavigate();

    useEffect(() => {
        // 카테고리 정보를 불러오기
        axios.get(`http://localhost:8080/api/admin/categories/${id}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                const category = response.data;
                setName(category.name);
                setDisplayOrder(category.displayOrder);
                // setParentId(category.parentId);
                setLoading(false); // 데이터 로딩 완료
            })
            .catch(error => {
                console.error('Error fetching category data:', error);
                setError('카테고리 정보를 불러오는 중 오류가 발생했습니다.');
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
                setLoading(false); // 에러 발생 시 로딩 종료
            });
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();

        const categoryData = {
            name,
            displayOrder: displayOrder ? Number(displayOrder) : null,
            parentId
        };

        axios.put(`http://localhost:8080/api/admin/categories/${id}`, categoryData, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                console.log('Category updated:', response.data);
                alert('카테고리가 성공적으로 수정되었습니다.');
                navigate('/category-admin'); // 성공적으로 수정된 후 메인 화면으로 이동
            })
            .catch(error => {
                console.error('Error updating category:', error);
                alert('카테고리 수정 중 오류가 발생했습니다.');
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
        <div className="category-create-container">
            <h2>카테고리 수정</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>이름:</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>표시 순서:</label>
                    <input
                        type="number"
                        value={displayOrder}
                        onChange={(e) => setDisplayOrder(e.target.value)}
                    />
                </div>
                <button type="submit" className="submit-button">카테고리 수정</button>
            </form>
        </div>
    );
};

export default CategoryUpdate;
