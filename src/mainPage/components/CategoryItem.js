import React from 'react';
import { Link } from 'react-router-dom';
import './CategoryItem.css';

const CategoryItem = ({ category }) => {
    return (
        <div className="category-item">
            <Link to={category.link} className="category-link">
                {/* 이미지가 있는 경우에만 렌더링 */}
                {category.imageUrl && (
                    <img src={category.imageUrl} alt={category.name} className="category-image" />
                )}
                <span>{category.name}</span>
            </Link>
        </div>
    );
};

export default CategoryItem;
