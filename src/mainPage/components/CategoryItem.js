import React from 'react';
import { Link } from 'react-router-dom';

const CategoryItem = ({ category }) => {
    return (
        <div className="category-item">
            <Link to={category.link}>
                <img src={category.image} alt={category.name} />
                <span>{category.name}</span>
            </Link>
        </div>
    );
};

export default CategoryItem;

