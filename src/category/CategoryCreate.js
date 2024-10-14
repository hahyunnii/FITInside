// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import './categoryCreate.css';
//
// const CategoryCreate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [parentId, setParentId] = useState('');
//     const [categories, setCategories] = useState([]);
//     const [isDeleted, setIsDeleted] = useState(false);
//
//     useEffect(() => {
//         // 기존 카테고리를 불러와서 상위 카테고리 선택 드롭다운에 표시
//         axios.get('/api/categories') // 엔드포인트는 필요에 따라 조정하세요
//             .then(response => setCategories(response.data))
//             .catch(error => console.error('카테고리 불러오기 오류:', error));
//     }, []);
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         const categoryData = {
//             name,
//             displayOrder: displayOrder ? Number(displayOrder) : null,
//             isDeleted,
//             parentId: parentId ? Number(parentId) : null,
//         };
//
//         axios.post('/api/categories', categoryData) // 엔드포인트는 필요에 따라 조정하세요
//             .then(response => {
//                 console.log('카테고리 생성됨:', response.data);
//                 // 폼 초기화 또는 페이지 이동 등 추가 작업
//             })
//             .catch(error => console.error('카테고리 생성 오류:', error));
//     };
//
//     return (
//         <div>
//             <h2>카테고리 생성</h2>
//             <form onSubmit={handleSubmit}>
//                 <div>
//                     <label>이름:</label>
//                     <input
//                         type="text"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div>
//                     <label>표시 순서:</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <div>
//                     <label>상위 카테고리:</label>
//                     <select
//                         value={parentId}
//                         onChange={(e) => setParentId(e.target.value)}
//                     >
//                         <option value="">없음</option>
//                         {categories.map((category) => (
//                             <option key={category.id} value={category.id}>
//                                 {category.name}
//                             </option>
//                         ))}
//                     </select>
//                 </div>
//                 <button type="submit">카테고리 생성</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryCreate;

// import React, { useState } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
// import './categoryCreate.css';
//
// const CategoryCreate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [parentId, setParentId] = useState(null);
//
//     const navigate = useNavigate();
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         const categoryData = {
//             name,
//             displayOrder: displayOrder ? Number(displayOrder) : null,
//             parentId
//         };
//
//         axios.post('http://localhost:8080/api/admin/categories', categoryData, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 console.log('Category created:', response.data);
//                 navigate('/admin/categories'); // 성공적으로 생성된 후 메인 화면으로 이동
//             })
//             .catch(error => {
//                 console.error('Error creating category:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     };
//
//     return (
//         <div className="category-create-container">
//             <h2>카테고리 생성</h2>
//             <form onSubmit={handleSubmit}>
//                 <div className="form-group">
//                     <label>이름:</label>
//                     <input
//                         type="text"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>표시 순서:</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <button type="submit" className="submit-button">카테고리 생성</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryCreate;

// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
// import './categoryCreate.css';
//
// const CategoryCreate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [parentId, setParentId] = useState(null);
//     const [categories, setCategories] = useState([]);
//
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         // 서버에서 카테고리 목록 가져오기
//         axios.get('http://localhost:8080/api/categories', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 setCategories(response.data);
//             })
//             .catch(error => {
//                 console.error('Error fetching categories:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     }, []);
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         const categoryData = {
//             name,
//             displayOrder: displayOrder ? Number(displayOrder) : null,
//             parentId
//         };
//
//         axios.post('http://localhost:8080/api/admin/categories', categoryData, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 console.log('Category created:', response.data);
//                 navigate('/admin/categories'); // 성공적으로 생성된 후 메인 화면으로 이동
//             })
//             .catch(error => {
//                 console.error('Error creating category:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     };
//
//     return (
//         <div className="category-create-container">
//             <h2>카테고리 생성</h2>
//             <form onSubmit={handleSubmit}>
//                 <div className="form-group">
//                     <label>이름:</label>
//                     <input
//                         type="text"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>표시 순서:</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>부모 카테고리 선택:</label>
//                     <select
//                         value={parentId || ''}
//                         onChange={(e) => setParentId(e.target.value || null)}
//                     >
//                         <option value="">부모 카테고리 없음</option>
//                         {categories.map((category) => (
//                             <option key={category.id} value={category.id}>
//                                 {category.name}
//                             </option>
//                         ))}
//                     </select>
//                 </div>
//                 <button type="submit" className="submit-button">카테고리 생성</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryCreate;

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
