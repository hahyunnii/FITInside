import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import CategoryItem from './CategoryItem';
import './CategoryList.css';

const CategoryList = ({ userRole }) => {
    const [categories, setCategories] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // 서버에서 카테고리 목록 가져오기
        axios.get('http://localhost:8080/api/categories', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                const allCategories = response.data;

                // mainDisplayOrder가 null이 아닌 카테고리 필터링 및 정렬
                const mainDisplayCategories = allCategories
                    .filter(category => category.mainDisplayOrder !== null)
                    .sort((a, b) => a.mainDisplayOrder - b.mainDisplayOrder);

                setCategories(mainDisplayCategories);
            })
            .catch(error => {
                console.error('Error fetching categories:', error);
                alert('카테고리 목록을 불러오는 중 문제가 발생했습니다.');
            });
    }, []);

    const handleAddCategoryClick = () => {
        navigate('/admin/categories');
    };

    return (
        <div className="category-list">
            {categories.length > 0 ? (
                categories.map((category) => (
                    <CategoryItem
                        key={category.id}
                        category={{
                            id: category.id,
                            name: category.name,
                            link: `/products/category/${category.id}`,
                            imageUrl: category.imageUrl // 이미지 URL 추가
                        }}
                    />
                ))
            ) : (
                <p>등록된 카테고리가 없습니다.</p>
            )}
            {userRole === 'ROLE_ADMIN' && (
                <button className="add-category-button" onClick={handleAddCategoryClick}>
                    카테고리 추가
                </button>
            )}
        </div>
    );
};

export default CategoryList;
