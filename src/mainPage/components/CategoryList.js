import React from 'react';
import CategoryItem from './CategoryItem';
import './CategoryList.css';

// const categories = [
//     { id: 1, name: '여성의류', image: 'path/to/womenswear.jpg', link: '/womenswear' },
//     { id: 2, name: '남성의류', image: 'path/to/menswear.jpg', link: '/menswear' },
//     // 다른 카테고리를 추가
//
// ];

const categories = [
    { id: 1, name: '여성의류', image: require('../assets/images/여성 상의.png'), link: '/womenswear' },
    { id: 2, name: '남성의류', image: require('../assets/images/남성 상의.png'), link: '/menswear' },
    // 다른 카테고리를 추가
];


const CategoryList = () => {
    return (
        <div className="category-list">
            {categories.map((category) => (
                <CategoryItem key={category.id} category={category} />
            ))}
        </div>
    );
};

export default CategoryList;

