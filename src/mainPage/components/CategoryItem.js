// import React from 'react';
// import { Link } from 'react-router-dom';
//
// const CategoryItem = ({ category }) => {
//     return (
//         <div className="category-item">
//             <Link to={category.link}>
//                 <img src={category.image} alt={category.name} />
//                 <span>{category.name}</span>
//             </Link>
//         </div>
//     );
// };
//
// export default CategoryItem;

// 일단 이미지 생략
// import React from 'react';
// import { Link } from 'react-router-dom';
//
// const CategoryItem = ({ category }) => {
//     return (
//         <div className="category-item">
//             <Link to={category.link}>
//                 <span>{category.name}</span>
//             </Link>
//         </div>
//     );
// };
//
// export default CategoryItem;


import React from 'react';
import { Link } from 'react-router-dom';

const CategoryItem = ({ category }) => {
    return (
        <div className="category-item">
            {/* 이미지가 있는 경우에만 렌더링 */}
            {category.imageUrl && (
                <img src={category.imageUrl} alt={category.name} className="category-image" />
            )}
            <Link to={category.link}>
                <span>{category.name}</span>
            </Link>
        </div>
    );
};

export default CategoryItem;
