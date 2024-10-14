import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './categoryCreate.css';

const CategoryCreate = () => {
    const [name, setName] = useState('');
    const [displayOrder, setDisplayOrder] = useState('');
    const [parentId, setParentId] = useState(null);
    const [parentCategories, setParentCategories] = useState([]); // 부모 카테고리만 저장

    const navigate = useNavigate();

    useEffect(() => {
        // 서버에서 카테고리 목록 가져오기
        axios.get('http://localhost:8080/api/categories', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                // parentId가 null인 카테고리만 필터링
                const parentCategories = response.data.filter(category => category.parentId === null);
                setParentCategories(parentCategories);
            })
            .catch(error => {
                console.error('Error fetching categories:', error);
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
            });
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();

        const categoryData = {
            name,
            displayOrder: displayOrder ? Number(displayOrder) : null,
            parentId
        };

        axios.post('http://localhost:8080/api/admin/categories', categoryData, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                console.log('Category created:', response.data);
                navigate('/admin/categories'); // 성공적으로 생성된 후 메인 화면으로 이동
            })
            .catch(error => {
                console.error('Error creating category:', error);
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
            });
    };

    return (
        <div className="category-create-container">
            <h2>카테고리 생성</h2>
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
                <div className="form-group">
                    <label>부모 카테고리 선택:</label>
                    <select
                        value={parentId || ''}
                        onChange={(e) => setParentId(e.target.value || null)}
                    >
                        <option value="">부모 카테고리 없음</option>
                        {parentCategories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>
                <button type="submit" className="submit-button">카테고리 생성</button>
            </form>
        </div>
    );
};

export default CategoryCreate;
