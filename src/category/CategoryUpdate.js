// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useNavigate, useParams } from 'react-router-dom';
// import './categoryCreate.css';
//
// const CategoryUpdate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [parentId, setParentId] = useState(null);
//     const [parentCategories, setParentCategories] = useState([]); // 부모 카테고리만 저장
//     const [loading, setLoading] = useState(true);
//     const [error, setError] = useState(null);
//
//     const { id } = useParams(); // URL에서 카테고리 ID 가져오기
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         // 모든 카테고리 목록 불러오기
//         axios.get('http://localhost:8080/api/categories', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 // parentId가 null인 카테고리만 필터링하여 부모 카테고리 목록으로 저장
//                 const filteredParentCategories = response.data.filter(category => category.parentId === null);
//                 setParentCategories(filteredParentCategories);
//             })
//             .catch(error => {
//                 console.error('Error fetching categories:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//
//         // 현재 카테고리 정보 불러오기
//         axios.get(`http://localhost:8080/api/categories/${id}`, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 const category = response.data;
//                 setName(category.name);
//                 setDisplayOrder(category.displayOrder);
//                 setParentId(category.parentId || null); // 부모 카테고리가 없으면 null
//                 setLoading(false);
//             })
//             .catch(error => {
//                 console.error('Error fetching category data:', error);
//                 setError('카테고리 정보를 불러오는 중 오류가 발생했습니다.');
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//                 setLoading(false);
//             });
//     }, [id]);
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
//         axios.put(`http://localhost:8080/api/admin/categories/${id}`, categoryData, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 console.log('Category updated:', response.data);
//                 alert('카테고리가 성공적으로 수정되었습니다.');
//                 navigate('/admin/categories'); // 성공적으로 수정된 후 메인 화면으로 이동
//             })
//             .catch(error => {
//                 console.error('Error updating category:', error);
//                 alert('카테고리 수정 중 오류가 발생했습니다.');
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     };
//
//     if (loading) {
//         return <p>로딩 중...</p>;
//     }
//
//     if (error) {
//         return <p>{error}</p>;
//     }
//
//     return (
//         <div className="category-create-container">
//             <h2>카테고리 수정</h2>
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
//                         {parentCategories.map((category) => (
//                             <option key={category.id} value={category.id}>
//                                 {category.name}
//                             </option>
//                         ))}
//                     </select>
//                 </div>
//                 <button type="submit" className="submit-button">카테고리 수정</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryUpdate;


import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import './categoryCreate.css';

const CategoryUpdate = () => {
    const [name, setName] = useState('');
    const [displayOrder, setDisplayOrder] = useState('');
    const [parentId, setParentId] = useState(null);
    const [parentCategories, setParentCategories] = useState([]);
    const [image, setImage] = useState(null); // 이미지 파일 상태 추가
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        axios.get('http://localhost:8080/api/categories', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                const filteredParentCategories = response.data.filter(category => category.parentId === null);
                setParentCategories(filteredParentCategories);
            })
            .catch(error => {
                console.error('Error fetching categories:', error);
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
            });

        axios.get(`http://localhost:8080/api/categories/${id}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                const category = response.data;
                setName(category.name);
                setDisplayOrder(category.displayOrder);
                setParentId(category.parentId || null);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error fetching category data:', error);
                setError('카테고리 정보를 불러오는 중 오류가 발생했습니다.');
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
                setLoading(false);
            });
    }, [id]);

    const handleImageChange = (e) => {
        setImage(e.target.files[0]);
    };

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
                if (image) {
                    uploadCategoryImage(id); // 이미지가 있을 경우 이미지 업로드
                } else {
                    navigate('/admin/categories'); // 이미지가 없을 경우 바로 이동
                }
            })
            .catch(error => {
                console.error('Error updating category:', error);
                alert('카테고리 수정 중 오류가 발생했습니다.');
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
            });
    };

    const uploadCategoryImage = (categoryId) => {
        const token = localStorage.getItem('token');
        const formData = new FormData();
        formData.append('image', image);

        axios.post(`http://localhost:8080/api/admin/categories/${categoryId}/image`, formData, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(() => {
                alert("이미지가 성공적으로 수정되었습니다.");
                navigate('/admin/categories');
            })
            .catch(error => {
                console.error('Error uploading image:', error);
                alert("이미지 업로드에 실패했습니다.");
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
                <div className="form-group">
                    <label>카테고리 이미지:</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleImageChange}
                    />
                </div>
                <button type="submit" className="submit-button">카테고리 수정</button>
            </form>
        </div>
    );
};

export default CategoryUpdate;
